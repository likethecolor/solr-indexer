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
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the properties file and the command line flags.
 */
public class Configuration implements Constants {
  private Integer batchSize;
  private String collectionName;
  private Character csvDelimiter;
  private Character csvQuoteCharacter;
  private DataTypeEnum dataType;
  private String dynamicFields;
  private String fields;
  private String fieldsToJson;
  private Boolean firstRowIsHeader;
  private Boolean help;
  private String literals;
  private String multivalueFieldDelimiter;
  private Boolean optimizeIndex;
  private String pathToDataFile;
  private String pathToPropertiesFile;
  private Integer retryCount;
  private String skipFields;
  private Long sleepMillisBetweenRetries;
  private Long softCommitFrequency;
  private Integer threadCount;
  private String uniqueKeyFieldName;
  private String uniqueKeyFieldValue;
  private String uniqueKeyFieldValueDelimiter;
  private Integer zookeeperClientConnectTimeout;
  private Integer zookeeperEnsembleConnectTimeout;
  private String zookeeperHost;

  public Configuration() {
    batchSize = DEFAULT_BATCH_SIZE;
    dynamicFields = null;
    collectionName = null;
    csvDelimiter = DEFAULT_CSV_DELIMITER;
    csvQuoteCharacter = DEFAULT_CSV_QUOTE_CHARACTER;
    dataType = DataTypeEnum.DEFAULT;
    dynamicFields = null;
    fields = null;
    fieldsToJson = null;
    firstRowIsHeader = false;
    help = false;
    literals = null;
    multivalueFieldDelimiter = DEFAULT_MULTIVALUE_FIELD_DELIMITER;
    optimizeIndex = false;
    pathToDataFile = null;
    pathToPropertiesFile = null;
    retryCount = DEFAULT_RETRY_COUNT;
    skipFields = null;
    sleepMillisBetweenRetries = DEFAULT_SLEEP_MILLIS_BETWEEN_RETRIES;
    softCommitFrequency = 0L; // 0 = no soft commit, >= 1 soft commit after every x batches
    threadCount = DEFAULT_THREAD_COUNT;
    uniqueKeyFieldName = DEFAULT_UNIQUE_KEY_FIELD_NAME;
    uniqueKeyFieldValue = null;
    uniqueKeyFieldValueDelimiter = DEFAULT_UNIQUE_KEY_FIELD_VALUE_DELIMITER;
    zookeeperClientConnectTimeout = DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT;
    zookeeperEnsembleConnectTimeout = DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT;
    zookeeperHost = null;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  @ConfigurationValues(optionName = BATCH_SIZE_OPTION,
      defaultValueInteger = DEFAULT_BATCH_SIZE)
  public void setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
  }

  public String getCollectionName() {
    return collectionName;
  }

  @ConfigurationValues(optionName = COLLECTION_NAME_OPTION)
  public void setCollectionName(String collectionName) {
    if(collectionName != null) {
      collectionName = collectionName.trim();
    }
    this.collectionName = collectionName;
  }

  public char getCsvDelimiter() {
    return csvDelimiter;
  }

  @ConfigurationValues(optionName = CSV_DELIMITER_OPTION,
      defaultValueCharacter = DEFAULT_CSV_DELIMITER)
  public void setCsvDelimiter(final char csvDelimiter) {
    // no trim as the csvDelimiter could be a tab or other such character
    // that trim would delete
    this.csvDelimiter = csvDelimiter;
  }

  public char getCsvQuoteCharacter() {
    return csvQuoteCharacter;
  }

  @ConfigurationValues(optionName = CSV_QUOTE_CHARACTER_OPTION,
      defaultValueCharacter = DEFAULT_CSV_QUOTE_CHARACTER)
  public void setCsvQuoteCharacter(final char csvQuoteCharacter) {
    // no trim as the csvDelimiter could be a tab or other such character
    // that trim would delete
    this.csvQuoteCharacter = csvQuoteCharacter;
  }

