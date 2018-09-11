/*
 * Copyright (c) 2018.  Dan Brown <dan@likethecolor.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.likethecolor.solr.indexer.handler.indexer;

import com.likethecolor.solr.indexer.dynamic.DynamicFieldProcessor;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicField;
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicFieldParser;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldValueSetter;
import com.likethecolor.solr.indexer.field.FieldsParser;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import com.likethecolor.solr.indexer.reader.DataReader;
import com.likethecolor.solr.indexer.util.conversion.ToListConversion;
import com.likethecolor.solr.indexer.util.conversion.ToTimeConversion;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractSolrDocumentHandler implements DocumentHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSolrDocumentHandler.class);
  private static long batchCount = 0;
  private final DataReader dataReader;
  private FieldValueSetter fieldValueSetter;
  private Configuration configuration;

  public AbstractSolrDocumentHandler(final Configuration configuration, final DataReader dataReader) {
    this.dataReader = dataReader;
    fieldValueSetter = new FieldValueSetter(configuration);
    this.configuration = configuration;
  }

  /**
   * Sets up the processors used for the examples. If there are 10 CSV columns
   * there must be 10 processors defined. Empty columns are read as null (hence
   * the NotNull() for mandatory columns).
   *
   * @param fieldDefinitions map of field definitions
   * @return the cell processors
   */
  abstract protected CellProcessor[] getProcessors(final Map<String, FieldDefinition> fieldDefinitions);

  /**
   * Loop through field definitions and add the value in the corresponding row
   * of values to a solr document.
   *
   * @param rowValues list of values from the data file
   * @return solr document populated with the values from the row of values
   */
  abstract protected SolrInputDocument buildSolrInputDocument(List<Object> rowValues);

  public long getBatchCount() {
    return batchCount;
  }

  public long handle(final CloudSolrClient cloudSolrServer) throws SolrServerException, IOException, InterruptedException, ExecutionException {
    final SoftCommitter softCommitter = new SoftCommitter(configuration, cloudSolrServer);
    final long startTimeInMS = System.currentTimeMillis();
    long numDocs = 0;
    final Map<String, FieldDefinition> fieldDefinitions = getFieldDefinitions();
    List<SolrInputDocument> docs = new ArrayList<>();
    List<Object> rowValues;
    SolrInputDocument doc;

    ExecutorService es = Executors.newFixedThreadPool(getConfiguration().getThreadCount());
    List<Future<SolrjUpdateResponse>> futures = new ArrayList<>();

    LOGGER.info("start: file parsing");
    try {
      // The argument, if true, ensures that getHeader is only called when reading
      // the first line (as that's where the header is meant to be)
      // Either way, true or false, it returns the first row.
      //
      final Object[] headerOrFirstRow = dataReader.getHeader(getConfiguration().firstRowIsHeader()); // skip the header (can't be used with CsvListReader)
      if(!getConfiguration().firstRowIsHeader() && headerOrFirstRow != null) {
        doc = buildSolrInputDocument(Arrays.asList(headerOrFirstRow));
        addDocToDocsList(doc, docs, dataReader.getLineNumber(), Arrays.asList(headerOrFirstRow));
      }

      final CellProcessor[] processors = getProcessors(fieldDefinitions);
      while((rowValues = dataReader.read(processors)) != null) {
        doc = buildSolrInputDocument(rowValues);
        addDocToDocsList(doc, docs, dataReader.getLineNumber(), rowValues);

        int docSize = docs.size();
        if(docSize > 0 && (docSize % getConfiguration().getBatchSize()) == 0) {
          final IndexService service = getIndexService(cloudSolrServer, docs);
          docs.clear();
          futures.add(es.submit(service));
          if(futures.size() < getConfiguration().getThreadCount()) {
            continue;
          }
          while(futures.size() >= getConfiguration().getThreadCount()) {
            for(int i = 0; i < futures.size(); i++) {
              final Future<SolrjUpdateResponse> future = futures.get(i);
              if(future.isDone()) {
                futures.remove(future);
                final SolrjUpdateResponse response = future.get();
                numDocs += response.getDocSize();
                logBatchMessage(response);
                softCommitter.doSoftCommit(batchCount);
                if(numDocs != 0 && numDocs % REPORT_MODULUS == 0) {
                  LOGGER.info(String.format("processed %d documents [elapsed time: %s]", numDocs, new ToTimeConversion(System.currentTimeMillis() - startTimeInMS).getMinutesSecondsMillisecondsFromMilliseconds()));
                }
              }
            }
          }
        }
      }

      //catch any docs that didn't make in a batch
      if(docs.size() > 0) {
        final IndexService service = getIndexService(cloudSolrServer, docs);
        docs.clear();
        final SolrjUpdateResponse response = service.call();
        numDocs += response.getDocSize();
        logBatchMessage(response);
        softCommitter.doSoftCommit(batchCount);
      }
      numDocs += cleanUpRemainingFutures(futures);
      LOGGER.info("end: file parsing and indexing [{}]", new ToTimeConversion(System.currentTimeMillis() - startTimeInMS).getMinutesSecondsMillisecondsFromMilliseconds());
    }
    catch(SuperCsvConstraintViolationException e) {
      LOGGER.error("error parsing csv data");
      LOGGER.error("error could be due to a value in the data file is null when it was expected to be not null");
      LOGGER.error(String.format("error could be due to the list fields (fields, skip fields) use the correct list delimiter '%s'", getConfiguration().getCsvDelimiter()));
      throw e;
    }
    catch(SuperCsvException e) {
      LOGGER.error("error parsing csv data");
      LOGGER.error("error could be due the number of elements in the fields configuration does not match the number of fields in the data file");
      LOGGER.error(String.format("error could be due to the list fields (fields, skip fields) use the correct list delimiter '%s'", getConfiguration().getCsvDelimiter()));
      throw e;
    }
    catch(InterruptedException e) {
      LOGGER.error("thread interrupted", e);
      throw e;
    }
    catch(ExecutionException e) {
      LOGGER.error("execution of thread", e);
      throw e;
    }
    finally {
      if(dataReader != null) {
        try {
          dataReader.close();
        }
        catch(IOException ignore) {
        }
      }
    }
    return numDocs;
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  protected FieldValueSetter getFieldValueSetter() {
    return fieldValueSetter;
  }

  protected UniqueKeyFieldValueGenerator getUniqueKeyFieldValueGenerator() {
    return new UniqueKeyFieldValueGenerator(getConfiguration());
  }
  
  protected Map<String, FieldDefinition> getFieldDefinitions() {
    return getFieldDefinitions(getConfiguration().getFields());
  }

  protected Map<String, FieldDefinition> getLiteralsDefinitions() {
    return getLiteralsFieldDefinitions(getConfiguration().getLiterals());
  }

  protected Map<String, DynamicField> getDynamicFields() {
    return new DynamicFieldParser().parse(getConfiguration().getDynamicFields());
  }

  protected List<String> getSkipFields() {
    return new ToListConversion(getConfiguration().getSkipFields()).toList();
  }

  protected IndexService getIndexService(CloudSolrClient solr, List<SolrInputDocument> docs) {
    return new IndexService(configuration, solr, docs);
  }

  /**
   * Return a mapping of field definitions.
   *
   * @param fields list of fields as a delimited string
   *
   * @return mapping of field definitions
   */
  private Map<String, FieldDefinition> getFieldDefinitions(final String fields) {
    return new FieldsParser(configuration).parse(fields);
  }

  /**
   * Return a mapping of literals field definitions.
   *
   * @param fields list of fields as a delimited string
   *
   * @return mapping of literals field definitions
   */
  private Map<String, FieldDefinition> getLiteralsFieldDefinitions(final String fields) {
    return new FieldsParser(configuration).parse(fields, true);
  }

  /**
   * Add the document to the document list and log what is being added.
   *
   * @param doc document to add
   * @param docs list to add to
   * @param lineNumber line number being processed
   * @param rowValues list of data making up the document (for log message)
   */
  protected void addDocToDocsList(final SolrInputDocument doc, final List<SolrInputDocument> docs, final int lineNumber, final List<Object> rowValues) {
    if(doc != null) {
      docs.add(doc);
      LOGGER.debug("adding document: lineNo={}, rowValues={}", lineNumber, rowValues);
    }
  }

  protected void handleDynamicClasses(SolrInputDocument solrInputDocument) {
    Map<String, DynamicField> dynamicFieldMap = getDynamicFields();

    Configuration configuration = new Configuration();

    Set<String> fieldNames = dynamicFieldMap.keySet();
    for(String fieldName : fieldNames) {
      DynamicField dynamicField = dynamicFieldMap.get(fieldName);
      DynamicFieldProcessor processor = new DynamicFieldProcessor(configuration, solrInputDocument);
      processor.process(dynamicField);
    }
  }

  /**
   * If the code does not wait for all remaining futures to finish before
   * committing and cleaning up solr cloud resources batches will get dropped.
   *
   * @param futures collection of futures remaining to be processed
   *
   * @return number of documents that the remaining futures process
   *
   * @throws ExecutionException if the computation threw an exception
   * @throws InterruptedException if the current thread was interrupted while waiting
   */
  private int cleanUpRemainingFutures(List<Future<SolrjUpdateResponse>> futures) throws ExecutionException, InterruptedException {
    int numDocs = 0;
    LOGGER.info("batches left: {}", futures.size());
    if(futures.size() > 0) {
      while(futures.size() > 0) {
        Iterator<Future<SolrjUpdateResponse>> iter = futures.iterator();
        while(iter.hasNext()) {
          final Future<SolrjUpdateResponse> future = iter.next();
          if(future.isDone()) {
            iter.remove();
            final SolrjUpdateResponse response = future.get();
            numDocs += response.getDocSize();
            logBatchMessage(response);
          }
        }
      }
    }
    return numDocs;
  }

  private void logBatchMessage(final SolrjUpdateResponse response) {
    batchCount++;
    LOGGER.info(String.format("%5d: Added a batch of %5d documents [total %5d = %s]", batchCount, response.getDocSize(), response.getUpdateResponse().getElapsedTime(), new ToTimeConversion(response.getUpdateResponse().getElapsedTime()).getMinutesSecondsMillisecondsFromMilliseconds()));
  }
}
