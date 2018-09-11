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
package com.likethecolor.solr.indexer.field;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.FieldStringParser;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldTypeEnum;
import com.likethecolor.solr.indexer.field.FieldsParser;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FieldsParserTest {
  private Configuration configuration = new Configuration();
  private FieldStringParser fieldStringParser = new FieldStringParser(configuration);

  @Test
  public void testParse() {
    final String fields = new StringBuilder()
        .append("site_id").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.INTEGER)
        .append(Constants.LIST_DELIMITER)
        .append("site_code").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.STRING)
        .append(Constants.LIST_DELIMITER)
        .append("create_datetime").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.DATETIME).append(Constants.LIST_ELEMENT_DELIMITER).append("yyyy-MM-dd hh:mm:ss")
        .append(Constants.LIST_DELIMITER)
        .append("update_datetime").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.DATETIME)
        .append(Constants.LIST_DELIMITER)
        .append("site_name").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.STRING)
        .append(Constants.LIST_DELIMITER)
        .append("site_url").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.STRING)
        .append(Constants.LIST_DELIMITER)
        .append("country").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.STRING)
        .append(Constants.LIST_DELIMITER)
        .append("cats").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.ARRAY)
        .toString();

    final Map<String, FieldDefinition> expectedMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = fieldStringParser.parse("site_id" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.INTEGER);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("site_code" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.STRING);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("create_datetime" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.DATETIME + Constants.LIST_ELEMENT_DELIMITER + "yyyy-MM-dd hh:mm:ss");
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("update_datetime" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.DATETIME);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("site_name" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.STRING);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("site_url" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.STRING);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("country" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.STRING);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);
    fieldDefinition = fieldStringParser.parse("cats" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.ARRAY);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);

    final FieldsParser fieldsParser = new FieldsParser(configuration);

    final Map<String, FieldDefinition> actualMap = fieldsParser.parse(fields);

    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testParse_NullEmptyFieldString() {
    final Map<String, FieldDefinition> expectedMap = new LinkedHashMap<>();

    final FieldsParser fieldsParser = new FieldsParser(configuration);

    Map<String, FieldDefinition> actualMap = fieldsParser.parse(null);

    assertEquals(expectedMap, actualMap);

    actualMap = fieldsParser.parse("");

    assertEquals(expectedMap, actualMap);

    actualMap = fieldsParser.parse("\t  ");

    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testParse_NoRowDelimiter() {
    final String fields = new StringBuilder()
        .append("site_id").append(Constants.LIST_ELEMENT_DELIMITER).append(FieldTypeEnum.INTEGER)
        .toString();

    final Map<String, FieldDefinition> expectedMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = fieldStringParser.parse("site_id" + Constants.LIST_ELEMENT_DELIMITER + FieldTypeEnum.INTEGER);
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);

    final FieldsParser fieldsParser = new FieldsParser(configuration);

    Map<String, FieldDefinition> actualMap = fieldsParser.parse(fields);

    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testParse_NoDelimiter() {
    final String fields = "site_id";

    final Map<String, FieldDefinition> expectedMap = new LinkedHashMap<>();
    FieldDefinition fieldDefinition = fieldStringParser.parse("site_id");
    expectedMap.put(fieldDefinition.getName(), fieldDefinition);

    final FieldsParser fieldsParser = new FieldsParser(configuration);

    Map<String, FieldDefinition> actualMap = fieldsParser.parse(fields);

    assertEquals(expectedMap, actualMap);
  }
}
