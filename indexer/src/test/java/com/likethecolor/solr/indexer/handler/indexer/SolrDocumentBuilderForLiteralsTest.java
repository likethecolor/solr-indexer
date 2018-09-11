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
package com.likethecolor.solr.indexer.handler.indexer;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentBuilderForLiterals;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SolrDocumentBuilderForLiteralsTest {
  private static final String KEY_FOR_UPDATE_ACTION = "set";
  private static final String MAP_PREFIX = "name";

  @Test
  public void testBuild_ZeroFields() {
    final Configuration configuration = new Configuration();
    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);

    final Map<String, FieldDefinition> literalsMap = new HashMap<>();

    final SolrDocumentBuilderForLiterals builder = new SolrDocumentBuilderForLiterals(uniqueKeyFieldValueGenerator, literalsMap);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    assertEquals(0, doc.keySet().size());

    assertEquals("", uniqueKeyFieldValueGenerator.getId());
  }

  @Test
  public void testBuild_OneField() {
    final int numberOfFields = 1;
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 0);
    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final Map<String, Object> expectedFieldValue = new HashMap<>();
    expectedFieldValue.put(KEY_FOR_UPDATE_ACTION, 0);

    final Map<String, FieldDefinition> literalsMap = getLiteralsDefinitionMap(numberOfFields);

    final SolrDocumentBuilderForLiterals builder = new SolrDocumentBuilderForLiterals(uniqueKeyFieldValueGenerator, literalsMap);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    for(int i = 0; i < numberOfFields; i++) {
      SolrInputField field = doc.get(MAP_PREFIX + i);
      assertEquals(MAP_PREFIX + i, field.getName());
      assertEquals(expectedFieldValue, field.getValue());
    }

    assertEquals("0", uniqueKeyFieldValueGenerator.getId());
  }

  @Test
  public void testBuild_TwoFields() {
    final int numberOfFields = 2;
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 0 + ";" + MAP_PREFIX + 1);
    configuration.setUniqueKeyFieldValueDelimiter("-");

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final Map<String, Object> expectedFieldValue = new HashMap<>();
    for(int i = 0; i < numberOfFields; i++) {
      expectedFieldValue.put(KEY_FOR_UPDATE_ACTION, i);
    }

    final Map<String, FieldDefinition> literalsMap = getLiteralsDefinitionMap(numberOfFields);

    final SolrDocumentBuilderForLiterals builder = new SolrDocumentBuilderForLiterals(uniqueKeyFieldValueGenerator, literalsMap);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    for(int i = 0; i < numberOfFields; i++) {
      expectedFieldValue.clear();
      expectedFieldValue.put(KEY_FOR_UPDATE_ACTION, i);

      SolrInputField field = doc.get(MAP_PREFIX + i);
      assertEquals(MAP_PREFIX + i, field.getName());
      assertEquals(expectedFieldValue, field.getValue());
    }

    assertEquals("0-1", uniqueKeyFieldValueGenerator.getId());
  }

  @Test
  public void testBuild_MultipleFields() {
    final int numberOfFields = 20;
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 11 + ";" + MAP_PREFIX + 10);
    configuration.setUniqueKeyFieldValueDelimiter("-");
    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final Map<String, Object> expectedFieldValue = new HashMap<>();
    for(int i = 0; i < numberOfFields; i++) {
      expectedFieldValue.put(KEY_FOR_UPDATE_ACTION, i);
    }

    final Map<String, FieldDefinition> literalsMap = getLiteralsDefinitionMap(numberOfFields);

    final SolrDocumentBuilderForLiterals builder = new SolrDocumentBuilderForLiterals(uniqueKeyFieldValueGenerator, literalsMap);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    for(int i = 0; i < numberOfFields; i++) {
      expectedFieldValue.clear();
      expectedFieldValue.put(KEY_FOR_UPDATE_ACTION, i);

      SolrInputField field = doc.get(MAP_PREFIX + i);
      assertEquals(MAP_PREFIX + i, field.getName());
      assertEquals(expectedFieldValue, field.getValue());
    }
    System.out.println();

    assertEquals("11-10", uniqueKeyFieldValueGenerator.getId());
  }

  private Map<String, FieldDefinition> getLiteralsDefinitionMap(final int sizeOfMap) {
    final Map<String, FieldDefinition> definitionMap = new HashMap<>(sizeOfMap);
    for(int i = 0; i < sizeOfMap; i++) {
      definitionMap.put("value" + i, getFieldDefinition(MAP_PREFIX + i, true, i));
    }
    return definitionMap;
  }

  private FieldDefinition getFieldDefinition(final String fieldName, final boolean isLiteral, final Object value) {
    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setIsLiteral(isLiteral);
    fieldDefinition.setValue(value);
    return fieldDefinition;
  }
}
