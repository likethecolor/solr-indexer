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
import com.likethecolor.solr.indexer.field.FieldsParser;
import com.likethecolor.solr.indexer.handler.CsvPreferencesBuilder;
import com.likethecolor.solr.indexer.json.JSONParser;
import com.likethecolor.solr.indexer.reader.CSVDataReader;
import com.likethecolor.solr.indexer.reader.DataReader;
import com.likethecolor.solr.indexer.reader.JSONDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.prefs.CsvPreference;

import java.util.Map;

/**
 * This is the factory used to provide the correct document handler.  The handler
 * uses and implementation of {@link DataReader} to read.
 */
public class SolrDocumentHandlerFactory implements Constants {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrDocumentHandlerFactory.class);
  private static final SolrDocumentHandlerFactory INSTANCE = new SolrDocumentHandlerFactory();

  private SolrDocumentHandlerFactory() {
  }

  public static SolrDocumentHandlerFactory getInstance() {
    return INSTANCE;
  }

  public DocumentHandler getHandler(Configuration configuration) {
    DataReader dataReader = null;

    String pathToDataFile = configuration.getPathToDataFile();
    if(configuration.getDataType().getName().equalsIgnoreCase(DATA_TYPE_CSV)) {
      dataReader = new CSVDataReader(pathToDataFile, getCsvPreference(configuration));
      LOGGER.debug("using CSVDataReader using data file: {}", pathToDataFile);
      return new SolrDocumentHandler(configuration, dataReader);
    }
    if(configuration.getDataType().getName().equalsIgnoreCase(DATA_TYPE_JSON)) {
      Map<String, FieldDefinition> fieldDefinitionMap = new FieldsParser(configuration).parse(configuration.getFields());
      dataReader = new JSONDataReader(pathToDataFile, fieldDefinitionMap, new JSONParser(configuration));
      LOGGER.debug("using JSONDataReader using data file: {}", pathToDataFile);
      return new SolrDocumentHandler(configuration, dataReader);
    }

    LOGGER.error("cannot create document handler - returning DoNothing document handler!");
    return new DoNothingSolrDocumentHandler(configuration, dataReader);
  }

  private CsvPreference getCsvPreference(Configuration configuration) {
    return new CsvPreferencesBuilder(configuration).getCsvPreference();
  }
}
