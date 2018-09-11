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

package com.likethecolor.solr.indexer.json;

import com.likethecolor.solr.indexer.field.FieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Using the field definition map product an ordered list of values from an unordered map of data.
 *
 * The field definition map is an ordered map {@link java.util.LinkedHashMap} which is how an ordered list of values
 * can be extracted in the order required.
 */
public class MapToListValuesBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(MapToListValuesBuilder.class);
  private Map<String, FieldDefinition> fieldDefinitionMap;

  public MapToListValuesBuilder(Map<String, FieldDefinition> fieldDefinitionMap) {
    this.fieldDefinitionMap = fieldDefinitionMap;
  }

  public List<Object> build(Map<String, String> dataMap) {
    List<Object> list = new ArrayList<>();
    for(String fieldName : fieldDefinitionMap.keySet()) {
      if(dataMap.containsKey(fieldName)) {
        list.add(dataMap.get(fieldName));
      }
      else {
        LOGGER.error("field does not exist in data map: field name: {}  available fields: {}", fieldName, dataMap.keySet());
      }
    }
    return list;
  }
}
