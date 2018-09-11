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
import com.likethecolor.solr.indexer.field.FieldValueSetter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

public class FieldStringParser implements Constants {
  private Configuration configuration;

  public FieldStringParser(final Configuration configuration) {
    this.configuration = configuration;
  }

  public FieldDefinition parse(String fieldValue) {
    return parse(fieldValue, false);
  }

  public FieldDefinition parse(final String fieldValue, final boolean isLiteral) {
    final FieldDefinition fieldDefinition = new FieldDefinition();
    if(fieldValue == null || fieldValue.trim().length() == 0) {
      return fieldDefinition;
    }
    final Deque<String> parsedValue = parseAsList(fieldValue);

    final String fieldName = parsedValue.removeFirst();
    final FieldTypeEnum fieldType = getFieldType(parsedValue);

    fieldDefinition.setName(fieldName);
    fieldDefinition.setIsLiteral(isLiteral);
    fieldDefinition.setType(fieldType);
    if(FieldTypeEnum.DATETIME.equals(fieldType)) {
      fieldDefinition.setDateFormat(getDateFormat(parsedValue, isLiteral));
    }
    setValueOnLiteral(fieldDefinition, parsedValue);
    return fieldDefinition;
  }

  private Deque<String> parseAsList(final String fieldValue) {
    final String[] parsedValue = fieldValue.trim().split(LIST_ELEMENT_DELIMITER);
    final Deque<String> deque = new ArrayDeque<>(parsedValue.length);
    Collections.addAll(deque, parsedValue);
    return deque;
  }

  /**
   * Return the field type by looking at the second element of the parsed string.
   * If there is no second element or if the second element is empty default to
   * string.
   *
   * @param parsedValue parsed field type string
   *
   * @return field type
   */
  private FieldTypeEnum getFieldType(final Deque<String> parsedValue) {
    if(parsedValue != null && parsedValue.size() > 0) {
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_ARRAY)) {
        return FieldTypeEnum.ARRAY;
      }
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_DATETIME)) {
        return FieldTypeEnum.DATETIME;
      }
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_DOUBLE)) {
        return FieldTypeEnum.DOUBLE;
      }
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_INTEGER)) {
        return FieldTypeEnum.INTEGER;
      }
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_LONG)) {
        return FieldTypeEnum.LONG;
      }
      if(null != containsAndRemove(parsedValue, FIELD_TYPE_STRING)) {
        return FieldTypeEnum.STRING;
      }
    }
    // default field type to string if no type found
    return FieldTypeEnum.STRING;
  }

  /**
   * Return the date value by looking at the third element of the parsed string.
   * If there is no third element or if the third element is empty default to
   * {@link Constants#DEFAULT_DATE_FORMAT}.
   *
   * @param parsedValue parsed field type string
   *
   * @return date
   */
  private String getDateFormat(final Deque<String> parsedValue, final boolean isLiteral) {
    if(isLiteral) {
      // if literal assume date format to be the default
      return DEFAULT_DATE_FORMAT;
    }

    String dateFormat = DEFAULT_DATE_FORMAT;
    if(parsedValue != null && parsedValue.size() > 0) {
      final StringBuilder builder = new StringBuilder();
      for(String aParsedValue : parsedValue) {
        if(builder.length() > 0) {
          builder.append(":");
        }
        builder.append(aParsedValue);
      }
      if(builder.length() > 0) {
        dateFormat = builder.toString();
      }
    }
    return dateFormat;
  }

  /**
   * Set on the field the value by looking at the third element of the parsed
   * string.  This is used for literal values.
   * <p/>
   * This assumes that by the time the parsed value gets here everything that
   * is left in the parsed value is the value.
   * <p/>
   * This does not handle multivalued types.
   *
   * @param fieldDefinition field on which to set the value
   * @param parsedValue parsed field type string
   */
  private void setValueOnLiteral(final FieldDefinition fieldDefinition, final Deque<String> parsedValue) {
    if(!fieldDefinition.isLiteral()) {
      return;
    }
    if(FieldTypeEnum.DATETIME.equals(fieldDefinition.getType())) {
      final StringBuilder value = new StringBuilder();
      final Iterator<String> iter = parsedValue.iterator();
      while(iter.hasNext()) {
        if(value.length() > 0) {
          value.append(":");
        }
        value.append(iter.next());
        iter.remove();
      }
      try {
        fieldDefinition.setValue(new SimpleDateFormat(fieldDefinition.getDateFormat()).parse(value.toString()));
      }
      catch(ParseException e) {
        throw new RuntimeException(String.format("cannot parse date value %s using format %s", value.toString(), fieldDefinition.getDateFormat()), e);
      }
    }

    if(parsedValue != null && parsedValue.size() == 1) {
      final FieldValueSetter fieldValueSetter = new FieldValueSetter(configuration);
      final String value = parsedValue.removeFirst();
      fieldValueSetter.setFieldValue(fieldDefinition, value);
    }
  }

  private String containsAndRemove(final Deque<String> parsedValue, final String valueToCheck) {
    String value;
    String theValue = null;
    if(parsedValue != null && parsedValue.size() > 0) {
      final Iterator<String> iter = parsedValue.iterator();
      while(iter.hasNext()) {
        value = iter.next();
        if(valueToCheck.equalsIgnoreCase(value)) {
          iter.remove();
          theValue = value;
          break;
        }
      }
    }
    return theValue;
  }
}
