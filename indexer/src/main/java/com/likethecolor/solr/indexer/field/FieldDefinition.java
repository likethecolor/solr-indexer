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

public class FieldDefinition {
  private String dateFormat = Constants.DEFAULT_DATE_FORMAT;
  private String listSplitDelimiter;
  private boolean isLiteral;
  private String multivalueFieldValueDelimiter;
  private String name;
  private FieldTypeEnum type;
  private Object value;

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    if(dateFormat != null) {
      dateFormat = dateFormat.trim();
    }
    this.dateFormat = dateFormat;
  }

  public String getListSplitDelimiter() {
    return listSplitDelimiter;
  }

  public void setListSplitDelimiter(final String listSplitDelimiter) {
    this.listSplitDelimiter = listSplitDelimiter;
  }

  public void setIsLiteral(final boolean isLiteral) {
    this.isLiteral = isLiteral;
  }

  public boolean isLiteral() {
    return isLiteral;
  }

  public String getMultivalueFieldValueDelimiter() {
    return multivalueFieldValueDelimiter;
  }

  public void setMultivalueFieldValueDelimiter(String multivalueFieldValueDelimiter) {
    // no trim as a delimiter might be one that trim would remove
    this.multivalueFieldValueDelimiter = multivalueFieldValueDelimiter;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if(name != null) {
      name = name.trim();
    }
    this.name = name;
  }

  public FieldTypeEnum getType() {
    return type;
  }

  public void setType(FieldTypeEnum type) {
    this.type = type;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(o == null || getClass() != o.getClass()) {
      return false;
    }

    FieldDefinition fieldDefinition = (FieldDefinition) o;

    if(isLiteral != fieldDefinition.isLiteral) {
      return false;
    }
    if(dateFormat != null ? !dateFormat.equals(fieldDefinition.dateFormat)
                          : fieldDefinition.dateFormat != null) {
      return false;
    }
    if(listSplitDelimiter != null
       ? !listSplitDelimiter.equals(fieldDefinition.listSplitDelimiter)
       : fieldDefinition.listSplitDelimiter != null) {
      return false;
    }
    if(multivalueFieldValueDelimiter != null
       ? !multivalueFieldValueDelimiter.equals(fieldDefinition.multivalueFieldValueDelimiter)
       : fieldDefinition.multivalueFieldValueDelimiter != null) {
      return false;
    }
    if(name != null ? !name.equals(fieldDefinition.name)
                    : fieldDefinition.name != null) {
      return false;
    }
    if(type != fieldDefinition.type) {
      return false;
    }
    return !(value != null ? !value.equals(fieldDefinition.value)
                           : fieldDefinition.value != null);
  }

  @Override
  public int hashCode() {
    int result = dateFormat != null ? dateFormat.hashCode() : 0;
    result = 31 * result + (listSplitDelimiter != null
                            ? listSplitDelimiter.hashCode() : 0);
    result = 31 * result + (isLiteral ? 1 : 0);
    result = 31 * result + (multivalueFieldValueDelimiter != null
                            ? multivalueFieldValueDelimiter.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final String value = (getValue() == null ? null : getValue().toString());
    return new StringBuilder()
        .append("date format: ").append(getDateFormat())
        .append("; list split delimiter: ").append(getListSplitDelimiter())
        .append("; is literal: ").append((isLiteral() ? "true" : "false"))
        .append("; multi-value field value delimiter: ").append(getMultivalueFieldValueDelimiter())
        .append("; name: ").append(getName())
        .append("; type: ").append(getType())
        .append("; value: ").append(value)
        .toString();
  }

  public FieldDefinition clone() {
    try {
      super.clone();
    }
    catch(CloneNotSupportedException ignore) {
    }
    final FieldDefinition clone = new FieldDefinition();
    clone.setDateFormat(getDateFormat());
    clone.setListSplitDelimiter(getListSplitDelimiter());
    clone.setIsLiteral(isLiteral());
    clone.setMultivalueFieldValueDelimiter(getMultivalueFieldValueDelimiter());
    clone.setName(getName());
    clone.setType(getType());
    clone.setValue(getValue());
    return clone;
  }
}
