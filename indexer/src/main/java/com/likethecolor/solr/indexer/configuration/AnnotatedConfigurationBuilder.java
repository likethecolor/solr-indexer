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
import com.likethecolor.solr.indexer.util.annotation.BooleanType;
import com.likethecolor.solr.indexer.util.annotation.CharacterType;
import com.likethecolor.solr.indexer.util.annotation.DoubleType;
import com.likethecolor.solr.indexer.util.annotation.IntegerType;
import com.likethecolor.solr.indexer.util.annotation.LongType;
import com.likethecolor.solr.indexer.util.annotation.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class handles processing any configuration processing classes that handle
 * configuration annotations.
 */
public abstract class AnnotatedConfigurationBuilder implements Constants {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedConfigurationBuilder.class);
  private static final String CONFIGURATION_CANNOT_BE_NULL = "configuration cannot be null";
  private Configuration configuration;

  /**
   * Constructor.
   *
   * @param configuration configuration to populate from properties file
   */
  public AnnotatedConfigurationBuilder(final Configuration configuration) {
    if(configuration == null) {
      LOGGER.error(CONFIGURATION_CANNOT_BE_NULL);
      throw new IllegalArgumentException(CONFIGURATION_CANNOT_BE_NULL);
    }
    this.configuration = configuration;
  }

  protected void validateParamType(Class<?> paramType, String dataType, String optionName) {
    if(!new BooleanType(paramType).isBooleanType()
       && !new CharacterType(paramType).isCharacterType()
       && !new DoubleType(paramType).isDoubleType()
       && !new IntegerType(paramType).isIntegerType()
       && !new LongType(paramType).isLongType()
       && !new StringType(paramType).isStringType()) {
      throw new IllegalStateException(String.format("Unable to assign option value to setter [type:%s option:--%s]", dataType, optionName));
    }
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  protected void setValue(final Method method, final Object[] params, final String dataType, final String optionName) {
    if(params[0] != null) {
      try {
        LOGGER.debug("{}({})", method.getName(), paramListToString(params));
        method.invoke(getConfiguration(), params);
      }
      catch(IllegalAccessException e) {
        throw new IllegalArgumentException(getIllegalAccessExceptionString(dataType, optionName, params), e);
      }
      catch(InvocationTargetException e) {
        throw new IllegalArgumentException(getInvocationTargetExceptionString(dataType, optionName, params), e);
      }
    }
  }

  private String getIllegalAccessExceptionString(final String dataType, final String optionName, final Object[] params) {
    return String.format("Illegal access to configuration setter [data type:%s option:--%s params:%s]", dataType, optionName, paramListToString(params));
  }

  private String getInvocationTargetExceptionString(final String dataType, final String optionName, final Object[] params) {
    return String.format("Error invoking configuration setter [data type:%s option:--%s params:%s]", dataType, optionName, paramListToString(params));
  }

  private String paramListToString(final Object[] params) {
    final StringBuilder builder = new StringBuilder();
    if(params != null && params.length > 0) {
      for(Object param : params) {
        if(builder.length() > 0) {
          builder.append(", ");
        }
        if(param == null) {
          builder.append("null");
        }
        else {
          builder.append(param.toString());
        }
      }
    }
    return builder.toString();
  }
}