  public DataTypeEnum getDataType() {
    return dataType;
  }

  @ConfigurationValues(optionName = DATA_TYPE_OPTION,
      defaultValue = DEFAULT_DATA_TYPE)
  public void setDataType(String dataType) {
    this.dataType = DataTypeEnum.get(dataType);
  }
  
  public String getDynamicFields() {
    return dynamicFields;
  }

  @ConfigurationValues(optionName = DYNAMIC_OPTION)
  public void setDynamicFields(String dynamicFields) {
    if(dynamicFields != null) {
      dynamicFields = dynamicFields.trim();
    }
    this.dynamicFields = dynamicFields;
  }

  public String getFields() {
    return fields;
  }

  @ConfigurationValues(optionName = FIELDS_OPTION)
  public void setFields(String fields) {
    if(fields != null) {
      fields = fields.trim();
    }
    this.fields = fields;
  }

  public String getFieldsToJSON() {
    return fieldsToJson;
  }

  @ConfigurationValues(optionName = FIELDS_TO_JSON_OPTION)
  public void setFieldsToJSON(String json2Fields) {
    if(json2Fields != null) {
      json2Fields = json2Fields.trim();
    }
    this.fieldsToJson = json2Fields;
  }

  @ConfigurationValues(optionName = FIRST_ROW_IS_HEADER_OPTION,
      defaultValueBoolean = true)
  public void setFirstRowIsHeader(Boolean firstRowIsHeader) {
    if(firstRowIsHeader == null) {
      firstRowIsHeader = true;
    }
    this.firstRowIsHeader = firstRowIsHeader;
  }

  public Boolean firstRowIsHeader() {
    return firstRowIsHeader;
  }

  @ConfigurationValues(optionName = HELP_OPTION)
  public void setHelp(Boolean help) {
    if(help == null) {
      help = false;
    }
    this.help = help;
  }

  public Boolean help() {
    return help;
  }

  public String getLiterals() {
    return literals;
  }

  @ConfigurationValues(optionName = LITERALS_OPTION)
  public void setLiterals(String literals) {
    if(literals != null) {
      literals = literals.trim();
    }
    this.literals = literals;
  }

  public String getMultivalueFieldDelimiter() {
    return multivalueFieldDelimiter;
  }

  @ConfigurationValues(optionName = MULTIVALUE_FIELD_DELIMITER_OPTION,
      defaultValue = DEFAULT_MULTIVALUE_FIELD_DELIMITER)
  public void setMultivalueFieldDelimiter(String multivalueFieldDelimiter) {
    // no trim as the delimiter could be a tab or other such character
    // that trim would delete
    this.multivalueFieldDelimiter = multivalueFieldDelimiter;
  }

  @ConfigurationValues(optionName = OPTIMIZE_INDEX_OPTION,
      defaultValueBoolean = false)
  public void setOptimizeIndex(Boolean optimizeIndex) {
    if(optimizeIndex == null) {
      optimizeIndex = false;
    }
    this.optimizeIndex = optimizeIndex;
  }

  public Boolean optimizeIndex() {
    return optimizeIndex;
  }

  public String getPathToDataFile() {
    return pathToDataFile;
  }

  @ConfigurationValues(optionName = PATH_TO_DATA_FILE_OPTION)
  public void setPathToDataFile(String pathToDataFile) {
    if(pathToDataFile != null) {
      pathToDataFile = pathToDataFile.trim();
    }
    this.pathToDataFile = pathToDataFile;
  }

  public String getPathToPropertiesFile() {
    return pathToPropertiesFile;
  }

