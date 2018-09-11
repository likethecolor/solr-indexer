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

import com.likethecolor.solr.indexer.util.conversion.ToCharacterConversion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToCharacterConversionTest {
  @Test
  public void getCharValue() {
    final String stringValue = "a";
    final char expectedValue = 'a';

    assertEquals(expectedValue, new ToCharacterConversion(stringValue).getCharValue());
  }

  @Test
  public void getCharValue_NullEmptyValue() {
    final String stringValue = null;
    final char expectedValue = '\0';

    assertEquals(expectedValue, new ToCharacterConversion(stringValue).getCharValue());
    assertEquals(expectedValue, new ToCharacterConversion("").getCharValue());
    assertEquals(expectedValue, new ToCharacterConversion("\t  \n").getCharValue());
  }

  @Test
  public void getCharValue_CannotParseValueAsChar() {
    final String stringValue = "";
    final char expectedValue = '\0';

    assertEquals(expectedValue, new ToCharacterConversion(stringValue).getCharValue());
  }

  @Test
  public void getCharValue_NullEmptyValue_WithDefault() {
    final String stringValue = null;
    final char expectedValue = 'x';

    assertEquals(expectedValue, new ToCharacterConversion(stringValue).getCharValue(expectedValue));
    assertEquals(expectedValue, new ToCharacterConversion("").getCharValue(expectedValue));
    assertEquals(expectedValue, new ToCharacterConversion("\t  \n").getCharValue(expectedValue));
  }

  @Test
  public void getCharValue_CannotParseValueAsChar_WithDefault() {
    final String stringValue = "";
    final char expectedValue = 'z';

    assertEquals(expectedValue, new ToCharacterConversion(stringValue).getCharValue(expectedValue));
  }
}
