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
package com.likethecolor.solr.indexer.configuration;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FieldStringParserTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(FieldStringParserTest.class);

  @Test
  public void testArray() {
    final Configuration configuration = new Configuration();
    final String fieldName = "categories";
    final String fieldType = Constants.FIELD_TYPE_ARRAY;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.ARRAY;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
    assertFalse(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testDateTime() {
    final Configuration configuration = new Configuration();
    final String fieldName = "create_datetime";
    final String fieldType = Constants.FIELD_TYPE_DATETIME;
    final String dateFormat = "yyyy-MM-dd";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + dateFormat;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DATETIME;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(dateFormat, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
    assertFalse(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testDateTime_Literal() throws ParseException {
    final Configuration configuration = new Configuration();
    final String fieldName = "create_datetime";
    final String fieldType = Constants.FIELD_TYPE_DATETIME;
    final String dateFormat = "MM/dd/yyyy hh:mm:ssa";
    final String dateString = new SimpleDateFormat(dateFormat).format(new Date());
    final Date expectedValue = new SimpleDateFormat(dateFormat).parse(dateString);
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + dateString;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DATETIME;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString, true);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(dateFormat, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertEquals(expectedValue, actualFieldDefinition.getValue());
    assertTrue(actualFieldDefinition.isLiteral());
  }

  @Test(expected = RuntimeException.class)
  public void testDateTime_Literal_BadFormat() throws ParseException {
//    LOGGER.error("expect error not being able to parse integer");
    final Configuration configuration = new Configuration();
    final String fieldName = "create_datetime";
    final String fieldType = Constants.FIELD_TYPE_DATETIME;
    final String dateFormat = "yyyy-MM-dd hh:mm:ssa";
    final String dateString = new SimpleDateFormat(dateFormat).format(new Date());
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + dateString;

    final FieldStringParser parser = new FieldStringParser(configuration);

    parser.parse(fieldString, true);
  }

  @Test
  public void testDateTime_ColonInFormat() {
    final Configuration configuration = new Configuration();
    final String fieldName = "create_datetime";
    final String fieldType = Constants.FIELD_TYPE_DATETIME;
    final String dateFormat = "yyyy-MM-dd hh:mm:ss";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + dateFormat;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DATETIME;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(dateFormat, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testDateTime_NoThirdElement() {
    final Configuration configuration = new Configuration();
    final String fieldName = "create_datetime";
    final String fieldType = Constants.FIELD_TYPE_DATETIME;
    final String dateFormat = Constants.DEFAULT_DATE_FORMAT;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DATETIME;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(dateFormat, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testDouble() {
    final Configuration configuration = new Configuration();
    final String fieldName = "avg_speed";
    final String fieldType = Constants.FIELD_TYPE_DOUBLE;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DOUBLE;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
    assertFalse(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testDouble_Literal() {
    final Configuration configuration = new Configuration();
    final String fieldName = "avg_speed";
    final String fieldType = Constants.FIELD_TYPE_DOUBLE;
    final String valueString = "123131.13131";
    final Double expectedValue = Double.parseDouble(valueString);
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.DOUBLE;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString, true);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertEquals(expectedValue, actualFieldDefinition.getValue());
    assertTrue(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testDouble_Literal_CannotParseValueAsDouble() {
    LOGGER.error("expect error not being able to parse numeric value");
    final Configuration configuration = new Configuration();
    final String fieldName = "avg_speed";
    final String fieldType = Constants.FIELD_TYPE_DOUBLE;
    final String valueString = "aaada.asas";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition fieldDefinition = parser.parse(fieldString, true);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testInteger() {
    final Configuration configuration = new Configuration();
    final String fieldName = "avg_speed";
    final String fieldType = Constants.FIELD_TYPE_INTEGER;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.INTEGER;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testInteger_Literal() {
    final Configuration configuration = new Configuration();
    final String fieldName = "avg_speed";
    final String fieldType = Constants.FIELD_TYPE_INTEGER;
    final String valueString = "123131";
    final Integer expectedValue = Integer.parseInt(valueString);
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.INTEGER;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString, true);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertEquals(expectedValue, actualFieldDefinition.getValue());
    assertTrue(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testInteger_Literal_CannotParseAsInteger() {
    LOGGER.error("expect error not being able to parse numeric value");
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_INTEGER;
    final String valueString = "123asaa131";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition fieldDefinition = parser.parse(fieldString, true);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testLong() {
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_LONG;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.LONG;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
    assertFalse(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testLong_Literal() {
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_LONG;
    final String valueString = "1231311311231";
    final Long expectedValue = Long.parseLong(valueString);
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.LONG;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString, true);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertEquals(expectedValue, actualFieldDefinition.getValue());
    assertTrue(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testLong_Literal_CannotParseAsLong() {
    LOGGER.error("expect error not being able to parse numeric value");
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_LONG;
    final String valueString = "1231addasda3113131";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + valueString;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition fieldDefinition = parser.parse(fieldString, true);

    assertNull(fieldDefinition.getValue());
  }

  @Test
  public void testString() {
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_STRING;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.STRING;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testLongLiteral_TooManyParts() {
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final String fieldType = Constants.FIELD_TYPE_LONG;
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER + fieldType + Constants.LIST_ELEMENT_DELIMITER + "foo" + Constants.LIST_ELEMENT_DELIMITER + "bar" + Constants.LIST_ELEMENT_DELIMITER + "baz";
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.LONG;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString, true);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
    assertTrue(actualFieldDefinition.isLiteral());
  }

  @Test
  public void testNoDelimiter_DefaultTypeToString() {
    final Configuration configuration = new Configuration();
    final String fieldName = "speed";
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.STRING;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldName);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testDelimiter_EmptyType() {
    final Configuration configuration = new Configuration();
    final String fieldName = "field_name";
    final String fieldString = fieldName + Constants.LIST_ELEMENT_DELIMITER;
    final FieldTypeEnum expectedFieldType = FieldTypeEnum.STRING;

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertEquals(fieldName, actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertEquals(expectedFieldType, actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testNull() {
    final Configuration configuration = new Configuration();
    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(null);

    assertNull(actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertNull(actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testEmptyString() {
    final Configuration configuration = new Configuration();
    final String fieldString = "";

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertNull(actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertNull(actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }

  @Test
  public void testEmptyWhiteSpaceString() {
    final Configuration configuration = new Configuration();
    final String fieldString = "\t  \r  \n";

    final FieldStringParser parser = new FieldStringParser(configuration);

    final FieldDefinition actualFieldDefinition = parser.parse(fieldString);

    assertNull(actualFieldDefinition.getName());
    assertEquals(Constants.DEFAULT_DATE_FORMAT, actualFieldDefinition.getDateFormat());
    assertNull(actualFieldDefinition.getType());
    assertNull(actualFieldDefinition.getValue());
  }
}
