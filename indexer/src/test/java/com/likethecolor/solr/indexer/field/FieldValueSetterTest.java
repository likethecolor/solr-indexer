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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FieldValueSetterTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(FieldValueSetterTest.class);

  @Test
  public void testHandleArray() {
    final Configuration configuration = new Configuration();
    configuration.setMultivalueFieldDelimiter("\u0002");
    final String fieldName = "my_multivalued_field";
    final String value = "one\u0002two\u0002three";
    final List<String> expectedValue = new ArrayList<>();
    expectedValue.add("one");
    expectedValue.add("two");
    expectedValue.add("three");

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.ARRAY);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedValue, fieldDefinition.getValue());
  }

  @Test
  public void testHandleArray_NoDelimiterInValue() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_multivalued_field";
    final String value = "onetwothree";
    final List<String> expectedValue = new ArrayList<>();
    expectedValue.add("onetwothree");

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.ARRAY);
    fieldDefinition.setListSplitDelimiter("\u0002");

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedValue, fieldDefinition.getValue());
  }

  @Test
  public void testHandleDatetime() throws ParseException {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_double_field";
    final String value = "03/30/2014 10:14:33am";
    final Date expectedDate = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT).parse(value);

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.DATETIME);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedDate, fieldDefinition.getValue());
  }

  @Test
  public void testHandleDatetime_SpecificFormat() throws ParseException {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_double_field";
    final String value = "2014-03-30 22:33:44";
    final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    final Date expectedDate = new SimpleDateFormat(dateFormat).parse(value);

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setType(FieldTypeEnum.DATETIME);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedDate, fieldDefinition.getValue());
  }

  @Test
  public void testHandleDatetime_CannotParseAsDate() throws ParseException {
    final Configuration configuration = new Configuration();
    LOGGER.error("expect an error about not being able to parse datetime");
    final String fieldName = "my_double_field";
    final String value = "xx-aaaa-12312";
    final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setType(FieldTypeEnum.DATETIME);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testHandleDouble() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_double_field";
    final String value = "123.456";
    final Double expectedValue = Double.parseDouble(value);

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedValue, fieldDefinition.getValue());
  }

  @Test
  public void testHandleDouble_CannotParseValueAsDouble() {
    final Configuration configuration = new Configuration();
    LOGGER.error("expect an error about not being able to parse double");
    final String fieldName = "my_double_field";
    final String value = "abc.456";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.DOUBLE);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testHandleInteger() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_integer_field";
    final String value = "123456";
    final Integer expectedValue = Integer.parseInt(value);

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedValue, fieldDefinition.getValue());
  }

  @Test
  public void testHandleInteger_CannotParseValueAsInteger() {
    final Configuration configuration = new Configuration();
    LOGGER.error("expect an error about not being able to parse integer");
    final String fieldName = "my_integer_field";
    final String value = "asd121";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.INTEGER);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testHandleLong() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_long_field";
    final String value = "1234567890123456789";
    final Long expectedValue = Long.parseLong(value);

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.LONG);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(expectedValue, fieldDefinition.getValue());
  }

  @Test
  public void testHandleLong_CannotParseValueAsLong() {
    final Configuration configuration = new Configuration();
    LOGGER.error("expect an error about not being able to parse long");
    final String fieldName = "my_long_field";
    final String value = "14asda12eqsae1dq1d1";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.LONG);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testHandleString() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_string_field";
    final String value = "\tthis is a string\t  \r\n";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.STRING);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, value);

    assertEquals(value, fieldDefinition.getValue());
  }

  @Test
  public void testHandleNullValue() {
    final Configuration configuration = new Configuration();
    final String fieldName = "my_null_value_field";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setName(fieldName);
    fieldDefinition.setType(FieldTypeEnum.STRING);

    final FieldValueSetter setter = new FieldValueSetter(configuration);

    setter.setFieldValue(fieldDefinition, null);

    assertNull(fieldDefinition.getValue());
  }
}
