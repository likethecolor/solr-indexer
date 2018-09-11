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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {
  @Test
  public void testInitialState() {
    final Configuration configuration = new Configuration();

    assertEquals(Constants.DEFAULT_BATCH_SIZE, configuration.getBatchSize().intValue());
    assertNull(configuration.getCollectionName());
    assertEquals(Constants.DEFAULT_CSV_DELIMITER, configuration.getCsvDelimiter());
    assertEquals(Constants.DEFAULT_CSV_QUOTE_CHARACTER, configuration.getCsvQuoteCharacter());
    assertNull(configuration.getDynamicFields());
    assertNull(configuration.getFields());
    assertFalse(configuration.firstRowIsHeader());
    assertFalse(configuration.help());
    assertNull(configuration.getFieldsToJSON());
    assertNull(configuration.getLiterals());
    assertEquals(Constants.DEFAULT_MULTIVALUE_FIELD_DELIMITER, configuration.getMultivalueFieldDelimiter());
    assertFalse(configuration.optimizeIndex());
    assertNull(configuration.getPathToDataFile());
    assertNull(configuration.getPathToPropertiesFile());
    assertEquals(Constants.DEFAULT_RETRY_COUNT, configuration.getRetryCount().intValue());
    assertNull(configuration.getSkipFields());
    assertEquals(Constants.DEFAULT_SLEEP_MILLIS_BETWEEN_RETRIES, configuration.getSleepMillisBetweenRetries().longValue());
    assertEquals(Configuration.DEFAULT_SOFT_COMMIT_FREQUENCY, configuration.getSoftCommitFrequency().longValue());
    assertEquals(Constants.DEFAULT_THREAD_COUNT, configuration.getThreadCount().intValue());
    assertEquals(Constants.DEFAULT_UNIQUE_KEY_FIELD_NAME, configuration.getUniqueKeyFieldName());
    assertNull(configuration.getUniqueKeyFieldValue());
    assertEquals(Constants.DEFAULT_UNIQUE_KEY_FIELD_VALUE_DELIMITER, configuration.getUniqueKeyFieldValueDelimiter());
    assertNull(configuration.getZookeeperHost());
    assertEquals(Constants.DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT, configuration.getZookeeperClientConnectionTimeout().intValue());
    assertEquals(Constants.DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT, configuration.getZookeeperEnsembleConnectionTimeout().intValue());
    assertFalse(configuration.hasUniqueKeyFieldValue());
  }

  @Test
  public void testBatchSize() {
    Integer batchSize = 200;
    final Configuration configuration = new Configuration();

    configuration.setBatchSize(batchSize);

    assertEquals(batchSize, configuration.getBatchSize());

    // < 0 will not change the value
    configuration.setBatchSize(batchSize);
    configuration.setBatchSize(0);

    assertEquals(0, configuration.getBatchSize().intValue());

    configuration.setBatchSize(batchSize);
    configuration.setBatchSize(-100);

    assertEquals(-100, configuration.getBatchSize().intValue());
  }

  @Test
  public void testCollectionName() {
    final String collectionName = "wa-en-collection-100";
    final String collectionName2 = "wa-en-collection-200";
    final Configuration configuration = new Configuration();

    configuration.setCollectionName(collectionName);

    assertEquals(collectionName, configuration.getCollectionName());

    // should trim
    configuration.setCollectionName("\t  " + collectionName2 + " \r\n");

    assertEquals(collectionName2, configuration.getCollectionName());

    // null should change value
    configuration.setCollectionName(collectionName);
    configuration.setCollectionName(null);

    assertNull(configuration.getCollectionName());

    // empty string should change value and be trimmed
    configuration.setCollectionName(collectionName);
    configuration.setCollectionName("");

    assertEquals("", configuration.getCollectionName());

    // empty white space string should change value and be trimmed
    configuration.setCollectionName(collectionName);
    configuration.setCollectionName("\t   \r\n  \n");

    assertEquals("", configuration.getCollectionName());
  }

  @Test
  public void testCsvDelimiter() {
    final char csvDelimiter = ',';
    final Configuration configuration = new Configuration();

    configuration.setCsvDelimiter(csvDelimiter);

    assertEquals(csvDelimiter, configuration.getCsvDelimiter());

    // empty string should change value and not be trimmed
    configuration.setCsvDelimiter(csvDelimiter);
    configuration.setCsvDelimiter('\0');

    assertEquals('\0', configuration.getCsvDelimiter());
  }

  @Test
  public void testCsvQuoteCharacter() {
    final char csvQuoteCharacter = '"';
    final Configuration configuration = new Configuration();

    configuration.setCsvQuoteCharacter(csvQuoteCharacter);

    assertEquals(csvQuoteCharacter, configuration.getCsvQuoteCharacter());

    // empty string should change value and not be trimmed
    configuration.setCsvQuoteCharacter(csvQuoteCharacter);
    configuration.setCsvQuoteCharacter('\0');

    assertEquals('\0', configuration.getCsvQuoteCharacter());
  }

  @Test
  public void testDynamicFields() {
    final String dynamicFields = "field0=com.likethecolor.solr.dynamic.MyClass(arg0,arg1)";
    final String dynamicFields2 = "barbar0=com.likethecolor.solr.dynamic.MyClassWithoutArgs()";
    final Configuration configuration = new Configuration();

    configuration.setDynamicFields(dynamicFields);

    String actualFields = configuration.getDynamicFields();

    assertEquals(dynamicFields, actualFields);

    // should trim
    configuration.setDynamicFields("\t  " + dynamicFields2 + " \r\n");

    assertEquals(dynamicFields2, configuration.getDynamicFields());

    // null should change value
    configuration.setDynamicFields(dynamicFields);
    configuration.setDynamicFields(null);

    assertNull(configuration.getDynamicFields());

    // empty string should change value and be trimmed
    configuration.setDynamicFields(dynamicFields);
    configuration.setDynamicFields("");

    assertEquals("", configuration.getDynamicFields());

    // empty white space string should change value and be trimmed
    configuration.setDynamicFields(dynamicFields);
    configuration.setDynamicFields("\t   \r\n  \n");

    assertEquals("", configuration.getDynamicFields());
  }

  @Test
  public void testFields() {
    final String fields = "site_id:int;site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String fields2 = "site_id:int";
    final Configuration configuration = new Configuration();

    configuration.setFields(fields);

    String actualFields = configuration.getFields();

    assertEquals(fields, actualFields);

    // should trim
    configuration.setFields("\t  " + fields2 + " \r\n");

    assertEquals(fields2, configuration.getFields());

    // null should change value
    configuration.setFields(fields);
    configuration.setFields(null);

    assertNull(configuration.getFields());

    // empty string should change value and be trimmed
    configuration.setFields(fields);
    configuration.setFields("");

    assertEquals("", configuration.getFields());

    // empty white space string should change value and be trimmed
    configuration.setFields(fields);
    configuration.setFields("\t   \r\n  \n");

    assertEquals("", configuration.getFields());
  }

  @Test
  public void testFirstRowIsHeader() {
    final Configuration configuration = new Configuration();

    configuration.setFirstRowIsHeader(true);

    assertTrue(configuration.firstRowIsHeader());

    configuration.setFirstRowIsHeader(false);

    assertFalse(configuration.firstRowIsHeader());

    configuration.setFirstRowIsHeader(true);
    configuration.setFirstRowIsHeader(null);

    assertTrue(configuration.firstRowIsHeader());
  }

  @Test
  public void testHelp() {
    final Configuration configuration = new Configuration();

    configuration.setHelp(true);

    assertTrue(configuration.help());

    configuration.setHelp(false);

    assertFalse(configuration.help());

    configuration.setHelp(true);
    configuration.setHelp(null);

    assertFalse(configuration.help());
  }

  @Test
  public void testFieldsToJSON() {
    final String fieldsToJSON = "site_id:SiteID;site_code:SiteCode;create_datetime:CreateDt";
    final String fieldsToJSON2 = "site_id:SiteID";
    final Configuration configuration = new Configuration();

    configuration.setFieldsToJSON(fieldsToJSON);

    String actualFields = configuration.getFieldsToJSON();

    assertEquals(fieldsToJSON, actualFields);

    // should trim
    configuration.setFieldsToJSON("\t  " + fieldsToJSON2 + " \r\n");

    assertEquals(fieldsToJSON2, configuration.getFieldsToJSON());

    // null should change value
    configuration.setFieldsToJSON(fieldsToJSON);
    configuration.setFieldsToJSON(null);

    assertNull(configuration.getFieldsToJSON());

    // empty string should change value and be trimmed
    configuration.setFieldsToJSON(fieldsToJSON);
    configuration.setFieldsToJSON("");

    assertEquals("", configuration.getFieldsToJSON());

    // empty white space string should change value and be trimmed
    configuration.setFieldsToJSON(fieldsToJSON);
    configuration.setFieldsToJSON("\t   \r\n  \n");

    assertEquals("", configuration.getFieldsToJSON());
  }

  @Test
  public void testLiterals() {
    final String literals = "type:string:site;type_sort:int:4";
    final String literals2 = "type_sort:int:4";
    final Configuration configuration = new Configuration();

    configuration.setLiterals(literals);

    assertEquals(literals, configuration.getLiterals());

    // should trim
    configuration.setLiterals("\t  " + literals2 + " \r\n");

    assertEquals(literals2, configuration.getLiterals());

    // null should change value
    configuration.setLiterals(literals);
    configuration.setLiterals(null);

    assertNull(configuration.getLiterals());

    // empty string should change value and be trimmed
    configuration.setLiterals(literals);
    configuration.setLiterals("");

    assertEquals("", configuration.getLiterals());

    // empty white space string should change value and be trimmed
    configuration.setLiterals(literals);
    configuration.setLiterals("\t   \r\n  \n");

    assertEquals("", configuration.getLiterals());
  }

  @Test
  public void testMultivalueFieldMultivalueFieldDelimiter() {
    final String multivalueFieldDelimiter = ",";
    final String multivalueFieldDelimiter2 = "\t";
    final Configuration configuration = new Configuration();

    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiter);

    assertEquals(multivalueFieldDelimiter, configuration.getMultivalueFieldDelimiter());

    // should not trim
    String multivalueFieldDelimiterWithWhiteSpace = "\t  " + multivalueFieldDelimiter2 + " \r\n";
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiterWithWhiteSpace);

    assertEquals(multivalueFieldDelimiterWithWhiteSpace, configuration.getMultivalueFieldDelimiter());

    // null should change value
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiter);
    configuration.setMultivalueFieldDelimiter(null);

    assertNull(configuration.getMultivalueFieldDelimiter());

    // empty string should change value and not be trimmed
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiter);
    configuration.setMultivalueFieldDelimiter("");

    assertEquals("", configuration.getMultivalueFieldDelimiter());

    // empty white space string should change value and not be trimmed
    multivalueFieldDelimiterWithWhiteSpace = "\t   \r\n  \n";
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiter);
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiterWithWhiteSpace);

    assertEquals(multivalueFieldDelimiterWithWhiteSpace, configuration.getMultivalueFieldDelimiter());
  }

  @Test
  public void testOptimizeIndex() {
    final Configuration configuration = new Configuration();

    configuration.setOptimizeIndex(true);

    assertTrue(configuration.optimizeIndex());

    configuration.setOptimizeIndex(false);

    assertFalse(configuration.optimizeIndex());

    configuration.setOptimizeIndex(true);
    configuration.setOptimizeIndex(null);

    assertFalse(configuration.optimizeIndex());
  }

  @Test
  public void testPathToDataFile() {
    final String pathToDataFile = "/path/to/data/file.csv";
    final String pathToDataFile2 = "/path/to/data/another/file.csv";
    final Configuration configuration = new Configuration();

    configuration.setPathToDataFile(pathToDataFile);

    assertEquals(pathToDataFile, configuration.getPathToDataFile());

    // should trim
    configuration.setPathToDataFile("\t  " + pathToDataFile2 + " \r\n");

    assertEquals(pathToDataFile2, configuration.getPathToDataFile());

    // null should change value
    configuration.setPathToDataFile(pathToDataFile);
    configuration.setPathToDataFile(null);

    assertNull(configuration.getPathToDataFile());

    // empty string should change value and be trimmed
    configuration.setPathToDataFile(pathToDataFile);
    configuration.setPathToDataFile("");

    assertEquals("", configuration.getPathToDataFile());

    // empty white space string should change value and be trimmed
    configuration.setPathToDataFile(pathToDataFile);
    configuration.setPathToDataFile("\t   \r\n  \n");

    assertEquals("", configuration.getPathToDataFile());
  }

  @Test
  public void testPropertiesFile() {
    final String propertyFile = "file.properties";
    final String propertyFile2 = "file-foo.properties";
    final Configuration configuration = new Configuration();

    configuration.setPathToPropertiesFile(propertyFile);

    assertEquals(propertyFile, configuration.getPathToPropertiesFile());

    // should trim
    configuration.setPathToPropertiesFile("\t  " + propertyFile2 + " \r\n");

    assertEquals(propertyFile2, configuration.getPathToPropertiesFile());

    // null should change value
    configuration.setPathToPropertiesFile(propertyFile);
    configuration.setPathToPropertiesFile(null);

    assertNull(configuration.getPathToPropertiesFile());

    // empty string should change value and be trimmed
    configuration.setPathToPropertiesFile(propertyFile);
    configuration.setPathToPropertiesFile("");

    assertEquals("", configuration.getPathToPropertiesFile());

    // empty white space string should change value and be trimmed
    configuration.setPathToPropertiesFile(propertyFile);
    configuration.setPathToPropertiesFile("\t   \r\n  \n");

    assertEquals("", configuration.getPathToPropertiesFile());
  }

  @Test
  public void testRetryCount() {
    Integer retryCount = 10;
    final Configuration configuration = new Configuration();

    configuration.setRetryCount(retryCount);

    assertEquals(retryCount, configuration.getRetryCount());

    // null || < 0 will set the value to default
    configuration.setRetryCount(retryCount);
    configuration.setRetryCount(0);

    assertEquals(0, configuration.getRetryCount().intValue());

    configuration.setRetryCount(retryCount);
    configuration.setRetryCount(null);

    assertEquals(Constants.DEFAULT_RETRY_COUNT, configuration.getRetryCount().intValue());

    configuration.setRetryCount(retryCount);
    configuration.setRetryCount(-100);

    assertEquals(Constants.DEFAULT_RETRY_COUNT, configuration.getRetryCount().intValue());
  }

  @Test
  public void testSkipFields() {
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";
    final String skipFields2 = "create_datetime;mobile";
    final Configuration configuration = new Configuration();

    configuration.setSkipFields(skipFields);

    assertEquals(skipFields, configuration.getSkipFields());

    // should trim
    configuration.setSkipFields("\t  " + skipFields2 + " \r\n");

    assertEquals(skipFields2, configuration.getSkipFields());

    // null should change value
    configuration.setSkipFields(skipFields);
    configuration.setSkipFields(null);

    assertNull(configuration.getSkipFields());

    // empty string should change value and be trimmed
    configuration.setSkipFields(skipFields);
    configuration.setSkipFields("");

    assertEquals("", configuration.getSkipFields());

    // empty white space string should change value and be trimmed
    configuration.setSkipFields(skipFields);
    configuration.setSkipFields("\t   \r\n  \n");

    assertEquals("", configuration.getSkipFields());
  }

  @Test
  public void testSleepMillisBetweenRetries() {
    Long sleepMillisBetweenRetries = 5000L;
    final Configuration configuration = new Configuration();

    configuration.setSleepMillisBetweenRetries(sleepMillisBetweenRetries);

    assertEquals(sleepMillisBetweenRetries, configuration.getSleepMillisBetweenRetries());

    // 0 will set the value to 0
    configuration.setSleepMillisBetweenRetries(sleepMillisBetweenRetries);
    configuration.setSleepMillisBetweenRetries(0L);

    assertEquals(0L, configuration.getSleepMillisBetweenRetries().intValue());

    // null || < 0 will set the value to 0
    configuration.setSleepMillisBetweenRetries(sleepMillisBetweenRetries);
    configuration.setSleepMillisBetweenRetries(null);

    assertEquals(0L, configuration.getSleepMillisBetweenRetries().intValue());

    configuration.setSleepMillisBetweenRetries(sleepMillisBetweenRetries);
    configuration.setSleepMillisBetweenRetries(-100L);

    assertEquals(0L, configuration.getSleepMillisBetweenRetries().intValue());
  }

  @Test
  public void testSoftCommitAfterEachBatch() {
    final long softCommitFrequency = 10;
    final Configuration configuration = new Configuration();

    assertEquals(Constants.DEFAULT_SOFT_COMMIT_FREQUENCY, configuration.getSoftCommitFrequency().longValue());

    configuration.setSoftCommitFrequency(softCommitFrequency);

    assertEquals(softCommitFrequency, configuration.getSoftCommitFrequency().longValue());
  }

  @Test
  public void testThreadCount() {
    Integer threadCount = 10;
    final Configuration configuration = new Configuration();

    configuration.setThreadCount(threadCount);

    assertEquals(threadCount, configuration.getThreadCount());

    // null || <= 0 will set the value to 1
    configuration.setThreadCount(threadCount);
    configuration.setThreadCount(0);

    assertEquals(1, configuration.getThreadCount().intValue());

    configuration.setThreadCount(threadCount);
    configuration.setThreadCount(null);

    assertEquals(1, configuration.getThreadCount().intValue());

    configuration.setThreadCount(threadCount);
    configuration.setThreadCount(-100);

    assertEquals(1, configuration.getThreadCount().intValue());
  }

  @Test
  public void testUniqueKeyFieldName() {
    final String uniqueKeyFieldName = "id";
    final String uniqueKeyFieldName2 = "q_qid";
    final Configuration configuration = new Configuration();

    configuration.setUniqueKeyFieldName(uniqueKeyFieldName);

    assertEquals(uniqueKeyFieldName, configuration.getUniqueKeyFieldName());

    // should trim
    configuration.setUniqueKeyFieldName("\t  " + uniqueKeyFieldName2 + " \r\n");

    assertEquals(uniqueKeyFieldName2, configuration.getUniqueKeyFieldName());

    // null should change value
    configuration.setUniqueKeyFieldName(uniqueKeyFieldName);
    configuration.setUniqueKeyFieldName(null);

    assertNull(configuration.getUniqueKeyFieldName());

    // empty string should change value and be trimmed
    configuration.setUniqueKeyFieldName(uniqueKeyFieldName);
    configuration.setUniqueKeyFieldName("");

    assertEquals("", configuration.getUniqueKeyFieldName());

    // empty white space string should change value and be trimmed
    configuration.setUniqueKeyFieldName(uniqueKeyFieldName);
    configuration.setUniqueKeyFieldName("\t   \r\n  \n");

    assertEquals("", configuration.getUniqueKeyFieldName());
  }

  @Test
  public void testUniqueKeyFieldValue() {
    final String uniqueKeyFieldValue = "id;foo;bar";
    final String uniqueKeyFieldValue2 = "q_qid;baz";
    final Configuration configuration = new Configuration();

    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);

    assertEquals(uniqueKeyFieldValue, configuration.getUniqueKeyFieldValue());

    // should trim
    configuration.setUniqueKeyFieldValue("\t  " + uniqueKeyFieldValue2 + " \r\n");

    assertEquals(uniqueKeyFieldValue2, configuration.getUniqueKeyFieldValue());

    // null should change value
    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue(null);

    assertNull(configuration.getUniqueKeyFieldValue());

    // empty string should change value and be trimmed
    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue("");

    assertEquals("", configuration.getUniqueKeyFieldValue());

    // empty white space string should change value and be trimmed
    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue("\t   \r\n  \n");

    assertEquals("", configuration.getUniqueKeyFieldValue());
  }

  @Test
  public void testUniqueKeyFieldValueDelimiter() {
    final String uniqueKeyFieldValueDelimiter = "-";
    final String uniqueKeyFieldValueDelimiter2 = "::";
    final Configuration configuration = new Configuration();

    configuration.setUniqueKeyFieldValueDelimiter(uniqueKeyFieldValueDelimiter);

    assertEquals(uniqueKeyFieldValueDelimiter, configuration.getUniqueKeyFieldValueDelimiter());

    // should not trim
    configuration.setUniqueKeyFieldValueDelimiter("\t  " + uniqueKeyFieldValueDelimiter2 + " \r\n");

    assertEquals("\t  " + uniqueKeyFieldValueDelimiter2 + " \r\n", configuration.getUniqueKeyFieldValueDelimiter());

    // null should change value
    configuration.setUniqueKeyFieldValueDelimiter(uniqueKeyFieldValueDelimiter);
    configuration.setUniqueKeyFieldValueDelimiter(null);

    assertNull(configuration.getUniqueKeyFieldValueDelimiter());

    // empty string should change value
    configuration.setUniqueKeyFieldValueDelimiter(uniqueKeyFieldValueDelimiter);
    configuration.setUniqueKeyFieldValueDelimiter("");

    assertEquals("", configuration.getUniqueKeyFieldValueDelimiter());

    // empty white space string should change value and not be trimmed
    configuration.setUniqueKeyFieldValueDelimiter(uniqueKeyFieldValueDelimiter);
    configuration.setUniqueKeyFieldValueDelimiter("\t   \r\n  \n");

    assertEquals("\t   \r\n  \n", configuration.getUniqueKeyFieldValueDelimiter());
  }

  @Test
  public void testHasUniqueKeyFieldValue() {
    final String uniqueKeyFieldValue = "id;foo;bar";
    final Configuration configuration = new Configuration();

    assertFalse(configuration.hasUniqueKeyFieldValue());

    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);

    assertTrue(configuration.hasUniqueKeyFieldValue());

    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue(null);

    assertFalse(configuration.hasUniqueKeyFieldValue());

    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue("");

    assertFalse(configuration.hasUniqueKeyFieldValue());

    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setUniqueKeyFieldValue("\t   \n\n");

    assertFalse(configuration.hasUniqueKeyFieldValue());
  }

  @Test
  public void testZookeeper() {
    final Configuration configuration = new Configuration();
    final String expectedZookeeper = "host0:0000,host1:1111,host2:2222,host3:3333";

    configuration.setZookeeperHost(expectedZookeeper);

    String actualZookeeper = configuration.getZookeeperHost();

    assertEquals(expectedZookeeper, actualZookeeper);

    // null should change value
    configuration.setZookeeperHost(expectedZookeeper);
    configuration.setZookeeperHost(null);

    actualZookeeper = configuration.getZookeeperHost();

    assertNull(actualZookeeper);

    // space should change value
    configuration.setZookeeperHost(expectedZookeeper);
    configuration.setZookeeperHost("");

    actualZookeeper = configuration.getZookeeperHost();

    assertEquals("", actualZookeeper);

    // empty white space should change value and trim
    configuration.setZookeeperHost(expectedZookeeper);
    configuration.setZookeeperHost("\t  \r\n  ");

    actualZookeeper = configuration.getZookeeperHost();

    assertEquals("", actualZookeeper);
  }

  @Test
  public void testZookeeperClientConnectTimeout() {
    Integer zookeeperClientConnectTimeout = 200;
    final Configuration configuration = new Configuration();

    configuration.setZookeeperClientConnectTimeout(zookeeperClientConnectTimeout);

    assertEquals(zookeeperClientConnectTimeout, configuration.getZookeeperClientConnectionTimeout());

    // < 0 set the value to the default
    configuration.setZookeeperClientConnectTimeout(zookeeperClientConnectTimeout);
    configuration.setZookeeperClientConnectTimeout(0);

    assertEquals(0, configuration.getZookeeperClientConnectionTimeout().intValue());

    configuration.setZookeeperClientConnectTimeout(zookeeperClientConnectTimeout);
    configuration.setZookeeperClientConnectTimeout(-100);

    assertEquals(Constants.DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT, configuration.getZookeeperClientConnectionTimeout().intValue());
  }

  @Test
  public void testZookeeperEnsembleConnectTimeout() {
    Integer zookeeperEnsembleConnectTimeout = 200;
    final Configuration configuration = new Configuration();

    configuration.setZookeeperEnsembleConnectTimeout(zookeeperEnsembleConnectTimeout);

    assertEquals(zookeeperEnsembleConnectTimeout, configuration.getZookeeperEnsembleConnectionTimeout());

    // 0 will change the value to 0
    configuration.setZookeeperEnsembleConnectTimeout(0);

    assertEquals(0, configuration.getZookeeperEnsembleConnectionTimeout().intValue());

    // < 0 will change the value to default
    configuration.setZookeeperEnsembleConnectTimeout(-100);

    assertEquals(Constants.DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT, configuration.getZookeeperEnsembleConnectionTimeout().intValue());
  }

  @Test
  public void testToString() {
    final Configuration configuration = new Configuration();
    final Integer batchSize = 2000;
    final String collectionName = "wa-en-collection-100";
    final char csvDelimiter = ',';
    final char csvQuoteCharacter = '"';
    String dataType = Constants.DATA_TYPE_CSV;
    final String dynamicFields = "foo=com.likethecolor.solr.dynamic.SampleDynamicClass(arg0,arg0)";
    final String fields = "site_id:int;site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String fieldsToJson = "site_id:SiteID;site_code:SiteCode";
    final String literals = "sort_type:int:3;type:string:site";
    final String multivalueFieldDelimiter = "\u0002";
    final String pathToDataFile = "/path/to/file.csv";
    final String propertyFile = "file.properties";
    final Integer retryCount = 5;
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";
    final Long sleepMillisBetweenRetries = 1000L;
    final long softCommitFrequency = 200;
    final String zookeeper = "host0:0000,host1:1111,host2:2222,host3:3333";
    final Integer threadCount = 20;
    final String uniqueKeyFieldName = "id";
    final String uniqueKeyFieldValue = "type;site_id;site_code;site_name";
    final String uniqueKeyFieldValueDelimiter = "-";
    final Integer zookeeperClientConnectTimeout = 250;
    final Integer zookeeperEnsembleConnectTimeout = 200;

    final String expectedString = new StringBuilder()
        .append("batch size: ").append(batchSize)
        .append("; collection name: ").append(collectionName)
        .append("; csv delimiter: ").append(csvDelimiter)
        .append("; csv quote character: ").append(csvQuoteCharacter)
        .append("; data type: ").append(dataType)
        .append("; dynamic fields: ").append(dynamicFields)
        .append("; fields: ").append(fields)
        .append("; fields to json: ").append(fieldsToJson)
        .append("; first row is header: true")
        .append("; help: false")
        .append("; literals: ").append(literals)
        .append("; multi-value field delimiter: ").append(multivalueFieldDelimiter)
        .append("; optimize index: true")
        .append("; path to data file: ").append(pathToDataFile)
        .append("; property file: ").append(propertyFile)
        .append("; retry count: ").append(retryCount)
        .append("; skip fields: ").append(skipFields)
        .append("; sleep millis between retries: ").append(sleepMillisBetweenRetries)
        .append("; soft commit frequency: ").append(softCommitFrequency)
        .append("; thread count: ").append(threadCount)
        .append("; unique key field name: ").append(uniqueKeyFieldName)
        .append("; unique key field value: ").append(uniqueKeyFieldValue)
        .append("; unique key field value delimiter: ").append(uniqueKeyFieldValueDelimiter)
        .append("; zookeeper client connect timeout: ").append(zookeeperClientConnectTimeout)
        .append("; zookeeper ensemble connect timeout: ").append(zookeeperEnsembleConnectTimeout)
        .append("; zookeeper host: ").append(zookeeper)
        .toString();
    
    configuration.setBatchSize(batchSize);
    configuration.setCollectionName(collectionName);
    configuration.setCsvDelimiter(csvDelimiter);
    configuration.setCsvQuoteCharacter(csvQuoteCharacter);
    configuration.setDataType(dataType);
    configuration.setDynamicFields(dynamicFields);
    configuration.setFields(fields);
    configuration.setFieldsToJSON(fieldsToJson);
    configuration.setFirstRowIsHeader(true);
    configuration.setLiterals(literals);
    configuration.setMultivalueFieldDelimiter(multivalueFieldDelimiter);
    configuration.setOptimizeIndex(true);
    configuration.setPathToDataFile(pathToDataFile);
    configuration.setPathToPropertiesFile(propertyFile);
    configuration.setSleepMillisBetweenRetries(sleepMillisBetweenRetries);
    configuration.setSkipFields(skipFields);
    configuration.setSoftCommitFrequency(softCommitFrequency);
    configuration.setRetryCount(retryCount);
    configuration.setThreadCount(threadCount);
    configuration.setUniqueKeyFieldName(uniqueKeyFieldName);
    configuration.setUniqueKeyFieldValueDelimiter(uniqueKeyFieldValueDelimiter);
    configuration.setUniqueKeyFieldValue(uniqueKeyFieldValue);
    configuration.setZookeeperHost(zookeeper);
    configuration.setZookeeperClientConnectTimeout(zookeeperClientConnectTimeout);
    configuration.setZookeeperEnsembleConnectTimeout(zookeeperEnsembleConnectTimeout);

    assertEquals(expectedString, configuration.toString());
  }
}
