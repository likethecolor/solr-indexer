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

package com.likethecolor.solr.indexer.configuration.dynamic;

import com.likethecolor.solr.indexer.dynamic.DynamicClass;
import com.likethecolor.solr.indexer.dynamic.DynamicClassLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DynamicField {
  private static final Logger LOGGER = LoggerFactory.getLogger(DynamicField.class);
  private String className;
  private Class<DynamicClass> theClass;
  private String fieldName;
  private List<String> fieldNameArguments;

  public DynamicField() {
    fieldNameArguments = new ArrayList<>();
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    if(className == null) {
      className = "";
    }
    this.className = className.trim();
  }

  public Class<DynamicClass> getDynamicClass() {
    Class<DynamicClass> theClass = null;
    try {
      theClass = (Class<DynamicClass>) Class.forName(className);
      if(!DynamicClass.class.isAssignableFrom(theClass)) {
        throw new ClassNotFoundException("class not of DynamicClass: " + className);
      }
    }
    catch(ClassNotFoundException e) {
      LOGGER.error("cannot find class: " + className, e);
      throw new DynamicClassLoadingException(className);
    }
    return theClass;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    if(fieldName == null) {
      fieldName = "";
    }
    this.fieldName = fieldName.trim();
  }

  public void addFieldNameArgument(String fieldNameArgument) {
    if(fieldNameArgument != null && fieldNameArgument.trim().length() > 0) {
      fieldNameArguments.add(fieldNameArgument.trim());
    }
  }

  public List<String> getFieldNameArguments() {
    return fieldNameArguments;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DynamicField{");
    sb.append("className='").append(className).append('\'');
    sb.append(", fieldName='").append(fieldName).append('\'');
    sb.append(", fieldNameArguments=").append(fieldNameArguments);
    sb.append('}');
    return sb.toString();
  }
}
