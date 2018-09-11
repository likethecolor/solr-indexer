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

package com.likethecolor.solr.indexer.dynamic;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import org.apache.solr.common.SolrInputDocument;

import java.util.Map;

/**
 * Parent class that all dynamic classes should extend.  This class contains
 * methods that may be useful in handling dynamic classes.
 * 
 * A dynamic class is one that allows the calculation/generation of one field
 * in a {@link SolrInputDocument} from the value or values of other fields.  For
 * example, if you one field's value is to be the concatenation of 2 other fields
 * you would use a dynamic class.
 */
public abstract class DynamicClass implements Constants, Dynamic {
  private final Configuration configuration;
  private final SolrInputDocument solrInputDocument;
  
  public DynamicClass(Configuration configuration, SolrInputDocument solrInputDocument) {
    this.configuration = configuration;
    this.solrInputDocument = solrInputDocument;
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  protected SolrInputDocument getSolrInputDocument() {
    return solrInputDocument;
  }

  /**
   * Use this method to set the final value of the field.
   * 
   * @param fieldName name of the field whose value should be set
   * @param fieldValue value to set
   */
  protected void setFieldValue(String fieldName, Object fieldValue) {
    getSolrInputDocument().setField(fieldName, fieldValue);
  }

  /**
   * Pass in the name of a field and get its value.  If the field cannot be
   * found an empty string will be returned.
   * 
   * @param fieldName name of the field
   * @return value of the field or empty string
   */
  protected String getFieldValue(String fieldName) {
    String fieldValue = "";
    
    // returns null if field cannot be found in the solr document
    Object value = getSolrInputDocument().getFieldValue(fieldName);
    if(value != null && value instanceof Map) {
      Map map = (Map) value;
      if(map.containsKey(KEY_FOR_UPDATE_DOCUMENT_ACTION)) {
        fieldValue = String.valueOf(map.get(KEY_FOR_UPDATE_DOCUMENT_ACTION));
      }
    }
    else {
      if(value != null) {
        fieldValue = value.toString();
      }
    }
    return fieldValue;
  }

  /**
   * Get the value of the field as a double.
   * 
   * @param value name of field whose value is to be returned
   * @param defaultValue default value if the given value is null/empty
   * @return the value as a double or default value
   */
  protected double getDouble(String value, double defaultValue) {
    double doubleValue = defaultValue;
    if(!isNullOrEmpty(value)) {
      try {
        doubleValue = Double.parseDouble(value.trim());
      }
      catch(NumberFormatException ignore) {
      }
    }
    return doubleValue;
  }

  /**
   * Get the value of the field as a integer.
   * 
   * @param value name of field whose value is to be returned
   * @param defaultValue default value if the given value is null/empty
   * @return the value as a integer or default value
   */
  protected int getInteger(String value, int defaultValue) {
    int integerValue = defaultValue;
    if(!isNullOrEmpty(value)) {
      try {
        integerValue = Integer.parseInt(value.trim());
      }
      catch(NumberFormatException ignore) {
      }
    }
    return integerValue;
  }

  /**
   * Get the value of the field as a long.
   *
   * @param value name of field whose value is to be returned
   * @param defaultValue default value if the given value is null/empty
   *
   * @return the value as a long or default value
   */
  protected long getLong(String value, long defaultValue) {
    long longValue = defaultValue;
    if(!isNullOrEmpty(value)) {
      try {
        longValue = Long.parseLong(value.trim());
      }
      catch(NumberFormatException ignore) {
      }
    }
    return longValue;
  }

  /**
   * Return true if the value is null or empty.
   * @param value value to check
   * @return true if the value is null or empty
   */
  protected boolean isNullOrEmpty(String value) {
    return value == null || value.trim().length() == 0;
  }
}
