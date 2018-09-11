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
import com.likethecolor.solr.indexer.field.FieldTypeEnum;
import com.likethecolor.solr.indexer.field.FieldValueSetter;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentBuilderForFields;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SolrDocumentBuilderForFieldsTest {
  @Test
  public void testBuild_OneField() {
    final Configuration configuration = new Configuration();
    final String uniqueKeyFieldName = "id";
    final Integer value = 1231;
    final String stringValue = String.valueOf(value);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);

    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);

    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringValue);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName("id");
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);

    builder.build(doc, rowValues);

    final SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(value, field.getValue());
  }

  @Test
  public void testBuild_TwoFieldsOneNull() {
    final Configuration configuration = new Configuration();
    final String uniqueKeyFieldName = "id";
    final Integer idValue = 1231;
    final String stringIdValue = String.valueOf(idValue);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);

    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);

    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringIdValue);
    rowValues.add(null);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(uniqueKeyFieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName("field0");
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);

    builder.build(doc, rowValues);

    assertEquals(1, doc.keySet().size());

    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(idValue, field.getValue());
  }

  @Test
  public void testBuild_ThreeFields_MiddleFieldNull() {
    final Configuration configuration = new Configuration();
    final String uniqueKeyFieldName = "id";
    final Integer idValue = 1231;
    final String stringIdValue = String.valueOf(idValue);

    final String fieldName0 = "field0";
    final String stringValue0 = null;

    final String fieldName1 = "field1";
    final Double value1 = 1231D;
    final String stringValue1 = String.valueOf(value1);

    final Map<String, Object> expectedValue = new HashMap<>();
    expectedValue.put("set", value1);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringIdValue);
    rowValues.add(stringValue0);
    rowValues.add(stringValue1);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(uniqueKeyFieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName0);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName1);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);

    builder.build(doc, rowValues);

    assertEquals(2, doc.keySet().size());
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(idValue, field.getValue());

    field = doc.get(fieldName1);
    assertEquals(fieldName1, field.getName());
    assertEquals(expectedValue, field.getValue());
  }

  @Test
  public void testBuild_ThreeFields_MiddleFieldEmptyString() {
    final String uniqueKeyFieldName = "id";
    final Integer idValue = 1231;
    final String stringIdValue = String.valueOf(idValue);

    final String fieldName0 = "field0";
    final String stringValue0 = "";

    final String fieldName1 = "field1";
    final Double value1 = 1231D;
    final String stringValue1 = String.valueOf(value1);
    final Map<String, Object> expectedValue = new HashMap<>();
    expectedValue.put("set", value1);

    final Configuration configuration = new Configuration();
    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringIdValue);
    rowValues.add(stringValue0);
    rowValues.add(stringValue1);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(uniqueKeyFieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName0);
    fieldDefinition.setType(FieldTypeEnum.STRING);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName1);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);

    builder.build(doc, rowValues);

    assertEquals(2, doc.keySet().size());
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(idValue, field.getValue());

    field = doc.get(fieldName1);
    assertEquals(fieldName1, field.getName());
    assertEquals(expectedValue, field.getValue());
  }

  @Test
  public void testBuild_MultipleFields_SkipFields() {
    final Configuration configuration = new Configuration();
    final String uniqueKeyFieldName = "id";
    final Integer idValue = 1231;
    final String stringIdValue = String.valueOf(idValue);

    final String fieldName0 = "field0";
    final String stringValue0 = "a value0";
    final Map<String, Object> expectedValue0 = new HashMap<>();
    expectedValue0.put("set", stringValue0);

    final String fieldName1 = "field1";
    final Double value1 = 1231D;
    final String stringValue1 = String.valueOf(value1);
    final Map<String, Object> expectedValue1 = new HashMap<>();
    expectedValue1.put("set", value1);

    final String fieldName2 = "field2";
    final String stringValue2 = "a value2";

    final String fieldName3 = "field3";
    final Double value3 = 1231D;
    final String stringValue3 = String.valueOf(value3);
    final Map<String, Object> expectedValue2 = new HashMap<>();
    expectedValue2.put("set", value3);

    final String fieldName4 = "field4";
    final Long value4 = 131213121L;
    final String stringValue4 = String.valueOf(value4);

    final List<String> skipFields = new ArrayList<>(2);
    skipFields.add(fieldName2);
    skipFields.add(fieldName4);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringIdValue);
    rowValues.add(stringValue0);
    rowValues.add(stringValue1);
    rowValues.add(stringValue2);
    rowValues.add(stringValue3);
    rowValues.add(stringValue4);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(uniqueKeyFieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName0);
    fieldDefinition.setType(FieldTypeEnum.STRING);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName1);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName2);
    fieldDefinition.setType(FieldTypeEnum.STRING);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName3);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName4);
    fieldDefinition.setType(FieldTypeEnum.LONG);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);
    builder.setSkipFields(skipFields);

    builder.build(doc, rowValues);

    assertEquals(4, doc.keySet().size());
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(idValue, field.getValue());

    field = doc.get(fieldName0);
    assertEquals(fieldName0, field.getName());
    assertEquals(expectedValue0, field.getValue());

    field = doc.get(fieldName1);
    assertEquals(fieldName1, field.getName());
    assertEquals(expectedValue1, field.getValue());

    field = doc.get(fieldName3);
    assertEquals(fieldName3, field.getName());
    assertEquals(expectedValue2, field.getValue());
  }

  @Test
  public void testBuild_MultipleFields_EmptyFieldsAndSkipFields() {
    final Configuration configuration = new Configuration();
    final String uniqueKeyFieldName = "id";
    final Integer idValue = 1231;
    final String stringIdValue = String.valueOf(idValue);

    final String fieldName0 = "field0";
    final String stringValue0 = "";

    final String fieldName1 = "field1";
    final Double value1 = 1231D;
    final String stringValue1 = String.valueOf(value1);
    final Map<String, Object> expectedValue1 = new HashMap<>();
    expectedValue1.put("set", value1);

    final String fieldName2 = "field2";
    final String stringValue2 = "a value";

    final String fieldName3 = "field3";
    final Double value3 = 1231D;
    final String stringValue3 = String.valueOf(value3);
    final Map<String, Object> expectedValue2 = new HashMap<>();
    expectedValue2.put("set", value3);

    final String fieldName4 = "field4";
    final Long value4 = 131213121L;
    final String stringValue4 = String.valueOf(value4);

    final List<String> skipFields = new ArrayList<>(2);
    skipFields.add(fieldName2);
    skipFields.add(fieldName4);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(stringIdValue);
    rowValues.add(stringValue0);
    rowValues.add(stringValue1);
    rowValues.add(stringValue2);
    rowValues.add(stringValue3);
    rowValues.add(stringValue4);

    final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(uniqueKeyFieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName0);
    fieldDefinition.setType(FieldTypeEnum.STRING);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName1);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName2);
    fieldDefinition.setType(FieldTypeEnum.STRING);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName3);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName4);
    fieldDefinition.setType(FieldTypeEnum.LONG);
    fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);

    final SolrInputDocument doc = new SolrInputDocument();

    final SolrDocumentBuilderForFields builder = new SolrDocumentBuilderForFields(fieldValueSetter, uniqueKeyFieldValueGenerator, uniqueKeyFieldName);
    builder.setFieldDefinitionMap(fieldDefinitionMap);
    builder.setSkipFields(skipFields);

    builder.build(doc, rowValues);

    assertEquals(3, doc.keySet().size());
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(idValue, field.getValue());

    field = doc.get(fieldName1);
    assertEquals(fieldName1, field.getName());
    assertEquals(expectedValue1, field.getValue());

    field = doc.get(fieldName3);
    assertEquals(fieldName3, field.getName());
    assertEquals(expectedValue2, field.getValue());
  }
}
