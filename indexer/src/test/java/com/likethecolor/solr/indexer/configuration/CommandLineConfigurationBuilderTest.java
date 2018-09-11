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
import com.likethecolor.solr.indexer.configuration.CommandLineConfigurationBuilder;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.IndexerCommandLineOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommandLineConfigurationBuilderTest {
  @Test
  public void testParse() throws ParseException {
    final String batchSizeString = "8675309";
    final Integer batchSize = Integer.parseInt(batchSizeString);
    final String collectionName = "wc-en-collection";
    final char csvDelimiter = '|';
    final String csvDelimiterString = "|";
    final String csvQuoteCharacterString = "+";
    final char csvQuoteCharacter = '+';
    final String fields = "site_id:long;site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String literals = "type:string:site;type_sort:int:3";
    final String multivalueDelimiter = "\u0002";
    final String pathToDataFile = "/tmp/sample.csv";
    final String pathToPropertiesFile = "/tmp/sample.properties";
    final String retryCountString = "4";
    final Integer retryCount = Integer.parseInt(retryCountString);
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";
    final String sleepMillisBetweenRetriesString = "1000";
    final Long sleepMillisBetweenRetries = Long.parseLong(sleepMillisBetweenRetriesString);
    final String uniqueKeyFieldName = "id";
    final String uniqueKeyFieldValue = "site_id;type";
    final String threadCountString = "2";
    final Integer threadCount = Integer.parseInt(threadCountString);
    final String uniqueKeyFieldValueDelimiter = "-";
    final String zookeeperHost = "localhost:10993";
    final String zookeeperClientConnectionTimeoutString = "5000";
    final Integer zookeeperClientConnectionTimeout = Integer.parseInt(zookeeperClientConnectionTimeoutString);
    final String zookeeperEnsembleConnectionTimeoutString = "6000";
    final Integer zookeeperEnsembleConnectionTimeout = Integer.parseInt(zookeeperEnsembleConnectionTimeoutString);

    final String[] args = new String[]{
        "--" + Constants.BATCH_SIZE_OPTION, batchSizeString,
        "--" + Constants.COLLECTION_NAME_OPTION, collectionName,
        "--" + Constants.CSV_DELIMITER_OPTION, csvDelimiterString,
        "--" + Constants.CSV_QUOTE_CHARACTER_OPTION, csvQuoteCharacterString,
        "--" + Constants.FIELDS_OPTION, fields,
        "--" + Constants.FIRST_ROW_IS_HEADER_OPTION,
        "--" + Constants.LITERALS_OPTION, literals,
        "--" + Constants.MULTIVALUE_FIELD_DELIMITER_OPTION, multivalueDelimiter,
        "--" + Constants.OPTIMIZE_INDEX_OPTION,
        "--" + Constants.PATH_TO_DATA_FILE_OPTION, pathToDataFile,
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile,
        "--" + Constants.RETRY_COUNT_OPTION, retryCountString,
        "--" + Constants.SKIP_FIELDS_OPTION, skipFields,
        "--" + Constants.SLEEP_BETWEEN_RETRIES_OPTION, sleepMillisBetweenRetriesString,
        "--" + Constants.THREAD_COUNT_OPTION, threadCountString,
        "--" + Constants.UNIQUE_KEY_FIELD_NAME_OPTION, uniqueKeyFieldName,
        "--" + Constants.UNIQUE_KEY_FIELD_VALUE_OPTION, uniqueKeyFieldValue,
        "--" + Constants.UNIQUE_KEY_FIELD_VALUE_DELIMITER_OPTION, uniqueKeyFieldValueDelimiter,
        "--" + Constants.ZOOKEEPER_HOST_OPTION, zookeeperHost,
        "--" + Constants.ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT_OPTION, zookeeperClientConnectionTimeoutString,
        "--" + Constants.ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT_OPTION, zookeeperEnsembleConnectionTimeoutString
    };

    final CommandLineParser parser = new DefaultParser();
    final CommandLine commandLine = parser.parse(getOptions(), args);

    final Configuration configuration = new Configuration();
    final CommandLineConfigurationBuilder reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    assertEquals(batchSize, configuration.getBatchSize());
    assertEquals(collectionName, configuration.getCollectionName());
    assertEquals(csvDelimiter, configuration.getCsvDelimiter());
    assertEquals(csvQuoteCharacter, configuration.getCsvQuoteCharacter());
    assertEquals(fields, configuration.getFields());
    assertTrue(configuration.firstRowIsHeader());
    assertEquals(literals, configuration.getLiterals());
    assertEquals(multivalueDelimiter, configuration.getMultivalueFieldDelimiter());
    assertTrue(configuration.optimizeIndex());
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

  @Test
  public void testParse_BooleanOptions() throws ParseException {
    final String pathToPropertiesFile = "/tmp/sample.properties";

    // not given - false
    String[] args = new String[]{
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile
    };

    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(getOptions(), args);

    Configuration configuration = new Configuration();
    CommandLineConfigurationBuilder reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    assertFalse(configuration.firstRowIsHeader());
    assertFalse(configuration.optimizeIndex());

    // given - true
    args = new String[]{
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile,
        "--" + Constants.FIRST_ROW_IS_HEADER_OPTION,
        "--" + Constants.OPTIMIZE_INDEX_OPTION
    };

    parser = new DefaultParser();
    commandLine = parser.parse(getOptions(), args);

    configuration = new Configuration();
    reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    assertTrue(configuration.firstRowIsHeader());
    assertTrue(configuration.optimizeIndex());

    // not given - for command line that means that nothing should change
    // set the values here
    args = new String[]{
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile,
        "--" + Constants.FIRST_ROW_IS_HEADER_OPTION,
        "--" + Constants.OPTIMIZE_INDEX_OPTION
    };

    parser = new DefaultParser();
    commandLine = parser.parse(getOptions(), args);

    // set the values on the same configuration object
    configuration = new Configuration();
    reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    args = new String[]{
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile,
        "--" + Constants.OPTIMIZE_INDEX_OPTION
    };

    parser = new DefaultParser();
    commandLine = parser.parse(getOptions(), args);

    reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    assertTrue(configuration.firstRowIsHeader());
    assertTrue(configuration.optimizeIndex());
  }

  @Test
  public void testParse_DoNotSetValueIfOptionIsNotInCommandLine() throws ParseException {
    final String pathToPropertiesFile = "/tmp/sample.properties";
    final char expectedCsvDelimiter = '|';

    // not given - false
    String[] args = new String[]{
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, pathToPropertiesFile
    };

    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(getOptions(), args);

    Configuration configuration = new Configuration();
    configuration.setCsvDelimiter(expectedCsvDelimiter);

    CommandLineConfigurationBuilder reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(commandLine);

    assertEquals(expectedCsvDelimiter, configuration.getCsvDelimiter());
  }

  @Test
  public void testParse_CommandLineIsNull() throws ParseException {
    Configuration configuration = new Configuration();
    Configuration other = new Configuration();
    CommandLineConfigurationBuilder reader = new CommandLineConfigurationBuilder(configuration);
    reader.parse(null);

    assertEquals(configuration.getBatchSize(), other.getBatchSize());
    assertEquals(configuration.getCollectionName(), other.getCollectionName());
    assertEquals(configuration.getCsvDelimiter(), other.getCsvDelimiter());
    assertEquals(configuration.getFields(), other.getFields());
    assertEquals(configuration.firstRowIsHeader(), other.firstRowIsHeader());
    assertEquals(configuration.getLiterals(), other.getLiterals());
    assertEquals(configuration.getMultivalueFieldDelimiter(), other.getMultivalueFieldDelimiter());
    assertEquals(configuration.optimizeIndex(), other.optimizeIndex());
    assertEquals(configuration.getPathToDataFile(), other.getPathToDataFile());
    assertEquals(configuration.getRetryCount(), other.getRetryCount());
    assertEquals(configuration.getSkipFields(), other.getSkipFields());
    assertEquals(configuration.getSleepMillisBetweenRetries(), other.getSleepMillisBetweenRetries());
    assertEquals(configuration.getUniqueKeyFieldName(), other.getUniqueKeyFieldName());
    assertEquals(configuration.getUniqueKeyFieldValue(), other.getUniqueKeyFieldValue());
    assertEquals(configuration.getUniqueKeyFieldValueDelimiter(), other.getUniqueKeyFieldValueDelimiter());
    assertEquals(configuration.getZookeeperHost(), other.getZookeeperHost());
    assertEquals(configuration.getZookeeperClientConnectionTimeout(), other.getZookeeperClientConnectionTimeout());
    assertEquals(configuration.getZookeeperEnsembleConnectionTimeout(), other.getZookeeperEnsembleConnectionTimeout());
  }

  private Options getOptions() {
    IndexerCommandLineOptions indexerCommandLineOptions = new IndexerCommandLineOptions();
    return indexerCommandLineOptions.getOptionDefinitions();
  }
}
