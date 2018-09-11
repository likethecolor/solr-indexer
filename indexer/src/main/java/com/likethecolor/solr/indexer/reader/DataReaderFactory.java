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

package com.likethecolor.solr.indexer.reader;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldsParser;
import com.likethecolor.solr.indexer.json.JSONParser;
import org.supercsv.prefs.CsvPreference;

import java.util.Map;

public class DataReaderFactory {
  private static final DataReaderFactory INSTANCE = new DataReaderFactory();

  private DataReaderFactory() {
  }

  public static DataReaderFactory getInstance() {
    return INSTANCE;
  }

  public DataReader getDataReader(final Configuration configuration, final CsvPreference csvPreference) {
    if(configuration.getDataType().getName().equalsIgnoreCase(Constants.DATA_TYPE_CSV)) {
      return new CSVDataReader(configuration.getPathToDataFile(), csvPreference);
    }

    if(configuration.getDataType().getName().equalsIgnoreCase(Constants.DATA_TYPE_JSON)) {
      Map<String, FieldDefinition> fieldDefinitionMap = new FieldsParser(configuration).parse(configuration.getFields());
      JSONParser parser = new JSONParser(configuration);
      return new JSONDataReader(configuration.getPathToDataFile(), fieldDefinitionMap, parser);
    }
    return new DoNothingDataReader();
  }
}
