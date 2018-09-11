/**
 * Copyright 2018.  Dan Brown <dan@likethecolor.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likethecolor.solr.indexer.configuration;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConfigPropertiesTest {
  private static final String CONFIG_FILE_PATH = "/configpropertiestest.properties";

  @Test
  public void testConstructor_PassInProperties() {
    final int expectedInteger = 100;
    final double expectedDouble = 300.3333333D;
    final boolean expectedBoolean = true;
    final String expectedString = "a string value";

    final Properties props = new Properties();
    props.setProperty("integer", "100");
    props.setProperty("float", "200.222F");
    props.setProperty("double", "300.3333333D");
    props.setProperty("boolean", "true");
    props.setProperty("string", expectedString);

    final MockConfigProperties properties = new MockConfigProperties(props);

    final int actualInteger = properties.getInt("integer", 500);
    final double actualDouble = properties.getDouble("double", 555.555D);
    final boolean actualBoolean = properties.getBoolean("boolean", false);
    final String actualString = properties.getString("string");

    assertEquals(expectedInteger, actualInteger);
    assertEquals(expectedDouble, actualDouble, 0.0);
    assertEquals(expectedBoolean, actualBoolean);
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testConstructor_FileDoesNotExist() {
    try {
      new MockConfigProperties("/does/not/exist.properties");
      fail("Expected NPE since the file does not exist");
    } catch (NullPointerException e) {
      // expected
      assertTrue(e.getMessage().contains("Failed to load config file"));
    }
  }

  @Test
  public void testGetInt() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    int expectedInteger = 1;

    // property exists - default value not returned
    int actualInteger = properties.getInt("integer", 11);

    assertEquals(expectedInteger, actualInteger);

    // property does not exist - default value returned
    expectedInteger = 11;
    actualInteger = properties.getInt("unknown-integer", expectedInteger);

    assertEquals(expectedInteger, actualInteger);
  }

  @Test
  public void testGetChar() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    char expectedCharacter = ',';

    // property exists - default value not returned
    char actualCharacter = properties.getChar("character", '\t');

    assertEquals(expectedCharacter, actualCharacter);

    // property does not exist - default value returned
    expectedCharacter = '\u0002';
    actualCharacter = properties.getChar("unknown-character", expectedCharacter);

    assertEquals(expectedCharacter, actualCharacter);
  }

  @Test
  public void testGetDouble() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    double expectedDouble = 3.3D;

    // property exists - default value not returned
    double actualDouble = properties.getDouble("double", 11.22D);

    assertEquals(expectedDouble, actualDouble, 0.0);

    // property does not exist - default value returned
    expectedDouble = 11.22232131D;
    actualDouble = properties.getDouble("unknown-double", expectedDouble);

    assertEquals(expectedDouble, actualDouble, 0.0);
  }

  @Test
  public void testGetBoolean() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    boolean expectedBoolean = true;

    // property exists - default value not returned
    boolean actualBoolean = properties.getBoolean("boolean", true);

    assertEquals(expectedBoolean, actualBoolean);

    // property does not exist - default value returned
    expectedBoolean = false;
    actualBoolean = properties.getBoolean("unknown-boolean", expectedBoolean);

    assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  public void testGetString() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    String expectedString = "a string value";

    // property exists - default value not returned
    String actualString = properties.getString("string");

    assertEquals(expectedString, actualString);

    // property does not exist - default null value returned
    expectedString = null;
    actualString = properties.getString("unknown-string");

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetString_NoTrim() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    String expectedString = "\r\na string value\t";

    // property exists - default value not returned
    String actualString = properties.getString("string", false);

    assertEquals(expectedString, actualString);

    // property does not exist - default null value returned
    expectedString = null;
    actualString = properties.getString("unknown-string");

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetString_DefaultValue() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    String expectedString = "a string value";

    // property exists - default value not returned
    String actualString = properties.getString("string", "some string");

    assertEquals(expectedString, actualString);

    // property does not exist - default value returned
    expectedString = "some different string";
    actualString = properties.getString("unknown-string", expectedString);

    assertEquals(expectedString, actualString);

    // property does not exist - default value is null
    expectedString = null;
    actualString = properties.getString("unknown-string", null);

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetString_NeedsToBeTrimmed() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    String expectedString = "value";

    String actualString = properties.getString("needs-to-be-trimmed");

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetString_DefaultValueTrim() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    String expectedString = "value";

    // property exists - property value trimmed - default value not returned
    String actualString = properties.getString("needs-to-be-trimmed", "some string", true);

    assertEquals(expectedString, actualString);

    // property exists - property value not trimmed - default value not returned
    expectedString = "\r\na string value\t";
    actualString = properties.getString("string", expectedString, false);

    assertEquals(expectedString, actualString);

    // property does not exist - default value returned trimmed
    expectedString = "\t\r  some string\n";
    actualString = properties.getString("unknown-property", expectedString, true);

    assertEquals(expectedString.trim(), actualString);

    // property does not exist - default value returned untrimmed
    expectedString = "\t\r  some string\n";
    actualString = properties.getString("unknown-property", expectedString, false);

    assertEquals(expectedString, actualString);

    // property does not exist - default value is null
    expectedString = null;
    actualString = properties.getString("unknown-property", null, true);

    assertEquals(expectedString, actualString);

    // property does not exist - default value is null
    expectedString = null;
    actualString = properties.getString("unknown-property", null, false);

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetLong() {
    final MockConfigProperties properties = new MockConfigProperties(CONFIG_FILE_PATH);
    long expectedLong = 5000000000L;

    // property exists - default value not returned
    long actualLong = properties.getLong("long", 10L);

    assertEquals(expectedLong, actualLong);

    // property does not exist - default value returned
    expectedLong = 11L;
    actualLong = properties.getLong("unknown-long", expectedLong);

    assertEquals(expectedLong, actualLong);
  }

  class MockConfigProperties extends ConfigProperties {
    protected MockConfigProperties(Properties defaults) {
      super(defaults);
    }

    protected MockConfigProperties(final String filePath) {
      super(filePath);
    }
  }
}
