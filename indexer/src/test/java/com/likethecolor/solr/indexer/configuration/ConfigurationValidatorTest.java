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

package com.likethecolor.solr.indexer.configuration;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.ConfigurationValidator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConfigurationValidatorTest {
  private static final String PATH_TO_PROPERTIES_FILE = "/var/foo/file.properties";
  private static final String PATH_TO_DATA_FILE = "/var/foo/file.data";
  private static final String FIELDS = "f0,f1,f2,f3";
  private static final String FIELDS_TO_JSON = "f0=j0,f1=j1,f2=j2,f3=j3";
  
  @Test
  public void testValidate_CSV() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);
    configuration.setFields(FIELDS);

    new ConfigurationValidator().validate(configuration);
    assertTrue(true);
  }
  
  @Test
  public void testValidate_JSON() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_JSON);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);
    configuration.setFieldsToJSON(FIELDS_TO_JSON);

    new ConfigurationValidator().validate(configuration);
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToNullDataFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(null);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToEmptyDataFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile("");

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToEmptyWhiteSpaceDataFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile("\r  \t");

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToNullPropertiesFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(null);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToEmptyPropertiesFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile("");
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludePathToEmptyWhiteSpacePropertiesFile() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile("  \t  ");
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludeNullDataType() {
    Configuration configuration = new Configuration();
    configuration.setDataType(null);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludeEmptyDataType() {
    Configuration configuration = new Configuration();
    configuration.setDataType("");
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_IncludeEmptyWhiteSpaceDataType() {
    Configuration configuration = new Configuration();
    configuration.setDataType("\t  \n«");
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_CSV_FIELDS_MISSING() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidate_JSON_FIELDS_TO_JSON_MISSING() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_JSON);
    configuration.setPathToPropertiesFile(PATH_TO_PROPERTIES_FILE);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    new ConfigurationValidator().validate(configuration);
    assertTrue(true);
  }
}
