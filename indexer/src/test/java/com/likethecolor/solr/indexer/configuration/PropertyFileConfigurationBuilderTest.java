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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PropertyFileConfigurationBuilderTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileConfigurationBuilderTest.class);
  private static final String REL_PATH = PropertyFileConfigurationBuilderTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
  private static final String TEST_PROPERTIES_FILE = REL_PATH + "/configuration.properties";

  @Test
  public void testParse_PropertiesFile() {
    final Integer batchSize = 8675309;
    final String collectionName = "sample-collection";
    final char csvDelimiter = '|';
    final char csvQuoteCharacter = '+';
    final String fields = "id:long;code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;name:string;url:string;country:string;currency:string;language:string";
    final String literals = "type:string:site;type_sort:int:3";
    final String multivalueDelimiter = "\u0001";
    final String pathToDataFile = "/tmp/sample.csv";
    final Integer retryCount = 5;
    final String skipFields = "create_datetime;update_datetime;country;currency;language";
    final Long sleepMillisBetweenRetries = 6000L;
    final Integer threadCount = 5;
    final String uniqueKeyFieldName = "id";
    final String uniqueKeyFieldValue = "site_id;type";
    final String uniqueKeyFieldValueDelimiter = "-";
    final String zookeeperHost = "localhost:10993";
    final Integer zookeeperClientConnectionTimeout = 5000;
    final Integer zookeeperEnsembleConnectionTimeout = 6000;

    final Configuration configuration = new Configuration();
    final PropertyFileConfigurationBuilder reader = new PropertyFileConfigurationBuilder(configuration);
    reader.parse(TEST_PROPERTIES_FILE);

    assertEquals(batchSize, configuration.getBatchSize());
    assertEquals(collectionName, configuration.getCollectionName());
    assertEquals(csvDelimiter, configuration.getCsvDelimiter());
    assertEquals(csvQuoteCharacter, configuration.getCsvQuoteCharacter());
    assertEquals(fields, configuration.getFields());
    assertTrue(configuration.firstRowIsHeader());
    assertEquals(literals, configuration.getLiterals());
    assertEquals(multivalueDelimiter, configuration.getMultivalueFieldDelimiter());
    assertFalse(configuration.optimizeIndex());
    assertEquals(pathToDataFile, configuration.getPathToDataFile());
    assertEquals(retryCount, configuration.getRetryCount());
    assertEquals(skipFields, configuration.getSkipFields());
    assertEquals(sleepMillisBetweenRetries, configuration.getSleepMillisBetweenRetries());
    assertEquals(Configuration.DEFAULT_SOFT_COMMIT_FREQUENCY, configuration.getSoftCommitFrequency().longValue());
    assertEquals(threadCount, configuration.getThreadCount());
    assertEquals(uniqueKeyFieldName, configuration.getUniqueKeyFieldName());
    assertEquals(uniqueKeyFieldValue, configuration.getUniqueKeyFieldValue());
    assertEquals(uniqueKeyFieldValueDelimiter, configuration.getUniqueKeyFieldValueDelimiter());
    assertEquals(zookeeperHost, configuration.getZookeeperHost());
    assertEquals(zookeeperClientConnectionTimeout, configuration.getZookeeperClientConnectionTimeout());
    assertEquals(zookeeperEnsembleConnectionTimeout, configuration.getZookeeperEnsembleConnectionTimeout());
  }

  @Test
  public void testParse_Properties() {
    final Integer batchSize = 8675309;
    final String collectionName = "sample-collection";
    final char csvDelimiter = '|';
    final char csvQuoteCharacter = '+';
    final String fields = "id:long;code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;name:string;url:string;country:string;currency:string;language:string";
    final String literals = "type:string:site;type_sort:int:3";
    final String multivalueDelimiter = "\u0001";
    final String pathToDataFile = "/tmp/sample.csv";
    final Integer retryCount = 5;
    final String skipFields = "create_datetime;update_datetime;country;currency;language";
    final Long sleepMillisBetweenRetries = 6000L;
    final Integer threadCount = 5;
    final String uniqueKeyFieldName = "id";
    final String uniqueKeyFieldValue = "site_id;type";
    final String uniqueKeyFieldValueDelimiter = "-";
    final String zookeeperHost = "localhost:10993";
    final Integer zookeeperClientConnectionTimeout = 5000;
    final Integer zookeeperEnsembleConnectionTimeout = 6000;

    final Configuration configuration = new Configuration();
    final PropertyFileConfigurationBuilder reader = new PropertyFileConfigurationBuilder(configuration);
    final ConfigProperties properties = ConfigFactory.getInstance().getConfigPropertiesFromAbsolutePath(TEST_PROPERTIES_FILE);

    reader.parse(properties);

    assertEquals(batchSize, configuration.getBatchSize());
    assertEquals(collectionName, configuration.getCollectionName());
    assertEquals(csvDelimiter, configuration.getCsvDelimiter());
    assertEquals(csvQuoteCharacter, configuration.getCsvQuoteCharacter());
    assertEquals(fields, configuration.getFields());
    assertTrue(configuration.firstRowIsHeader());
    assertEquals(literals, configuration.getLiterals());
    assertEquals(multivalueDelimiter, configuration.getMultivalueFieldDelimiter());
    assertFalse(configuration.optimizeIndex());
    assertEquals(pathToDataFile, configuration.getPathToDataFile());
    assertEquals(retryCount, configuration.getRetryCount());
    assertEquals(skipFields, configuration.getSkipFields());
    assertEquals(sleepMillisBetweenRetries, configuration.getSleepMillisBetweenRetries());
    assertEquals(threadCount, configuration.getThreadCount());
    assertEquals(uniqueKeyFieldName, configuration.getUniqueKeyFieldName());
    assertEquals(uniqueKeyFieldValue, configuration.getUniqueKeyFieldValue());
    assertEquals(uniqueKeyFieldValueDelimiter, configuration.getUniqueKeyFieldValueDelimiter());
    assertEquals(zookeeperHost, configuration.getZookeeperHost());
    assertEquals(zookeeperClientConnectionTimeout, configuration.getZookeeperClientConnectionTimeout());
    assertEquals(zookeeperEnsembleConnectionTimeout, configuration.getZookeeperEnsembleConnectionTimeout());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullPath() {
    LOGGER.error("expect an error about configuration being null");
    new PropertyFileConfigurationBuilder(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_NullPath() {
    LOGGER.error("expect an error about path to properties file being null/empty");
    final Configuration configuration = new Configuration();

    final PropertyFileConfigurationBuilder propertyFileConfigurationBuilder = new PropertyFileConfigurationBuilder(configuration);

    propertyFileConfigurationBuilder.parse((String) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_EmptyPath() {
    LOGGER.error("expect an error about path to properties file being null/empty");
    final Configuration configuration = new Configuration();

    final PropertyFileConfigurationBuilder propertyFileConfigurationBuilder = new PropertyFileConfigurationBuilder(configuration);

    propertyFileConfigurationBuilder.parse("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_EmptyWhiteSpacePath() {
    LOGGER.error("expect an error about path to properties file being null/empty");
    final Configuration configuration = new Configuration();

    final PropertyFileConfigurationBuilder propertyFileConfigurationBuilder = new PropertyFileConfigurationBuilder(configuration);

    propertyFileConfigurationBuilder.parse("\t  \r\n ");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_FileDoesNotExist() {
    LOGGER.error("expect an error about properties file not existing");
    final Configuration configuration = new Configuration();

    final PropertyFileConfigurationBuilder propertyFileConfigurationBuilder = new PropertyFileConfigurationBuilder(configuration);

    propertyFileConfigurationBuilder.parse("/foo/bar/bas/23411");
  }
}
