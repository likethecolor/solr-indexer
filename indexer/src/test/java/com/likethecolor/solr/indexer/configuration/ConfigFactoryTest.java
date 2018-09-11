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

import com.likethecolor.solr.indexer.configuration.ConfigFactory;
import com.likethecolor.solr.indexer.configuration.ConfigProperties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigFactoryTest {
  private static final String CONFIG_FILE_PATH = "/configpropertiestest.properties";

  @Test
  public void testGetConfigProperties() {
    char expectedCharacter = ',';
    double expectedDouble = 3.3D;
    int expectedInteger = 1;
    long expectedLong = 5000000000L;
    String expectedString = "a string value";

    ConfigProperties properties = ConfigFactory.getInstance().getConfigProperties(CONFIG_FILE_PATH);
    assertTrue(properties.getBoolean("boolean", false));
    assertEquals(expectedCharacter, properties.getChar("character", '\0'));
    assertEquals(expectedDouble, properties.getDouble("double", -1.0D), 0.0);
    assertEquals(expectedInteger, properties.getInt("integer", -1));
    assertEquals(expectedLong, properties.getLong("long", -1L));
    assertEquals(expectedString, properties.getString("string", "ffooo"));
  }
}
