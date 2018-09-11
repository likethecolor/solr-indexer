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

package com.likethecolor.solr.indexer.util;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.exception.DuplicateJSON2FieldsException;

import java.util.Map;

public class ConfigurationMappingGenerator implements Constants {
  public ConfigurationMappingGenerator() {
  }

  public void generate(Map<String, String> map, String str) {
    if(map == null || str == null || str.trim().length() == 0) {
      return;
    }
    String[] split = str.split(LIST_DELIMITER);
    for(String s : split) {
      String[] values = s.split(LIST_ELEMENT_DELIMITER);
      if(values.length != 2) {
        throw new IllegalArgumentException(String.format("cannot split \"%s\" into 2 using delimiter \"%s\"", s, LIST_DELIMITER));
      }
      if(map.containsKey(values[0])) {
        throw new DuplicateJSON2FieldsException(values[0], values[1]);
      }
      map.put(values[0], values[1]);
    }
  }
}
