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

import com.likethecolor.solr.indexer.util.conversion.ToLongConversion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToLongConversionTest {

  @Test
  public void getLongValue() {
    final String stringValue = "14159089078";
    final Long expectedValue = Long.parseLong(stringValue);

    assertEquals(expectedValue, new ToLongConversion(stringValue).getLongValue());
  }

  @Test
  public void getLongValue_NullEmptyValue() {
    final String stringValue = null;
    final Long expectedValue = null;

    assertEquals(expectedValue, new ToLongConversion(stringValue).getLongValue());
    assertEquals(expectedValue, new ToLongConversion("").getLongValue());
    assertEquals(expectedValue, new ToLongConversion("\t  \n").getLongValue());
  }

  @Test
  public void getLongValue_CannotParseValueAsLong() {
    final String stringValue = "1das1cwc";
    final Long expectedValue = null;

    assertEquals(expectedValue, new ToLongConversion(stringValue).getLongValue());
  }

  @Test
  public void getLongValue_NullEmptyValue_WithDefault() {
    final String stringValue = null;
    final Long expectedValue = 3141131238327592L;

    assertEquals(expectedValue, new ToLongConversion(stringValue).getLongValue(expectedValue));
    assertEquals(expectedValue, new ToLongConversion("").getLongValue(expectedValue));
    assertEquals(expectedValue, new ToLongConversion("\t  \n").getLongValue(expectedValue));
  }

  @Test
  public void getLongValue_CannotParseValueAsLong_WithDefault() {
    final String stringValue = "1das1cwc";
    final Long expectedValue = 31411314523L;

    assertEquals(expectedValue, new ToLongConversion(stringValue).getLongValue(expectedValue));
  }
}
