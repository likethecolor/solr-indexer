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

import com.likethecolor.solr.indexer.exception.DuplicateJSON2FieldsException;
import com.likethecolor.solr.indexer.util.ConfigurationMappingGenerator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationMappingGeneratorTest {
  @Test
  public void testGenerate() {
    Map<String, String> map = new HashMap<>();
    String str = "json0:field0;json1:field1;json2:field2";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();

    generator.generate(map, str);

    assertEquals(3, map.size());

    assertTrue(map.containsKey("json0"));
    assertTrue(map.containsKey("json1"));
    assertTrue(map.containsKey("json2"));

    assertEquals("field0", map.get("json0"));
    assertEquals("field1", map.get("json1"));
    assertEquals("field2", map.get("json2"));
  }

  @Test
  public void testGenerate_MapIsNull() {
    String str = "json0:field0;json1;";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();
    generator.generate(null, str);
  }

  @Test
  public void testGenerate_StringIsNull() {
    Map<String, String> map = new HashMap<>();

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();
    generator.generate(map, null);
  }

  @Test
  public void testGenerate_StringIsEmpty() {
    Map<String, String> map = new HashMap<>();

    String str = "";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();

    generator.generate(map, str);

    assertEquals(0, map.size());
  }

  @Test
  public void testGenerate_StringIsEmptyWhiteSpace() {
    Map<String, String> map = new HashMap<>();

    String str = "\t  \r\n";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();

    generator.generate(map, str);

    assertEquals(0, map.size());
  }

  @Test(expected = DuplicateJSON2FieldsException.class)
  public void testConstructor_DuplicateJSONField() {
    String str = "json0:field0;json1:field1;json2:field2;json1:field1a";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();
    generator.generate(new HashMap<String, String>(), str);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_MissingField() {
    String str = "json0:field0;json1;";

    ConfigurationMappingGenerator generator = new ConfigurationMappingGenerator();
    generator.generate(new HashMap<String, String>(), str);
  }
}
