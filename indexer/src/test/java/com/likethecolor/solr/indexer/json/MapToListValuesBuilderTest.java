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
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldsParser;
import com.likethecolor.solr.indexer.json.MapToListValuesBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapToListValuesBuilderTest {
  private static final String FIELDS = "key0:string;key1:long;key2:double";
  private static final String KEY0_VALUE = "some string";
  private static final String KEY1_VALUE = "1231313131";
  private static final String KEY2_VALUE = "3.14159";
  private Map<String, String> dataMap;
  private Configuration configuration;

  @Before
  public void setUp() {
    dataMap = new HashMap<>();
    dataMap.put("key2", KEY2_VALUE);
    dataMap.put("key0", KEY0_VALUE);
    dataMap.put("key1", KEY1_VALUE);

    configuration = new Configuration();
    configuration.setFields(FIELDS);
  }

  @Test
  public void testBuild() {
    MapToListValuesBuilder mapToListValuesBuilder = getMapToListValuesBuilder(configuration);

    List<Object> values = mapToListValuesBuilder.build(dataMap);

    assertEquals(3, values.size());
    assertEquals(KEY0_VALUE, values.get(0));
    assertEquals(KEY1_VALUE, values.get(1));
    assertEquals(KEY2_VALUE, values.get(2));
  }

  @Test
  public void testBuild_ExtraKeyInDataMap() {
    dataMap.put("key3", "extra");

    MapToListValuesBuilder mapToListValuesBuilder = getMapToListValuesBuilder(configuration);

    List<Object> values = mapToListValuesBuilder.build(dataMap);

    assertEquals(3, values.size());
    assertEquals(KEY0_VALUE, values.get(0));
    assertEquals(KEY1_VALUE, values.get(1));
    assertEquals(KEY2_VALUE, values.get(2));
  }

  @Test
  public void testBuild_ExtraKeyInField() {
    Configuration configuration = new Configuration();
    configuration.setFields(FIELDS + ";key3:string");
    MapToListValuesBuilder mapToListValuesBuilder = getMapToListValuesBuilder(configuration);

    List<Object> values = mapToListValuesBuilder.build(dataMap);

    assertEquals(3, values.size());
    assertEquals(KEY0_VALUE, values.get(0));
    assertEquals(KEY1_VALUE, values.get(1));
    assertEquals(KEY2_VALUE, values.get(2));
  }

  private MapToListValuesBuilder getMapToListValuesBuilder(Configuration configuration) {
    Map<String, FieldDefinition> fieldDefinitionMap = new FieldsParser(configuration).parse(configuration.getFields());
    return new MapToListValuesBuilder(fieldDefinitionMap);
  }
}
