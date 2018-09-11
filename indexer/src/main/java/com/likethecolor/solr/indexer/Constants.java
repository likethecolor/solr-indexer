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

public interface Constants {
  String DATA_TYPE_CSV = "csv";
  String DATA_TYPE_JSON = "json";
  String DATA_TYPE_DEFAULT = "default";

  String ARGS_LEFT_DELIMITER = "(";
  String ARGS_LIST_DELIMITER = ",";
  String ARGS_RIGHT_DELIMITER = ")";
  int DEFAULT_BATCH_SIZE = 1000;
  char DEFAULT_CSV_DELIMITER = ',';  // from CsvPreference#STANDARD_PREFERENCE
  char DEFAULT_CSV_QUOTE_CHARACTER = '"'; // from CsvPreference#STANDARD_PREFERENCE
  String DEFAULT_DATA_TYPE = DATA_TYPE_DEFAULT;
  String DEFAULT_DATE_FORMAT = "MM/dd/yyyy hh:mm:ssa"; // 03/01/2013 04:48:40am
  String DEFAULT_MULTIVALUE_FIELD_DELIMITER = ",";
  int DEFAULT_RETRY_COUNT = 4;
  long DEFAULT_SLEEP_MILLIS_BETWEEN_RETRIES = 5000;
  long DEFAULT_SOFT_COMMIT_FREQUENCY = 0; // 0 = never; >= 1 after every X batches
  int DEFAULT_THREAD_COUNT = 4;
  String DEFAULT_UNIQUE_KEY_FIELD_NAME = "id";
  String DEFAULT_UNIQUE_KEY_FIELD_VALUE_DELIMITER = "";
  int DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT = 1000;
  int DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT = 1000;
  String EQUALS_LIST_DELIMITER = "=";
  String LIST_DELIMITER = ";";
  String LIST_ELEMENT_DELIMITER = ":";
  String NEW_LINE = System.getProperty("line.separator");
  int REPORT_MODULUS = 1000000;

  // use this map to always update fields
  // see: http://lucene.472066.n3.nabble.com/Updating-document-with-the-Solr-Java-API-td3998411.html
  //
  // NOTE: This cannot be done on the unique id field - that field is unique
  // and will/should never get updated
  //
  String KEY_FOR_UPDATE_DOCUMENT_ACTION = "set";

  // field types
  String FIELD_TYPE_ARRAY = "multivalued";
  String FIELD_TYPE_DATETIME = "datetime";
  String FIELD_TYPE_DOUBLE = "double";
  String FIELD_TYPE_INTEGER = "int";
  String FIELD_TYPE_LONG = "long";
  String FIELD_TYPE_STRING = "string";

  // Command line/Properties flags
  // no short names
  String BATCH_SIZE_OPTION = "batch-size";
  String COLLECTION_NAME_OPTION = "collection-name";
  String CSV_DELIMITER_OPTION = "csv-delimiter";
  String CSV_QUOTE_CHARACTER_OPTION = "csv-quote-character";
  String DATA_TYPE_OPTION = "data-type";
  String DYNAMIC_OPTION = "dynamic-fields";
  String FIELDS_OPTION = "fields";
  String FIELDS_TO_JSON_OPTION = "fields-to-json";
  String FIRST_ROW_IS_HEADER_OPTION = "first-row-is-header";
  String HELP_OPTION = "help";
  String LITERALS_OPTION = "literals";
  String MULTIVALUE_FIELD_DELIMITER_OPTION = "multivalue-field-delimiter";
  String OPTIMIZE_INDEX_OPTION = "optimize-index";
  String PATH_TO_DATA_FILE_OPTION = "path-to-data-file";
  String PATH_TO_PROPERTIES_FILE_OPTION = "path-to-properties-file";
  String RETRY_COUNT_OPTION = "retry-count";
  String SKIP_FIELDS_OPTION = "skip-fields";
  String SLEEP_BETWEEN_RETRIES_OPTION = "sleep-millis-between-retries";
  String SOFT_COMMIT_FREQUENCY = "soft-commit-frequency";
  String THREAD_COUNT_OPTION = "thread-count";
  String UNIQUE_KEY_FIELD_NAME_OPTION = "unique-key-field-name";
  String UNIQUE_KEY_FIELD_VALUE_DELIMITER_OPTION = "unique-key-field-value-delimiter";
  String UNIQUE_KEY_FIELD_VALUE_OPTION = "unique-key-field-value";
  String ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT_OPTION = "zookeeper-client-connection-timeout";
  String ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT_OPTION = "zookeeper-ensemble-connection-timeout";
  String ZOOKEEPER_HOST_OPTION = "zookeeper-host";

  String COMMAND_STR = "java -jar ltc-solr-indexer-VERSION.jar";
}
