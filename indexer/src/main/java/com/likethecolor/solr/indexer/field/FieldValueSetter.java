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
import com.likethecolor.solr.indexer.util.conversion.ToDateConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Add to the {@link FieldDefinition} the value.
 * Based on the field's type the value is modified before being set.  For example,
 * if the type is double the string is converted to a double.
 */
public class FieldValueSetter {
  private static final Logger LOGGER = LoggerFactory.getLogger(FieldValueSetter.class);
  private Configuration configuration;

  public FieldValueSetter(final Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Alter the value based on the field's type then set the altered value on
   * the field.  If the value is null the method returns.
   *
   * @param fieldDefinitionDefinition field object
   * @param value value to apply to the field
   */
  public void setFieldValue(final FieldDefinition fieldDefinitionDefinition, String value) {
    if(value == null) {
      return;
    }

    try {
      if(FieldTypeEnum.ARRAY == fieldDefinitionDefinition.getType()) {
        final List<String> valuesList = new ArrayList<>();
        String[] values = value.split(configuration.getMultivalueFieldDelimiter());
        Collections.addAll(valuesList, values);
        fieldDefinitionDefinition.setValue(valuesList);
      }
      else if(FieldTypeEnum.DATETIME == fieldDefinitionDefinition.getType()) {
        final String dateFormat = fieldDefinitionDefinition.getDateFormat();
        fieldDefinitionDefinition.setValue(new ToDateConversion(value, dateFormat).parseDate());
      }
      else if(FieldTypeEnum.DOUBLE == fieldDefinitionDefinition.getType()) {
        fieldDefinitionDefinition.setValue(Double.parseDouble(value));
      }
      else if(FieldTypeEnum.INTEGER == fieldDefinitionDefinition.getType()) {
        fieldDefinitionDefinition.setValue(Integer.parseInt(value));
      }
      else if(FieldTypeEnum.LONG == fieldDefinitionDefinition.getType()) {
        fieldDefinitionDefinition.setValue(Long.parseLong(value));
      }
      else if(FieldTypeEnum.STRING == fieldDefinitionDefinition.getType()) {
        fieldDefinitionDefinition.setValue(value);
      }
      else {
        LOGGER.error(String.format("cannot set field value unknown type: %s value: %s", fieldDefinitionDefinition.getType(), value));
      }
    }
    catch(NumberFormatException e) {
      LOGGER.error(String.format("cannot parse value - verify that the data type in the fields are valid (e.g., int when it should be long): field name: %s expected type: %s value: %s date format: %s list split delimiter '%s'", fieldDefinitionDefinition.getName(), fieldDefinitionDefinition.getType(), value, fieldDefinitionDefinition.getDateFormat(), fieldDefinitionDefinition.getListSplitDelimiter()), e);
    }
    catch(Exception e) {
      LOGGER.error(String.format("cannot parse value field name: %s expected type: %s value: %s date format: %s list split delimiter: %s", fieldDefinitionDefinition.getName(), fieldDefinitionDefinition.getType(), value, fieldDefinitionDefinition.getDateFormat(), fieldDefinitionDefinition.getListSplitDelimiter()), e);
    }
  }
}
