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
package com.likethecolor.solr.indexer.reader;

import com.likethecolor.solr.indexer.reader.DataReader;
import com.likethecolor.solr.indexer.reader.DoNothingDataReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DoNothingDataReaderTest {
  private DataReader dataReader;

  @Before
  public void setUp() {
    dataReader = new DoNothingDataReader();
  }

  @Test
  public void testRead_ReturnsEmptyList() throws IOException {
    assertEquals(0, dataReader.read(null).size());
  }

  @Test
  public void testGetHeader_ReturnsEmptyArray() throws IOException {
    assertEquals(0, dataReader.getHeader(true).length);
  }

  @Test
  public void testGetLineNumber_ReturnsZero() throws IOException {
    assertEquals(0, dataReader.getLineNumber());
  }
}
