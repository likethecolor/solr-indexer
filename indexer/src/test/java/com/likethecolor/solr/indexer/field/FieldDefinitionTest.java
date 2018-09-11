/**
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

import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldTypeEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FieldDefinitionTest {
  @Test
  public void testDateFormat() {
    final String dateFormat = "yyyy-MM-dd";
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setDateFormat(dateFormat);

    assertEquals(dateFormat, fieldDefinition.getDateFormat());

    // should be trimmed
    fieldDefinition.setDateFormat("  " + dateFormat + "\n   \t");

    assertEquals(dateFormat, fieldDefinition.getDateFormat());

    // null should change the value
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setDateFormat(null);

    assertNull(fieldDefinition.getDateFormat());

    // empty string should change the value
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setDateFormat("");

    assertEquals("", fieldDefinition.getDateFormat());

    // empty white space string should change the value and be trimmed
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setDateFormat("\t  \r\n ");

    assertEquals("", fieldDefinition.getDateFormat());
  }

  @Test
  public void testIdLiteral() {
    final FieldDefinition fieldDefinition = new FieldDefinition();

    assertFalse(fieldDefinition.isLiteral());

    fieldDefinition.setIsLiteral(true);

    assertTrue(fieldDefinition.isLiteral());

    fieldDefinition.setIsLiteral(false);

    assertFalse(fieldDefinition.isLiteral());
  }

  @Test
  public void testName() {
    final String name = "field_name";
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setName(name);

    assertEquals(name, fieldDefinition.getName());

    // should be trimmed
    fieldDefinition.setName("  " + name + "\n   \t");

    assertEquals(name, fieldDefinition.getName());

    // null should change the value
    fieldDefinition.setName(name);
    fieldDefinition.setName(null);

    assertNull(fieldDefinition.getName());

    // empty string should change the value
    fieldDefinition.setName(name);
    fieldDefinition.setName("");

    assertEquals("", fieldDefinition.getName());

    // empty white space string should change the value and be trimmed
    fieldDefinition.setName(name);
    fieldDefinition.setName("\t  \r\n ");

    assertEquals("", fieldDefinition.getName());
  }

  @Test
  public void testListSplitDelimiter() {
    final String listSplitDelimiter = "\t";
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);

    assertEquals(listSplitDelimiter, fieldDefinition.getListSplitDelimiter());


    // should not be trimmed
    fieldDefinition.setListSplitDelimiter("  " + listSplitDelimiter + "\n   \t");

    assertEquals("  " + listSplitDelimiter + "\n   \t", fieldDefinition.getListSplitDelimiter());

    // null should change the value
    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);
    fieldDefinition.setListSplitDelimiter(null);

    assertNull(fieldDefinition.getListSplitDelimiter());

    // empty string should change the value
    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);
    fieldDefinition.setListSplitDelimiter("");

    assertEquals("", fieldDefinition.getListSplitDelimiter());

    // empty white space string should change the value and not be trimmed
    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);
    fieldDefinition.setListSplitDelimiter("\t  \r\n ");

    assertEquals("\t  \r\n ", fieldDefinition.getListSplitDelimiter());
  }

  @Test
  public void testMultivalueFieldValueDelimiter() {
    final String multivalueFieldValueDelimiter = "\u0002";
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);

    assertEquals(multivalueFieldValueDelimiter, fieldDefinition.getMultivalueFieldValueDelimiter());


    // should not be trimmed
    fieldDefinition.setMultivalueFieldValueDelimiter("  " + multivalueFieldValueDelimiter + "\n   \t");

    assertEquals("  " + multivalueFieldValueDelimiter + "\n   \t", fieldDefinition.getMultivalueFieldValueDelimiter());

    // null should change the value
    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);
    fieldDefinition.setMultivalueFieldValueDelimiter(null);

    assertNull(fieldDefinition.getMultivalueFieldValueDelimiter());

    // empty string should change the value
    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);
    fieldDefinition.setMultivalueFieldValueDelimiter("");

    assertEquals("", fieldDefinition.getMultivalueFieldValueDelimiter());

    // empty white space string should change the value and not be trimmed
    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);
    fieldDefinition.setMultivalueFieldValueDelimiter("\t  \r\n ");

    assertEquals("\t  \r\n ", fieldDefinition.getMultivalueFieldValueDelimiter());
  }

  @Test
  public void testType() {
    final FieldTypeEnum type = FieldTypeEnum.ARRAY;
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setType(type);

    assertEquals(type, fieldDefinition.getType());
  }

  @Test
  public void testValue() {
    final String value = "my value";
    final FieldDefinition fieldDefinition = new FieldDefinition();

    fieldDefinition.setValue(value);

    assertEquals(value, fieldDefinition.getValue());
  }

  @Test
  public void testToString() {
    final String dateFormat = "yyyy-MM-dd hh:mm:ss";
    final String listSplitDelimiter = "::";
    final boolean isLiteral = true;
    final String multivalueFieldValueDelimiter = "\u0002";
    final String name = "field_name";
    final FieldTypeEnum type = FieldTypeEnum.ARRAY;
    final String value = "a string value";

    final String expectedString = new StringBuilder()
        .append("date format: ").append(dateFormat)
        .append("; list split delimiter: ").append(listSplitDelimiter)
        .append("; is literal: true")
        .append("; multi-value field value delimiter: ").append(multivalueFieldValueDelimiter)
        .append("; name: ").append(name)
        .append("; type: ").append(type)
        .append("; value: ").append(value)
        .toString();

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);
    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);
    fieldDefinition.setIsLiteral(isLiteral);
    fieldDefinition.setName(name);
    fieldDefinition.setType(type);
    fieldDefinition.setValue(value);

    final String actualString = fieldDefinition.toString();

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testClone() {
    final String dateFormat = "yyyy-MM-dd hh:mm:ss";
    final String listSplitDelimiter = "::";
    final boolean isLiteral = true;
    final String multivalueFieldValueDelimiter = "\u0002";
    final String name = "field_name";
    final FieldTypeEnum type = FieldTypeEnum.ARRAY;
    final String value = "a string value";

    final FieldDefinition fieldDefinition = new FieldDefinition();
    fieldDefinition.setDateFormat(dateFormat);
    fieldDefinition.setListSplitDelimiter(listSplitDelimiter);
    fieldDefinition.setIsLiteral(isLiteral);
    fieldDefinition.setMultivalueFieldValueDelimiter(multivalueFieldValueDelimiter);
    fieldDefinition.setName(name);
    fieldDefinition.setType(type);
    fieldDefinition.setValue(value);

    final FieldDefinition clone = fieldDefinition.clone();

    assertEquals(fieldDefinition, clone);
    assertNotSame(fieldDefinition, clone);
  }
}
