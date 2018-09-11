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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.util.conversion.ToListConversion;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ToListConversionTest {
  @Test
  public void testToList() {
    final List<String> expectedList = new ArrayList<>();
    expectedList.add("index_type");
    expectedList.add("site_id");
    expectedList.add("site_code");
    expectedList.add("site_name");

    final String value = new StringBuilder()
        .append(expectedList.get(0))
        .append(Constants.LIST_DELIMITER)
        .append(expectedList.get(1))
        .append(Constants.LIST_DELIMITER)
        .append(expectedList.get(2))
        .append(Constants.LIST_DELIMITER)
        .append(expectedList.get(3))
        .toString();

    final List<String> actualList = new ToListConversion(value).toList();

    assertEquals(expectedList, actualList);
  }

  @Test
  public void testToList_NoDelimiter() {
    final List<String> expectedList = new ArrayList<>();
    expectedList.add("index_type");

    final String value = expectedList.get(0);

    final List<String> actualList = new ToListConversion(value).toList();

    assertEquals(expectedList, actualList);
  }

  @Test
  public void testToList_NullValue() {
    final List<String> expectedList = new ArrayList<>();

    List<String> actualList = new ToListConversion(null).toList();

    assertEquals(expectedList, actualList);
  }

  @Test
  public void testToList_EmptyValue() {
    final List<String> expectedList = new ArrayList<>();

    List<String> actualList = new ToListConversion("").toList();

    assertEquals(expectedList, actualList);
  }

  @Test
  public void testToList_EmptyWhiteSpaceValue() {
    final List<String> expectedList = new ArrayList<>();

    List<String> actualList = new ToListConversion("\t  ").toList();

    assertEquals(expectedList, actualList);
  }
}
