/**
 * Copyright 2018 Dan Brown <dan@likethecolor.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likethecolor.solr.indexer.configuration;

import java.util.ArrayList;
import java.util.Collection;

public class ZkHostParser {
  private static final String HOST_DELIMITER = ",";
  private static final String ROOT_PREFIX = "/";
  private Collection<String> zkHosts;
  private String zkRoot;

  public ZkHostParser() {
    zkHosts = new ArrayList<>();
    zkRoot = ROOT_PREFIX;
  }

  public void parse(String theseZkHosts) {
    String[] split = theseZkHosts.split(HOST_DELIMITER);
    for(String host : split) {
      if(host.contains(ROOT_PREFIX)) {
        String[] split2 = host.split(ROOT_PREFIX);
        if(split2.length == 2) {
          zkHosts.add(split2[0].trim());
          zkRoot = ROOT_PREFIX + split2[1].trim(); // root must start with a slash
        }
      }
      else {
        zkHosts.add(host.trim());
      }
    }
  }

  public Collection<String> getZkHosts() {
    return zkHosts;
  }

  public String getZkRoot() {
    return zkRoot;
  }
}
