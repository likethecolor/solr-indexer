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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class ZkHostParserTest {
  @Test
  public void testParse_SingleHostNoRoot() {
    String zkHost = "zk01.ut1.afcv.net:2181";
    Collection<String> zkHosts = new ArrayList<>(1);
    zkHosts.add(zkHost);

    ZkHostParser parser = new ZkHostParser();
    parser.parse(zkHost);

    assertEquals(zkHosts.size(), parser.getZkHosts().size());
    Collection<String> actualZkHost = parser.getZkHosts();
    assertEquals(zkHosts, actualZkHost);
    assertEquals("/", parser.getZkRoot());
  }

  @Test
  public void testParse_SingleHostRoot() {
    String zkHost = "zk01.ut1.afcv.net:2181";
    String zkRoot = "/solr-72";
    Collection<String> zkHosts = new ArrayList<>(1);
    zkHosts.add(zkHost);

    ZkHostParser parser = new ZkHostParser();
    parser.parse(zkHost + zkRoot);

    assertEquals(zkHosts.size(), parser.getZkHosts().size());
    Collection<String> actualZkHost = parser.getZkHosts();
    assertEquals(zkHosts, actualZkHost);
    assertEquals(zkRoot, parser.getZkRoot());
  }

  @Test
  public void testParse_TwoHostsNoRoot() {
    String zkHost = "zk01.ut1.afcv.net:2181,zk02.ut1.afcv.net:2181";
    Collection<String> zkHosts = new ArrayList<>(1);
    zkHosts.add("zk01.ut1.afcv.net:2181");
    zkHosts.add("zk02.ut1.afcv.net:2181");

    ZkHostParser parser = new ZkHostParser();
    parser.parse(zkHost);

    assertEquals(zkHosts.size(), parser.getZkHosts().size());
    Collection<String> actualZkHost = parser.getZkHosts();
    assertEquals(zkHosts, actualZkHost);
    assertEquals("/", parser.getZkRoot());
  }

  @Test
  public void testParse_TwoHostsRoot() {
    String zkHost = "zk01.ut1.afcv.net:2181,zk02.ut1.afcv.net:2181/solr-72";
    Collection<String> zkHosts = new ArrayList<>(1);
    zkHosts.add("zk01.ut1.afcv.net:2181");
    zkHosts.add("zk02.ut1.afcv.net:2181");

    ZkHostParser parser = new ZkHostParser();
    parser.parse(zkHost);

    assertEquals(zkHosts.size(), parser.getZkHosts().size());
    Collection<String> actualZkHost = parser.getZkHosts();
    assertEquals(zkHosts, actualZkHost);
    assertEquals("/solr-72", parser.getZkRoot());
  }

  @Test
  public void testParse_ThreeHostsRoot() {
    String zkHost = "zk01.ut1.afcv.net:2181,zk02.ut1.afcv.net:2181,zk03.ut1.afcv.net:2181/solr-72";
    Collection<String> zkHosts = new ArrayList<>(1);
    zkHosts.add("zk01.ut1.afcv.net:2181");
    zkHosts.add("zk02.ut1.afcv.net:2181");
    zkHosts.add("zk03.ut1.afcv.net:2181");

    ZkHostParser parser = new ZkHostParser();
    parser.parse(zkHost);

    assertEquals(zkHosts.size(), parser.getZkHosts().size());
    Collection<String> actualZkHost = parser.getZkHosts();
    assertEquals(zkHosts, actualZkHost);
    assertEquals("/solr-72", parser.getZkRoot());
  }
}
