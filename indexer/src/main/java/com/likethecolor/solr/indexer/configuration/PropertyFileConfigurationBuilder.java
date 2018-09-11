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

import java.io.File;
import java.lang.reflect.Method;

/**
 * This class provides configuration information based on a properties file.
 * We assume that the Uploader will be run from command line so this class
 * uses {@link ConfigFactory#getConfigPropertiesFromAbsolutePath(String)}
 */
public class PropertyFileConfigurationBuilder extends AnnotatedConfigurationBuilder implements Constants {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileConfigurationBuilder.class);
  private static final String PATH_TO_PROPERTIES_FILE_CANNOT_BE_NULL_EMPTY = "path to properties file cannot be null/empty";
  private static final String PATH_TO_PROPERTIES_FILE_DOES_NOT_EXIST = "path to properties file does not exist: %s";
  private static final String PATH_TO_PROPERTIES_FILE_IS_NOT_READABLE = "path to properties file is not readable: %s";

  private ConfigProperties properties;

  /**
   * Constructor.
   *
   * @param configuration configuration to populate from properties file
   */
  public PropertyFileConfigurationBuilder(final Configuration configuration) {
    super(configuration);
  }

  /**
   * Parse the properties file then populate the {@link Configuration}
   * object with the parsed values.
   *
   * @param propertiesFile path to the properties file to be parsed
   */
  public void parse(final String propertiesFile) {
    if(propertiesFile == null || propertiesFile.trim().length() == 0) {
      LOGGER.error(PATH_TO_PROPERTIES_FILE_CANNOT_BE_NULL_EMPTY);
      throw new IllegalArgumentException(PATH_TO_PROPERTIES_FILE_CANNOT_BE_NULL_EMPTY);
    }
    final File file = new File(propertiesFile);
    if(!file.exists()) {
      LOGGER.error(String.format(PATH_TO_PROPERTIES_FILE_DOES_NOT_EXIST, propertiesFile));
      throw new IllegalArgumentException(String.format(PATH_TO_PROPERTIES_FILE_DOES_NOT_EXIST, propertiesFile));
    }
    if(!file.canRead()) {
      LOGGER.error(String.format(PATH_TO_PROPERTIES_FILE_IS_NOT_READABLE, propertiesFile));
      throw new IllegalArgumentException(String.format(PATH_TO_PROPERTIES_FILE_IS_NOT_READABLE, propertiesFile));
    }

    final ConfigProperties properties = parsePropertiesFile(propertiesFile);
    parse(properties);
  }

  /**
   * Parse the properties file then populate the {@link Configuration}
   * object with the parsed values.
   *
   * @param properties properties to populate the configuration
   */
  public void parse(final ConfigProperties properties) {
    setProperties(properties);

    final Configuration configuration = getConfiguration();

    for(Method method : configuration.getClass().getMethods()) {
      ConfigurationValues configurationValues = method.getAnnotation(ConfigurationValues.class);
      if(configurationValues != null) {
        LOGGER.debug("method name: " + method.getName());
        String dataType = "unknown";
        final String optionName = configurationValues.optionName();
        LOGGER.debug("option name: " + optionName);

        final String defaultValue =
            configurationValues.defaultValue().equals("") ? null
                                                          : configurationValues.defaultValue();
        LOGGER.debug("default string value: " + defaultValue);

        final Boolean defaultValueBoolean = configurationValues.defaultValueBoolean();
        LOGGER.debug("default boolean value: " + defaultValueBoolean);

        final char defaultValueCharacter = configurationValues.defaultValueCharacter();
        LOGGER.debug("default character value: " + defaultValueCharacter);

        final Double defaultValueDouble = configurationValues.defaultValueDouble();
        LOGGER.debug("default double value: " + defaultValueDouble);

        final Integer defaultValueInteger = configurationValues.defaultValueInteger();
        LOGGER.debug("default integer value: " + defaultValueInteger);

        final Long defaultValueLong = configurationValues.defaultValueLong();
        LOGGER.debug("default long value: " + defaultValueLong);

        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        dataType = setParamTypes(dataType, optionName, defaultValue, defaultValueBoolean, defaultValueCharacter, defaultValueDouble, defaultValueInteger, defaultValueLong, paramTypes, params);

        setValue(method, params, dataType, optionName);
      }
    }
  }

  private String setParamTypes(String dataType, String optionName, String defaultValue, Boolean defaultValueBoolean, char defaultValueCharacter, Double defaultValueDouble, Integer defaultValueInteger, Long defaultValueLong, Class<?>[] paramTypes, Object[] params) {
    for(int i = 0; i < paramTypes.length; ++i) {
      dataType = paramTypes[i].getSimpleName();

      validateParamType(paramTypes[i], dataType, optionName);

      setBoolean(paramTypes[i], params, i, optionName, defaultValueBoolean);
      setCharacter(paramTypes[i], params, i, optionName, defaultValueCharacter);
      setDouble(paramTypes[i], params, i, optionName, defaultValueDouble);
      setInteger(paramTypes[i], params, i, optionName, defaultValueInteger);
      setLong(paramTypes[i], params, i, optionName, defaultValueLong);
      setString(paramTypes[i], params, i, optionName, defaultValue);
    }
    return dataType;
  }

  /**
   * Return the properties resulting from parsing the properties file.
   *
   * @return properties resulting from parsing the properties file
   */
  private ConfigProperties getProperties() {
    return properties;
  }

  /**
   * Set the properties object.
   *
   * @param properties properties resulting from parsing the properties file
   */
  public void setProperties(final ConfigProperties properties) {
    this.properties = properties;
  }

  /**
   * Return the properties resulting from parsing the properties file.
   *
   * @param propertiesFile file containing properties to parse
   *
   * @return properties resulting from parsing the properties file
   */
  private ConfigProperties parsePropertiesFile(final String propertiesFile) {
    properties = ConfigFactory.getInstance().getConfigPropertiesFromAbsolutePath(propertiesFile);
    return properties;
  }

  private void setBoolean(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final Boolean defaultValueBoolean) {
    if(new BooleanType(paramType).isBooleanType()) {
      params[paramIndex] = getProperties().containsKey(optionName) && properties.getBoolean(optionName, defaultValueBoolean);
      LOGGER.debug("boolean value: " + params[paramIndex]);
    }
  }

  private void setCharacter(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final char defaultValueCharacter) {
    if(new CharacterType(paramType).isCharacterType()) {
      params[paramIndex] = getProperties().getChar(optionName, defaultValueCharacter);
      LOGGER.debug("character value: " + params[paramIndex]);
    }
  }

  private void setDouble(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final Double defaultValueDouble) {
    if(new DoubleType(paramType).isDoubleType()) {
      params[paramIndex] = getProperties().getDouble(optionName, defaultValueDouble);
      LOGGER.debug("double value: " + params[paramIndex]);
    }
  }

  private void setInteger(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final Integer defaultValueInteger) {
    if(new IntegerType(paramType).isIntegerType()) {
      params[paramIndex] = getProperties().getInt(optionName, defaultValueInteger);
      LOGGER.debug("integer value: " + params[paramIndex]);
    }
  }

  private void setLong(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final Long defaultValueLong) {
    if(new LongType(paramType).isLongType()) {
      params[paramIndex] = getProperties().getLong(optionName, defaultValueLong);
      LOGGER.debug("long value: " + params[paramIndex]);
    }
  }

  private void setString(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName, final String defaultValue) {
    if(new StringType(paramType).isStringType()) {
      params[paramIndex] = getProperties().getString(optionName, defaultValue, false);
      LOGGER.debug("string value: " + params[paramIndex]);
    }
  }
}
