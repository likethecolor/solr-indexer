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

public class ConfigurationValidator implements Constants {
  public void validate(Configuration configuration) {
    if(isNullOrEmpty(configuration.getPathToPropertiesFile())) {
      throw new IllegalArgumentException(String.format(
          "path to properties file (--%s) is required",
          PATH_TO_PROPERTIES_FILE_OPTION));
    }
    if(DATA_TYPE_DEFAULT.equalsIgnoreCase(configuration.getDataType().getName())
       || isNullOrEmpty(configuration.getDataType().getName())) {
      throw new IllegalArgumentException(String.format(
          "data type (--%s) is required",
          DATA_TYPE_OPTION));
    }
    if(isNullOrEmpty(configuration.getPathToDataFile())) {
      throw new IllegalArgumentException(String.format(
          "path to data file (--%s) is required",
          PATH_TO_DATA_FILE_OPTION));
    }
    if(configuration.getDataType().getName().equals(DATA_TYPE_JSON)
        && isNullOrEmpty(configuration.getFieldsToJSON())) {
      throw new IllegalArgumentException(String.format(
          "--%s cannot be empty for %s data type (--%s)",
          FIELDS_TO_JSON_OPTION, DATA_TYPE_JSON, DATA_TYPE_OPTION));
    }
    if(configuration.getDataType().getName().equals(DATA_TYPE_CSV)
        && isNullOrEmpty(configuration.getFields())) {
      throw new IllegalArgumentException(String.format(
          "--%s cannot be empty for %s data type (--%s)",
          FIELDS_OPTION, DATA_TYPE_CSV, DATA_TYPE_OPTION));
    }
  }

  private boolean isNullOrEmpty(String value) {
    return value == null || value.trim().length() == 0;
  }
}