  @ConfigurationValues(optionName = PATH_TO_PROPERTIES_FILE_OPTION)
  public void setPathToPropertiesFile(String pathToPropertiesFile) {
    if(pathToPropertiesFile != null) {
      pathToPropertiesFile = pathToPropertiesFile.trim();
    }
    this.pathToPropertiesFile = pathToPropertiesFile;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  @ConfigurationValues(optionName = RETRY_COUNT_OPTION,
      defaultValueInteger = DEFAULT_RETRY_COUNT)
  public void setRetryCount(Integer retryCount) {
    if(retryCount == null || retryCount < 0) {
      retryCount = DEFAULT_RETRY_COUNT;
    }
    this.retryCount = retryCount;
  }

  public String getSkipFields() {
    return skipFields;
  }

  @ConfigurationValues(optionName = SKIP_FIELDS_OPTION)
  public void setSkipFields(String skipFields) {
    if(skipFields != null) {
      skipFields = skipFields.trim();
    }
    this.skipFields = skipFields;
  }

  public Long getSleepMillisBetweenRetries() {
    return sleepMillisBetweenRetries;
  }

  @ConfigurationValues(optionName = SLEEP_BETWEEN_RETRIES_OPTION,
      defaultValueLong = DEFAULT_SLEEP_MILLIS_BETWEEN_RETRIES)
  public void setSleepMillisBetweenRetries(Long sleepMillisBetweenRetries) {
    if(sleepMillisBetweenRetries == null || sleepMillisBetweenRetries < 0) {
      sleepMillisBetweenRetries = 0L;
    }
    this.sleepMillisBetweenRetries = sleepMillisBetweenRetries;
  }

  public Long getSoftCommitFrequency() {
    return softCommitFrequency;
  }

  @ConfigurationValues(optionName = SOFT_COMMIT_FREQUENCY,
      defaultValueLong = DEFAULT_SOFT_COMMIT_FREQUENCY)
  public void setSoftCommitFrequency(Long softCommitFrequency) {
    if(softCommitFrequency == null) {
      softCommitFrequency = DEFAULT_SOFT_COMMIT_FREQUENCY;
    }
    this.softCommitFrequency = softCommitFrequency;
  }

  public Integer getThreadCount() {
    return threadCount;
  }

  @ConfigurationValues(optionName = THREAD_COUNT_OPTION,
      defaultValueInteger = DEFAULT_THREAD_COUNT)
  public void setThreadCount(Integer threadCount) {
    if(threadCount == null || threadCount <= 0) {
      threadCount = 1;
    }
    this.threadCount = threadCount;
  }

  public String getUniqueKeyFieldName() {
    return uniqueKeyFieldName;
  }

  @ConfigurationValues(optionName = UNIQUE_KEY_FIELD_NAME_OPTION,
      defaultValue = DEFAULT_UNIQUE_KEY_FIELD_NAME)
  public void setUniqueKeyFieldName(String uniqueKeyFieldName) {
    if(uniqueKeyFieldName != null) {
      uniqueKeyFieldName = uniqueKeyFieldName.trim();
    }
    this.uniqueKeyFieldName = uniqueKeyFieldName;
  }

  public String getUniqueKeyFieldValue() {
    return uniqueKeyFieldValue;
  }

  @ConfigurationValues(optionName = UNIQUE_KEY_FIELD_VALUE_OPTION)
  public void setUniqueKeyFieldValue(String uniqueKeyFieldValue) {
    if(uniqueKeyFieldValue != null) {
      uniqueKeyFieldValue = uniqueKeyFieldValue.trim();
    }
    this.uniqueKeyFieldValue = uniqueKeyFieldValue;
  }

  public boolean hasUniqueKeyFieldValue() {
    return uniqueKeyFieldValue != null && uniqueKeyFieldValue.trim().length() > 0;
  }

  public String getUniqueKeyFieldValueDelimiter() {
    return uniqueKeyFieldValueDelimiter;
  }

  @ConfigurationValues(optionName = UNIQUE_KEY_FIELD_VALUE_DELIMITER_OPTION)
  public void setUniqueKeyFieldValueDelimiter(String uniqueKeyFieldValueDelimiter) {
    this.uniqueKeyFieldValueDelimiter = uniqueKeyFieldValueDelimiter;
  }

  @ConfigurationValues(optionName = ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT_OPTION,
      defaultValueInteger = DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT)
  public void setZookeeperClientConnectTimeout(Integer zookeeperClientConnectTimeout) {
    if(zookeeperClientConnectTimeout < 0) {
      zookeeperClientConnectTimeout = DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT;
    }
    this.zookeeperClientConnectTimeout = zookeeperClientConnectTimeout;
  }

  public Integer getZookeeperClientConnectionTimeout() {
    return this.zookeeperClientConnectTimeout;
  }

  @ConfigurationValues(optionName = ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT_OPTION,
      defaultValueInteger = DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT)
  public void setZookeeperEnsembleConnectTimeout(Integer zookeeperEnsembleConnectTimeout) {
    if(zookeeperEnsembleConnectTimeout < 0) {
      zookeeperEnsembleConnectTimeout = DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT;
    }
    this.zookeeperEnsembleConnectTimeout = zookeeperEnsembleConnectTimeout;
  }

  public Integer getZookeeperEnsembleConnectionTimeout() {
    return this.zookeeperEnsembleConnectTimeout;
  }

  public String getZookeeperHost() {
    return zookeeperHost;
  }

  @ConfigurationValues(optionName = ZOOKEEPER_HOST_OPTION)
  public void setZookeeperHost(String zookeeperHost) {
    if(zookeeperHost != null) {
      zookeeperHost = zookeeperHost.trim();
    }
    this.zookeeperHost = zookeeperHost;
  }

  public String toString() {
    final StringBuilder builder = new StringBuilder();
    final List<String> toString = toStringList();
    for(String str : toString) {
      if(builder.length() > 0) {
        builder.append("; ");
      }
      builder.append(str);
    }
    return builder.toString();
  }

  public void printToStringToLogger(final Logger logger) {
    final List<String> toString = toStringList();
    for(String str : toString) {
      logger.info(str);
    }
  }

  private List<String> toStringList() {
    final List<String> toString = new ArrayList<>();
    toString.add("batch size: " + getBatchSize());
    toString.add("collection name: " + getCollectionName());
    toString.add("csv delimiter: " + getCsvDelimiter());
    toString.add("csv quote character: " + getCsvQuoteCharacter());
    toString.add("data type: " + getDataType().getName());
    toString.add("dynamic fields: " + getDynamicFields());
    toString.add("fields: " + getFields());
    toString.add("fields to json: " + getFieldsToJSON());
    toString.add("first row is header: " + (firstRowIsHeader() ? "true"
                                                               : "false"));
    toString.add("help: " + (help() ? "true" : "false"));
    toString.add("literals: " + getLiterals());
    toString.add("multi-value field delimiter: " + getMultivalueFieldDelimiter());
    toString.add("optimize index: " + (optimizeIndex() ? "true" : "false"));
    toString.add("path to data file: " + getPathToDataFile());
    toString.add("property file: " + getPathToPropertiesFile());
    toString.add("retry count: " + getRetryCount());
    toString.add("skip fields: " + getSkipFields());
    toString.add("sleep millis between retries: " + getSleepMillisBetweenRetries());
    toString.add("soft commit frequency: " + getSoftCommitFrequency());
    toString.add("thread count: " + getThreadCount());
    toString.add("unique key field name: " + getUniqueKeyFieldName());
    toString.add("unique key field value: " + getUniqueKeyFieldValue());
    toString.add("unique key field value delimiter: " + getUniqueKeyFieldValueDelimiter());
    toString.add("zookeeper client connect timeout: " + getZookeeperClientConnectionTimeout());
    toString.add("zookeeper ensemble connect timeout: " + getZookeeperEnsembleConnectionTimeout());
    toString.add("zookeeper host: " + getZookeeperHost());
    return toString;
  }
}
