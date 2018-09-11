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

package com.likethecolor.solr.indexer;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.ConfigurationBuilder;
import com.likethecolor.solr.indexer.configuration.ConfigurationValidator;
import com.likethecolor.solr.indexer.configuration.ZkHostParser;
import com.likethecolor.solr.indexer.handler.indexer.AbstractSolrDocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.DocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentHandlerFactory;
import com.likethecolor.solr.indexer.util.conversion.ToTimeConversion;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * This class uses SolrJ to index data found in a data file.  This should be
 * more effective than using something like curl as this code makes use of
 * the zookeeper to figure out where to send the data.
 */
public class Indexer {
  private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);

  public Indexer() {
  }

  public static void main(String[] args) throws InterruptedException {
    final long startTime = System.currentTimeMillis();
    LOGGER.info("---------------------------------------------------------------");
    LOGGER.info("starting index process");

    final Configuration configuration = new ConfigurationBuilder().build(args);

    boolean success = configuration != null;
    if(success) {
      final CloudSolrClient cloudSolrClient = buildClient(configuration);

      final Indexer indexer = new Indexer();
      success = indexer.index(configuration, cloudSolrClient);
    }
    long duration = System.currentTimeMillis() - startTime;
    LOGGER.info("finished index process with a total time of {} ms [{}]", duration, new ToTimeConversion(duration).getMinutesSecondsMillisecondsFromMilliseconds());

    if(!success) {
      System.exit(1);
    }
    System.exit(0);
  }

  private static CloudSolrClient buildClient(Configuration configuration) {
    // TODO: This zookeeper stuff should be in the Configuration
    String zkHost = configuration.getZookeeperHost();
    ZkHostParser zkHostParser = new ZkHostParser();
    zkHostParser.parse(zkHost);
    LOGGER.info("building solr client");
    LOGGER.info("explicitly turning on parallel updates");
    return new CloudSolrClient
        .Builder((List<String>) zkHostParser.getZkHosts(), Optional.of(zkHostParser.getZkRoot()))
        .withParallelUpdates(true)
        .build();
  }

  /**
   * Upload the data found in the data file to the solr cloud.
   *
   * @param configuration contains configuration information
   * @param cloudSolrClient cloud server
   * @return true if the index was a success
   */
  public boolean index(final Configuration configuration, final CloudSolrClient cloudSolrClient) {
    boolean success = true;
    long startTime = System.currentTimeMillis();
    try {
      LOGGER.info(String.format("updating leaders only: %s", (
          cloudSolrClient.isUpdatesToLeaders()
          ? "true" : "false")));

      LOGGER.debug(String.format("setting unique key field name as the solr id: %s", configuration.getUniqueKeyFieldName()));
      cloudSolrClient.setIdField(configuration.getUniqueKeyFieldName());

      LOGGER.debug(String.format("setting collection name on solr server: %s", configuration.getCollectionName()));
      cloudSolrClient.setDefaultCollection(configuration.getCollectionName());

      LOGGER.debug(String.format("setting zookeeper client connection timeout to: %s", configuration.getZookeeperClientConnectionTimeout()));
      cloudSolrClient.setZkClientTimeout(configuration.getZookeeperClientConnectionTimeout());

      LOGGER.debug(String.format("setting zookeeper ensemble connection timeout to: %s", configuration.getZookeeperEnsembleConnectionTimeout()));
      cloudSolrClient.setZkConnectTimeout(configuration.getZookeeperEnsembleConnectionTimeout());

      LOGGER.info("final configuration:");
      configuration.printToStringToLogger(LOGGER);

      LOGGER.info("validate configuration");
      new ConfigurationValidator().validate(configuration);

      final long numDocs = indexDataFile(configuration, cloudSolrClient);
      LOGGER.info("added {} docs [{}]", numDocs, new ToTimeConversion(System.currentTimeMillis() - startTime).getMinutesSecondsMillisecondsFromMilliseconds());

      // optimize/close
      SolrJCloudClientFacet.optimize(cloudSolrClient, configuration.optimizeIndex());
      SolrJCloudClientFacet.commitShutdown(cloudSolrClient);
    }
    catch(Exception e) {
      LOGGER.error("index error", e);
      SolrJCloudClientFacet.shutdown(cloudSolrClient);
      success = false;
    }
    return success;
  }

  /**
   * Call {@link AbstractSolrDocumentHandler#handle(CloudSolrClient)}
   * to index the data found in the data file.
   *
   * @param configuration contains configuration information
   * @param cloudSolrClient cloud server
   *
   * @return number of documents indexed
   *
   * @throws SolrServerException when there is an exception with the cloudSolrClient server
   */
  private long indexDataFile(final Configuration configuration, final CloudSolrClient cloudSolrClient) throws SolrServerException, IOException, InterruptedException, ExecutionException {
    final DocumentHandler solrDocumentHandler = getSolrDocumentHandler(configuration);
    long numDocs;
    final long start = System.currentTimeMillis();
    try {
      LOGGER.info("start: data file processing and indexing");
      numDocs = solrDocumentHandler.handle(cloudSolrClient);
    }
    catch(FileNotFoundException e) {
      LOGGER.error("data file not found", e);
      throw e;
    }
    catch(IOException e) {
      LOGGER.error("problem reading data file", e);
      throw e;
    }
    catch(InterruptedException e) {
      LOGGER.error("futures/threading interrupted", e);
      throw e;
    }
    catch(ExecutionException e) {
      LOGGER.error("execution of futures/threading", e);
      throw e;
    }
    LOGGER.info("completed file parsing and indexing of {} documents in {} batches [{}]", numDocs, solrDocumentHandler.getBatchCount(), new ToTimeConversion(System.currentTimeMillis() - start).getMinutesSecondsMillisecondsFromMilliseconds());
    return numDocs;
  }

  protected DocumentHandler getSolrDocumentHandler(final Configuration configuration) {
    return SolrDocumentHandlerFactory.getInstance().getHandler(configuration);
  }
}
