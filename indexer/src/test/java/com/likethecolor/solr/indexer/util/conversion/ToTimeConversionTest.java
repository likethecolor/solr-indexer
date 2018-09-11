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

import com.likethecolor.solr.indexer.util.conversion.ToTimeConversion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToTimeConversionTest {
  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_NegativeMilliseconds() {
    long millis = -1L;
    String expectedTime = "0 min, 00 sec, -01 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }

  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_NegativeMillisecondsWithThreePlaces() {
    long millis = -100L;
    String expectedTime = "0 min, 00 sec, -100 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }

  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_ZeroMilliseconds() {
    long millis = 0L;
    String expectedTime = "0 min, 00 sec, 000 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }

  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_TenMilliseconds() {
    long millis = 10L;
    String expectedTime = "0 min, 00 sec, 010 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }

  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_Minutes() {
    long millis = 135956L;
    String expectedTime = "2 min, 15 sec, 956 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }

  @Test
  public void testGetMinutesSecondsMillisecondsFromMilliseconds_Days() {
    long millis = (10 * 24 * 60 * 60 * 1000) + 1;
    String expectedTime = "14400 min, 00 sec, 001 ms";

    String actualTime = new ToTimeConversion(millis).getMinutesSecondsMillisecondsFromMilliseconds();

    assertEquals(expectedTime, actualTime);
  }
}
