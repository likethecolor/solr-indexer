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

import com.likethecolor.solr.indexer.reader.CSVDataReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith (MockitoJUnitRunner.class)
public class CSVDataReaderTest {
  @Mock
  private CsvListReader csvListReader;
  @Mock
  private CSVDataReader dataReader;
  @Mock
  private CSVDataReader dataReaderThrowsFileNotFoundException;
  @Mock
  private CSVDataReader dataReaderReturnNullCsvListReader;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    List<Object> list = new ArrayList<>();
    Object[] array = new String[0];

    when(dataReader.getCsvListReader())
        .thenReturn(csvListReader);
    doCallRealMethod().when(dataReader).read(null);
    doCallRealMethod().when(dataReader).getHeader(anyBoolean());
    doCallRealMethod().when(dataReader).getLineNumber();
    doCallRealMethod().when(dataReader).close();

    when(dataReaderThrowsFileNotFoundException.getCsvListReader())
        .thenThrow(new FileNotFoundException());
    doCallRealMethod().when(dataReaderThrowsFileNotFoundException).getLineNumber();
    doCallRealMethod().when(dataReaderThrowsFileNotFoundException).close();

    when(dataReaderReturnNullCsvListReader.getCsvListReader())
        .thenReturn(null);
    doCallRealMethod().when(dataReaderReturnNullCsvListReader).close();


    when(csvListReader.read((CellProcessor[]) null))
        .thenReturn(list);

    doReturn(array).when(csvListReader).getHeader(anyBoolean());

    when(csvListReader.getLineNumber())
        .thenReturn(10);

    doNothing().when(csvListReader).close();
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
  public void testGetLineNumber() throws IOException {
    assertEquals(10, dataReader.getLineNumber());
  }

  @Test
  public void testGetLineNumber_ExceptionThrown_ReturnZero() throws IOException {
    assertEquals(0, dataReaderThrowsFileNotFoundException.getLineNumber());
  }

  @Test
  public void testClose() throws IOException {
    dataReader.close();
  }

  @Test
  public void testClose_CsvListReaderIsNull() throws IOException {
    dataReaderReturnNullCsvListReader.close();
  }

  @Test
  public void testClose_ExceptionThrown() throws IOException {
    dataReaderThrowsFileNotFoundException.close();
  }
}
