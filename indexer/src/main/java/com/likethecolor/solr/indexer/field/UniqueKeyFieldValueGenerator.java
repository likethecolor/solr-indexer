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
package com.likethecolor.solr.indexer.field;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.util.conversion.ToListConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * If there is a unique key field value defined this class will generate the
 * value that should be sent to solr for the id.
 */
public class UniqueKeyFieldValueGenerator {
  private static final Logger LOGGER = LoggerFactory.getLogger(UniqueKeyFieldValueGenerator.class);
  private Map<String, Object> fieldDefinitions;
  private Configuration configuration;

  public UniqueKeyFieldValueGenerator(Configuration configuration) {
    // the code uses the list of values in the unique key field value, in the
    // order in which they appear in the unique key field value.  This structure
    // doesn't need to know anything about order.
    fieldDefinitions = new HashMap<>();
    this.configuration = configuration;
  }

  /**
   * Add to the fields from which the values will be taken when generating
   * the unique key field value.
   *
   * @param fieldName name of the field
   * @param fieldValue value of the field
   */
  public void addField(final String fieldName, final Object fieldValue) {
    fieldDefinitions.put(fieldName, fieldValue);
  }

  /**
   * Loop through the fields listed in the unique key field value.  The name of
   * the field in the unique key field value list will be used to get the field definition from the
   * map that was populated by {@link #addField(String, Object)}.  A string will
   * be created that contains these values delimited by {@link Configuration#getUniqueKeyFieldValueDelimiter()}
   *
   * The {@link Configuration#hasUniqueKeyFieldValue()}
   * must return true in order for anything to be processed.  If this value is
   * false this method returns an empty string.
   *
   * @return delimited string
   */
  public String getId() {
    final StringBuilder id = new StringBuilder();
    if(configuration.hasUniqueKeyFieldValue()) {
      final List<String> uniqueKeyFieldValueList = new ToListConversion(configuration.getUniqueKeyFieldValue()).toList();

      for(String fieldName : uniqueKeyFieldValueList) {
        if(fieldDefinitions.containsKey(fieldName)) {
          String stringValue = getFieldValue(fieldName);
          if(stringValue != null) {
            if(id.length() > 0) {
              id.append(configuration.getUniqueKeyFieldValueDelimiter());
            }
            id.append(stringValue);
          }
          else {
            throw new IllegalStateException(String.format("field value in unique key field value %s is null", fieldName));
          }
        }
        else {
          LOGGER.error("format error");
          LOGGER.error("- check that the values in unique_key_field_value are also in fields and/or literals and not in skipped_fields");
          LOGGER.error(String.format("- check that the value in the data file for the field %s not null/empty", fieldName));
          throw new IllegalStateException(String.format("field in unique key field value %s is not in map", fieldName));
        }
      }
    }

    return id.toString();
  }

  private String getFieldValue(final String fieldName) {
    Object value = fieldDefinitions.get(fieldName);
    if(value != null) {
      if(value.toString().trim().length() > 0) {
        return value.toString();
      }
    }
    return null;
  }
}
