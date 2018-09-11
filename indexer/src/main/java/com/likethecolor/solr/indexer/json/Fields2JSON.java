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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.util.ConfigurationMappingGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fields2JSON implements Constants {
  private Configuration configuration;
  private Map<String, String> fields2JSONMap;

  public Fields2JSON(Configuration configuration) {
    this.configuration = configuration;
    fields2JSONMap = new HashMap<>();
    buildMap();
  }

  public boolean contains(String key) {
    return fields2JSONMap.containsKey(key);
  }

  public Iterator<String> iterator() {
    return fields2JSONMap.keySet().iterator();
  }

  public String get(String key) {
    return fields2JSONMap.get(key);
  }

  public int size() {
    return fields2JSONMap.size();
  }

  private void buildMap() {
    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();
    generator.generate(fields2JSONMap, configuration.getFieldsToJSON());
  }
}
