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
import com.likethecolor.solr.indexer.util.conversion.ToCharacterConversion;
import com.likethecolor.solr.indexer.util.conversion.ToDoubleConversion;
import com.likethecolor.solr.indexer.util.conversion.ToIntegerConversion;
import com.likethecolor.solr.indexer.util.conversion.ToLongConversion;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * This class handles processing any command line flags.
 */
public class CommandLineConfigurationBuilder extends AnnotatedConfigurationBuilder implements Constants {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineConfigurationBuilder.class);
  private CommandLine commandLine;

  /**
   * Constructor.
   *
   * @param configuration configuration to populate from properties file
   */
  public CommandLineConfigurationBuilder(final Configuration configuration) {
    super(configuration);
  }

  public void parse(final CommandLine commandLine) {
    this.commandLine = commandLine;
    final Configuration configuration = getConfiguration();
    for(Method method : configuration.getClass().getMethods()) {
      ConfigurationValues configurationValues = method.getAnnotation(ConfigurationValues.class);
      if(configurationValues != null) {
        if(commandLine != null) {
          String dataType = "unknown";
          LOGGER.debug("method name: " + method.getName());
          String optionName = configurationValues.optionName();
          LOGGER.debug("long option annotation name: " + optionName);

          Class<?>[] paramTypes = method.getParameterTypes();
          Object[] params = new Object[paramTypes.length];
          dataType = setParamTypes(dataType, optionName, paramTypes, params);

          setValue(method, params, dataType, optionName);
        }
      }
    }
  }

  private String setParamTypes(String dataType, String optionName, Class<?>[] paramTypes, Object[] params) {
    for(int i = 0; i < paramTypes.length; ++i) {
      dataType = paramTypes[i].getSimpleName();

      validateParamType(paramTypes[i], dataType, optionName);

      setBoolean(paramTypes[i], params, i, optionName);
      setCharacter(paramTypes[i], params, i, optionName);
      setDouble(paramTypes[i], params, i, optionName);
      setInteger(paramTypes[i], params, i, optionName);
      setLong(paramTypes[i], params, i, optionName);
      setString(paramTypes[i], params, i, optionName);
    }
    return dataType;
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For booleans there should be a change only if the option
   * exists.
   *
   * @param paramType the type of the parameter
   * @param params the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  private void setBoolean(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new BooleanType(paramType).isBooleanType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = commandLine.hasOption(optionName);
      LOGGER.debug("boolean value: " + params[paramIndex]);
    }
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For characters there should be a change only if the option
   * exists.
   *
   * @param paramType the type of the parameter
   * @param params the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  private void setCharacter(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new CharacterType(paramType).isCharacterType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = new ToCharacterConversion(commandLine.getOptionValue(optionName)).getCharValue();
      LOGGER.debug("character value: " + params[paramIndex]);
    }
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For doubles there should be a change only if the option
   * exists.
   *
   * @param paramType the type of the parameter
   * @param params the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  private void setDouble(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new DoubleType(paramType).isDoubleType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = new ToDoubleConversion(commandLine.getOptionValue(optionName)).getDoubleValue();
      LOGGER.debug("double value: " + params[paramIndex]);
    }
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For integers there should be a change only if the option
   * exists.
   *
   * @param paramType  the type of the parameter
   * @param params     the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  private void setInteger(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new IntegerType(paramType).isIntegerType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = new ToIntegerConversion(commandLine.getOptionValue(optionName)).getIntegerValue();
      LOGGER.debug("integer value: " + params[paramIndex]);
    }
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For longs there should be a change only if the option
   * exists.
   *
   * @param paramType  the type of the parameter
   * @param params     the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  private void setLong(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new LongType(paramType).isLongType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = new ToLongConversion(commandLine.getOptionValue(optionName)).getLongValue();
      LOGGER.debug("long value: " + params[paramIndex]);
    }
  }

  /**
   * Properties file is king.  Command line is to override the properties in the
   * properties file.  For strings there should be a change only if the option
   * exists.
   *
   * @param paramType  the type of the parameter
   * @param params     the list of parameters
   * @param paramIndex the index of the list of parameters that this method will change
   * @param optionName name of the command line option
   */
  protected void setString(final Class<?> paramType, final Object[] params, int paramIndex, final String optionName) {
    if(new StringType(paramType).isStringType() && commandLine.hasOption(optionName)) {
      params[paramIndex] = commandLine.getOptionValue(optionName);
      LOGGER.debug("string value: " + params[paramIndex]);
    }
  }
}
