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

import com.likethecolor.solr.indexer.util.conversion.ToIntegerConversion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToIntegerConversionTest {

  @Test
  public void getIntegerValue() {
    final String stringValue = "14159";
    final Integer expectedValue = Integer.parseInt(stringValue);

    assertEquals(expectedValue, new ToIntegerConversion(stringValue).getIntegerValue());
  }

  @Test
  public void getIntegerValue_NullEmptyValue() {
    final String stringValue = null;
    final Integer expectedValue = null;

    assertEquals(expectedValue, new ToIntegerConversion(stringValue).getIntegerValue());
    assertEquals(expectedValue, new ToIntegerConversion("").getIntegerValue());
    assertEquals(expectedValue, new ToIntegerConversion("\t  \n").getIntegerValue());
  }

  @Test
  public void getIntegerValue_CannotParseValueAsInteger() {
    final String stringValue = "1das1cwc";
    final Integer expectedValue = null;

    assertEquals(expectedValue, new ToIntegerConversion(stringValue).getIntegerValue());
  }

  @Test
  public void getIntegerValue_NullEmptyValue_WithDefault() {
    final String stringValue = null;
    final Integer expectedValue = 3141131;

    assertEquals(expectedValue, new ToIntegerConversion(stringValue).getIntegerValue(expectedValue));
    assertEquals(expectedValue, new ToIntegerConversion("").getIntegerValue(expectedValue));
    assertEquals(expectedValue, new ToIntegerConversion("\t  \n").getIntegerValue(expectedValue));
  }

  @Test
  public void getIntegerValue_CannotParseValueAsInteger_WithDefault() {
    final String stringValue = "1das1cwc";
    final Integer expectedValue = 3141131;

    assertEquals(expectedValue, new ToIntegerConversion(stringValue).getIntegerValue(expectedValue));
  }
}
