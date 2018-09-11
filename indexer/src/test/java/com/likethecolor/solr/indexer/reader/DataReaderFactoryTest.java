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
import com.likethecolor.solr.indexer.reader.CSVDataReader;
import com.likethecolor.solr.indexer.reader.DataReader;
import com.likethecolor.solr.indexer.reader.DataReaderFactory;
import com.likethecolor.solr.indexer.reader.DoNothingDataReader;
import com.likethecolor.solr.indexer.reader.JSONDataReader;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

import static org.junit.Assert.assertTrue;

public class DataReaderFactoryTest {
  private static final String PATH_TO_DATA_FILE = "/var/foo/file.data";

  @Test
  public void testGetDataReader_CSVReader() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);
    CsvPreference csvPreference = CsvPreference.STANDARD_PREFERENCE;

    DataReader dataReader = DataReaderFactory.getInstance().getDataReader(configuration, csvPreference);

    assertTrue(dataReader.getClass().getSimpleName().equals(CSVDataReader.class.getSimpleName()));
  }

  @Test
  public void testGetDataReader_JSONReader() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_JSON);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    DataReader dataReader = DataReaderFactory.getInstance().getDataReader(configuration, null);

    assertTrue(dataReader.getClass().getSimpleName().equals(JSONDataReader.class.getSimpleName()));
  }

  @Test
  public void testGetDataReader_Unknown() {
    Configuration configuration = new Configuration();
    configuration.setDataType("unknown");
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    DataReader dataReader = DataReaderFactory.getInstance().getDataReader(configuration, null);

    assertTrue(dataReader.getClass().getSimpleName().equals(DoNothingDataReader.class.getSimpleName()));
  }
}
