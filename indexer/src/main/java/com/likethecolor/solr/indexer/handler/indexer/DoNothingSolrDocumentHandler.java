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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.reader.DataReader;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DoNothingSolrDocumentHandler extends AbstractSolrDocumentHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoNothingSolrDocumentHandler.class);

  public DoNothingSolrDocumentHandler(Configuration configuration, final DataReader dataReader) {
    super(configuration, dataReader);
  }

  @Override
  protected CellProcessor[] getProcessors(Map<String, FieldDefinition> fieldDefinitions) {
    return new CellProcessor[0];
  }

  @Override
  protected SolrInputDocument buildSolrInputDocument(List<Object> rowValues) {
    return null;
  }

  @Override
  public long handle(final CloudSolrClient solr) throws SolrServerException, IOException, InterruptedException, ExecutionException {
    LOGGER.error("this is a do nothing handler");
    LOGGER.error("something went wrong");
    LOGGER.error("could it be that there is no data type (--{}) or no path to a data file (--{})",
        Constants.DATA_TYPE_OPTION, Constants.PATH_TO_DATA_FILE_OPTION);
    LOGGER.error("configuration has this as the data type: {}",
        getConfiguration().getDataType());
    LOGGER.error("configuration has this as the path to a data file: {}",
        getConfiguration().getPathToDataFile());
    return 0;
  }
}
