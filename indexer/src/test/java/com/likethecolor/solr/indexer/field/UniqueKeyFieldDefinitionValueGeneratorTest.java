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

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.util.conversion.ToListConversion;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UniqueKeyFieldDefinitionValueGeneratorTest {
  private Configuration configuration = new Configuration();
  private FieldsParser fieldParser = new FieldsParser(configuration);
  private FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);

  @Test
  public void testGetId() {
    final String siteCode = "thepcpros";
    final String siteId = "1061";
    final String siteName = "The PC Pro's";
    final String indexType = "site";
    final String delimiter = "--";
    final String expectedId = indexType + delimiter + siteId + delimiter + siteCode + delimiter + siteName;
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValueDelimiter(delimiter);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final String fields = "site_id:int;site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String literals = "index_type:string:" + indexType + ";type_sort:int:3";
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";
    final String compositeId = "index_type;site_id;site_code;site_name";
    configuration.setUniqueKeyFieldValue(compositeId);

    final List<Object> rowValues = getRowValues(siteCode, siteId, siteName);
    addFieldsToUniqueKeyFieldValueGenerator(uniqueKeyFieldValueGenerator, rowValues, fields, literals, skipFields);

    final String actualUniqueKeyFieldValue = uniqueKeyFieldValueGenerator.getId();

    assertEquals(expectedId, actualUniqueKeyFieldValue);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetId_FieldInUniqueKeyFieldValueNotInField() {
    final String siteCode = "thepcpros";
    final String siteId = "1061";
    final String siteName = "The PC Pro's";
    final String indexType = "site";
    final String delimiter = "--";
    final String expectedId = indexType + delimiter + siteId + delimiter + siteCode + delimiter + siteName;
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValueDelimiter(delimiter);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final String fields = "site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String literals = "index_type:string:" + indexType + ";type_sort:int:3";
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";
    final String compositeId = "index_type;site_id;site_code;site_name";
    configuration.setUniqueKeyFieldValue(compositeId);

    final List<Object> rowValues = getRowValuesWithNoSiteId(siteCode, siteName);
    addFieldsToUniqueKeyFieldValueGenerator(uniqueKeyFieldValueGenerator, rowValues, fields, literals, skipFields);

    final String actualUniqueKeyFieldValue = uniqueKeyFieldValueGenerator.getId();

    assertEquals(expectedId, actualUniqueKeyFieldValue);
  }

  @Test
  public void testGetId_HasUniqueKeyFieldValueIsFalse() {
    final String siteCode = "thepcpros";
    final String siteId = "1061";
    final String siteName = "The PC Pro's";
    final String indexType = "site";
    final String delimiter = "--";
    final String expectedId = "";
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValueDelimiter(delimiter);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    final String fields = "site_id:int;site_code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa;update_datetime:datetime:MM/dd/yyyy hh:mm:ssa;site_name:string;site_url:string;country:string;currency:string;language:string;mobile:string";
    final String literals = "index_type:string:" + indexType + ";type_sort:int:3";
    final String skipFields = "create_datetime;update_datetime;country;currency;language;mobile";

    final List<Object> rowValues = getRowValues(siteCode, siteId, siteName);

    addFieldsToUniqueKeyFieldValueGenerator(uniqueKeyFieldValueGenerator, rowValues, fields, literals, skipFields);

    final String actualUniqueKeyFieldValue = uniqueKeyFieldValueGenerator.getId();

    assertEquals(expectedId, actualUniqueKeyFieldValue);
  }

  private void addFieldsToUniqueKeyFieldValueGenerator(final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator, final List<Object> rowValues, final String fields, final String literals, final String skipFields) {
    final Map<String, FieldDefinition> fieldsDefinitions = fieldParser.parse(fields);
    final Map<String, FieldDefinition> literalsDefinitions = fieldParser.parse(literals, true);
    final List<String> skipFieldsList = new ToListConversion(skipFields).toList();

    FieldDefinition fieldDefinition;
    int fieldIndex = 0;
    for(String fieldName : fieldsDefinitions.keySet()) {
      if(skipFieldsList.contains(fieldName)) {
        fieldIndex++;
        continue;
      }

      String value = (String) rowValues.get(fieldIndex);
      if(value != null && value.trim().length() > 0) {
        fieldDefinition = fieldsDefinitions.get(fieldName);
        fieldValueSetter.setFieldValue(fieldDefinition, value);
        uniqueKeyFieldValueGenerator.addField(fieldDefinition.getName(), fieldDefinition.getValue());
      }
      fieldIndex++;
    }
    for(String literal : literalsDefinitions.keySet()) {
      fieldDefinition = literalsDefinitions.get(literal);
      uniqueKeyFieldValueGenerator.addField(literal, fieldDefinition.getValue());
    }
  }

  private List<Object> getRowValues(final String siteCode, final String siteId, final String siteName) {
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(siteId);
    rowValues.add(siteCode);
    rowValues.add("01/01/2000 08:00:00pm");
    rowValues.add("03/12/2013 09:11:28am");
    rowValues.add(siteName);
    rowValues.add("http://www.thepcpros.com");
    rowValues.add("United States");
    rowValues.add("USD");
    rowValues.add("English");
    rowValues.add("0");
    return rowValues;
  }

  private List<Object> getRowValuesWithNoSiteId(final String siteCode, final String siteName) {
    final List<Object> rowValues = new ArrayList<>();
    rowValues.add(siteCode);
    rowValues.add("01/01/2000 08:00:00pm");
    rowValues.add("03/12/2013 09:11:28am");
    rowValues.add(siteName);
    rowValues.add("http://www.thepcpros.com");
    rowValues.add("United States");
    rowValues.add("USD");
    rowValues.add("English");
    rowValues.add("0");
    return rowValues;
  }
}
