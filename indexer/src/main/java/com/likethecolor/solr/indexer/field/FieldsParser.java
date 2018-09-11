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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.FieldStringParser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses the fields value {@link Configuration#getFields()}
 * into a map of field name =&gt; {@link FieldDefinition}
 */
public class FieldsParser {
  private FieldStringParser fieldStringParser;

  public FieldsParser(final Configuration configuration) {
    fieldStringParser = new FieldStringParser(configuration);
  }

  /**
   * Parse the fieldString string into a Map&lt;String,Field&gt; where the String is the
   * field name.  A {@link java.util.LinkedHashMap} is used to keep the order
   * the same as it appears in the configuration.
   *
   * @param fieldString string containing field definitions
   *
   * @return map
   */
  public Map<String, FieldDefinition> parse(String fieldString) {
    return parse(fieldString, false);
  }

  /**
   * Parse the fieldString string into a Map&lt;String,Field&gt; where the String is the
   * field name.  A {@link java.util.LinkedHashMap} is used to keep the order
   * the same as it appears in the configuration.
   *
   * @param fieldString string containing field definitions
   * @param isLiteral true if the string being parsed is for literals
   *
   * @return map
   */
  public Map<String, FieldDefinition> parse(String fieldString, final boolean isLiteral) {
    final Map<String, FieldDefinition> fieldsMap = new LinkedHashMap<>();
    if(fieldString != null && fieldString.trim().length() > 0) {
      FieldDefinition fieldDefinitionDefinition;
      final String[] fieldParts = fieldString.trim().split(Constants.LIST_DELIMITER);
      for(String fields : fieldParts) {
        fieldDefinitionDefinition = fieldStringParser.parse(fields, isLiteral);
        fieldsMap.put(fieldDefinitionDefinition.getName(), fieldDefinitionDefinition);
      }
    }
    return fieldsMap;
  }
}
