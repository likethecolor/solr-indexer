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
package com.likethecolor.solr.indexer.util.conversion;

import com.likethecolor.solr.indexer.util.conversion.ToDoubleConversion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToDoubleConversionTest {
  @Test
  public void getDoubleValue() {
    final String stringValue = "3.14159";
    final Double expectedValue = Double.parseDouble(stringValue);

    assertEquals(expectedValue, new ToDoubleConversion(stringValue).getDoubleValue());
  }

  @Test
  public void getDoubleValue_NullEmptyValue() {
    final String stringValue = null;
    final Double expectedValue = null;

    assertEquals(expectedValue, new ToDoubleConversion(stringValue).getDoubleValue());
    assertEquals(expectedValue, new ToDoubleConversion("").getDoubleValue());
    assertEquals(expectedValue, new ToDoubleConversion("\t  \n").getDoubleValue());
  }

  @Test
  public void getDoubleValue_CannotParseValueAsDouble() {
    final String stringValue = "1das1cwc";
    final Double expectedValue = null;

    assertEquals(expectedValue, new ToDoubleConversion(stringValue).getDoubleValue());
  }

  @Test
  public void getDoubleValue_NullEmptyValue_WithDefault() {
    final String stringValue = null;
    final Double expectedValue = 3.141131;

    assertEquals(expectedValue, new ToDoubleConversion(stringValue).getDoubleValue(expectedValue));
    assertEquals(expectedValue, new ToDoubleConversion("").getDoubleValue(expectedValue));
    assertEquals(expectedValue, new ToDoubleConversion("\t  \n").getDoubleValue(expectedValue));
  }

  @Test
  public void getDoubleValue_CannotParseValueAsDouble_WithDefault() {
    final String stringValue = "1das1cwc";
    final Double expectedValue = 3.141131;

    assertEquals(expectedValue, new ToDoubleConversion(stringValue).getDoubleValue(expectedValue));
  }
}
