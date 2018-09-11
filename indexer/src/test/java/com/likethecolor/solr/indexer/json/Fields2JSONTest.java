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

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.exception.DuplicateJSON2FieldsException;
import com.likethecolor.solr.indexer.json.Fields2JSON;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Fields2JSONTest {
  @Test
  public void test() {
    String json2FieldsString = "json0:field0;json1:field1;json2:field2";
    Configuration configuration = new Configuration();
    configuration.setFieldsToJSON(json2FieldsString);

    Fields2JSON fields2JSON = new Fields2JSON(configuration);

    assertEquals(3, fields2JSON.size());
    assertTrue(fields2JSON.contains("json0"));
    assertTrue(fields2JSON.contains("json1"));
    assertTrue(fields2JSON.contains("json2"));
    assertEquals("field0", fields2JSON.get("json0"));
    assertEquals("field1", fields2JSON.get("json1"));
    assertEquals("field2", fields2JSON.get("json2"));
  }

  @Test
  public void testIterator() {
    Set<String> expectedKeys = new HashSet<>();
    expectedKeys.add("json0");
    expectedKeys.add("json1");
    expectedKeys.add("json2");

    String json2FieldsString = "json0:field0;json1:field1;json2:field2";
    Configuration configuration = new Configuration();
    configuration.setFieldsToJSON(json2FieldsString);

    Fields2JSON fields2JSON = new Fields2JSON(configuration);

    Iterator<String> iter = fields2JSON.iterator();
    while(iter.hasNext()) {
      String key = iter.next();
      assertTrue(expectedKeys.contains(key));
    }
  }

  @Test(expected = DuplicateJSON2FieldsException.class)
  public void testConstructor_DuplicateJSONField() {
    String json2FieldsString = "json0:field0;json1:field1;json2:field2;json1:field1a";
    Configuration configuration = new Configuration();
    configuration.setFieldsToJSON(json2FieldsString);

    new Fields2JSON(configuration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_MissingField() {
    String json2FieldsString = "json0:field0;json1;";
    Configuration configuration = new Configuration();
    configuration.setFieldsToJSON(json2FieldsString);

    new Fields2JSON(configuration);
  }
}
