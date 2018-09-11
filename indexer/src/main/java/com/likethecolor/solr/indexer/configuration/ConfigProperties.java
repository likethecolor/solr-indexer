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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties extends Properties {
  private static final long serialVersionUID = 1;
  
  /**
   * Protected so that it can only be instantiated by the {@link ConfigFactory}
   */
  protected ConfigProperties() {
    super();
  }

  /**
   * Constructor that uses the {@link Properties} provided.
   * Protected so that it can only be instantiated by the {@link ConfigFactory}
   *
   * @param defaults properties to use
   * @see Properties
   */
  protected ConfigProperties(Properties defaults) {
    super(defaults);
  }

  /**
   * Constructor that loads properties from the file given.
   * Protected so that it can only be instantiated by the {@link ConfigFactory}
   *
   * @param filePath is the class path to the config file
   */
  protected ConfigProperties(final String filePath) {
    final InputStream inStream = ConfigProperties.class.getResourceAsStream(filePath);
    try {
      load(inStream);
    } catch (final Exception e) {
      throw new NullPointerException("Failed to load config file: " +
        filePath + ", error: " + e.getMessage());
    } finally {
      if (inStream != null) {
        try {
          inStream.close();
        } catch (final IOException e) {
          // do nothing
        }
      }
    }
  }

  /**
   * Get a int value.  The return value from {@link Properties#getProperty(String)}
   * is converted to a int.  Returns default value if property name does not
   * exist.
   *
   * @param propertyName name of the property
   * @param defaultValue if property name does not exist return this value
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public int getInt(final String propertyName, final int defaultValue) {
    int propertyValue = defaultValue;

    final String valueStr = getProperty(propertyName);
    try {
      propertyValue = Integer.parseInt(valueStr);
    } catch (final Exception e) {
      // do nothing, just return the default value;
    }

    return propertyValue;
  }

  /**
   * Get a char value.  The return value from {@link Properties#getProperty(String)}
   * is converted to a char.  Returns default value if property name does not
   * exist.
   *
   * @param propertyName name of the property
   * @param defaultValue if property name does not exist return this value
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public char getChar(final String propertyName, final char defaultValue) {
    char propertyValue = defaultValue;

    final String valueStr = getProperty(propertyName);
    try {
      propertyValue = valueStr.charAt(0);
    } catch (final Exception e) {
      // do nothing, just return the default value;
    }

    return propertyValue;
  }

  /**
   * Get a double value.  The return value from {@link Properties#getProperty(String)}
   * is converted to a double.  Returns default value if property name does not
   * exist.
   *
   * @param propertyName name of the property
   * @param defaultValue if property name does not exist return this value
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public double getDouble(final String propertyName, final double defaultValue) {
    double propertyValue = defaultValue;

    final String valueStr = getProperty(propertyName);
    try {
      propertyValue = Double.parseDouble(valueStr);
    } catch (final Exception e) {
      // do nothing, just return the default value;
    }

    return propertyValue;
  }

  /**
   * Get a boolean value.  The return value from {@link Properties#getProperty(String)}
   * is converted to a boolean.  Returns default value if property name does not
   * exist.
   *
   * @param propertyName name of the property
   * @param defaultValue if property name does not exist return this value
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public boolean getBoolean(final String propertyName, final boolean defaultValue) {
    boolean propertyValue = defaultValue;

    final String valueStr = getProperty(propertyName);
    try {
      propertyValue = Boolean.parseBoolean(valueStr);
    } catch (final Exception e) {
      // do nothing, just return the default value;
    }

    return propertyValue;
  }

  /**
   * Get a string value.  This is the same as {@link Properties#getProperty(String)}
   * but conforms to the format of the other method names found in this class.
   * Returns null if property name does not exist.
   *
   * @param propertyName name of the property
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public String getString(final String propertyName) {
    return getString(propertyName, true);
  }

  /**
   * Get a string value.  This is the same as {@link Properties#getProperty(String)}
   * but conforms to the format of the other method names found in this class.
   * Returns null if property name does not exist.
   *
   * @param propertyName name of the property
   * @param trimValue    true if the value should be trimmed
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public String getString(final String propertyName, final boolean trimValue) {
    String value = getProperty(propertyName);
    if (trimValue && value != null) {
      value = value.trim();
    }
    return value;
  }

  /**
   * Get a string value.  This is the same as {@link Properties#getProperty(String, String)}
   * but conforms to the format of the other method names found in this class.
   *
   * @param propertyName name of the property
   * @param defaultValue a default value
   * @return value corresponding to the name
   * @see Properties#getProperty(String, String)
   */
  public String getString(final String propertyName, final String defaultValue) {
    String value = getProperty(propertyName, defaultValue);
    if (value != null) {
      value = value.trim();
    }
    return value;
  }

  /**
   * Get a string value.  This is the same as {@link Properties#getProperty(String, String)}
   * but conforms to the format of the other method names found in this class.
   *
   * @param propertyName name of the property
   * @param defaultValue default value to use
   * @param trimValue    true to trim the non-null value
   * @return value corresponding to the name
   * @see Properties#getProperty(String, String)
   */
  public String getString(final String propertyName, final String defaultValue, final boolean trimValue) {
    String value = getProperty(propertyName, defaultValue);
    if (trimValue && value != null) {
      value = value.trim();
    }
    return value;
  }

  /**
   * Get a long value.  The return value from {@link Properties#getProperty(String)}
   * is converted to a long.  Returns default value if property name does not
   * exist.
   *
   * @param propertyName name of the property
   * @param defaultValue if property name does not exist return this value
   * @return value corresponding to the name
   * @see Properties#getProperty(String)
   */
  public long getLong(final String propertyName, final long defaultValue) {
    long propertyValue = defaultValue;

    final String valueStr = getProperty(propertyName);
    try {
      propertyValue = Long.parseLong(valueStr);
    } catch (final Exception e) {
      // do nothing, just return the default value;
    }

    return propertyValue;
  }
}

