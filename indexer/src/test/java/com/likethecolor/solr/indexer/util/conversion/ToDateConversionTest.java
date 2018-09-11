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

import com.likethecolor.solr.indexer.util.conversion.ToDateConversion;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ToDateConversionTest {
  @Test
  public void testParseDate() throws ParseException {
    final String format = "MM/dd/yyyy hh:mm:ssa";
    final String dateString = "01/22/3333 01:02:03am";
    final Date expectedDate = new SimpleDateFormat(format).parse(dateString.toUpperCase());

    Date actualDate = new ToDateConversion(dateString, format).parseDate();

    assertEquals(expectedDate, actualDate);

    actualDate = new ToDateConversion("  " + dateString, "\t " + format + "\r").parseDate();

    assertEquals(expectedDate, actualDate);
  }

  @Test(expected = RuntimeException.class)
  public void testParseDate_FormatDoesNotMatchValue() throws ParseException {
    final String format = "MM/dd/yyyy hh:mm:ssa";
    final String dateString = "2014-02-03";

    new ToDateConversion(dateString, format).parseDate();
  }

  @Test
  public void testParseDate_NullEmptyFormatString() throws ParseException {
    final String dateString = "2014-02-03";

    Date actualDate = new ToDateConversion(dateString, null).parseDate();

    assertNull(actualDate);
  }

  @Test
  public void testParseDate_EmptyFormatString() throws ParseException {
    final String dateString = "2014-02-03";

    Date actualDate = new ToDateConversion(dateString, "").parseDate();

    assertNull(actualDate);
  }

  @Test
  public void testParseDate_EmptyWhiteSpaceFormatString() throws ParseException {
    final String dateString = "2014-02-03";

    Date actualDate = new ToDateConversion(dateString, "\t  \r").parseDate();

    assertNull(actualDate);
  }

  @Test
  public void testParseDate_NullEmptyValue() throws ParseException {
    final String format = "MM/dd/yyyy hh:mm:ssa";

    Date actualDate = new ToDateConversion(null, format).parseDate();

    assertNull(actualDate);
  }

  @Test
  public void testParseDate_EmptyValue() throws ParseException {
    final String format = "MM/dd/yyyy hh:mm:ssa";

    Date actualDate = new ToDateConversion("", format).parseDate();

    assertNull(actualDate);
  }

  @Test
  public void testParseDate_EmptyWhiteSpaceValue() throws ParseException {
    final String format = "MM/dd/yyyy hh:mm:ssa";

    Date actualDate = new ToDateConversion("\t  \r", format).parseDate();

    assertNull(actualDate);
  }
}
