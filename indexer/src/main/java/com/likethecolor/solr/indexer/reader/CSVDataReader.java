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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CSVDataReader implements DataReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataReader.class);
  private String pathToDataFile;
  private CsvPreference csvPreference;
  private ICsvListReader csvListReader;

  public CSVDataReader(String pathToDataFile, CsvPreference csvPreference) {
    this.pathToDataFile = pathToDataFile;
    this.csvPreference = csvPreference;
  }

  /**
   * From: {@link org.supercsv.io.ICsvListReader}
   * Reads a row of a CSV file and returns a List of Objects containing each column. The data can be further processed
   * by cell processors (each element in the processors array corresponds with a CSV column). A <tt>null</tt> entry in
   * the processors array indicates no further processing is required (the unprocessed String value will be added to
   * the List). Prior to version 2.0.0 this method returned a List of Strings.
   *
   * @param cellProcessors an array of CellProcessors used to further process data before it is added to the List (each element
   * in the processors array corresponds with a CSV column - the number of processors should match the
   * number of columns). A <tt>null</tt> entry indicates no further processing is required (the unprocessed
   * String value will be added to the List).
   *
   * @return the List of columns, or null if EOF
   *
   * @throws IOException if an I/O error occurred
   * @throws NullPointerException if processors is null
   * @throws org.supercsv.exception.SuperCsvConstraintViolationException if a CellProcessor constraint failed
   * @throws org.supercsv.exception.SuperCsvException if there was a general exception while reading/processing
   * @since 1.0
   */
  @Override
  public List<Object> read(CellProcessor[] cellProcessors) throws IOException {
    return getCsvListReader().read(cellProcessors);
  }

  /**
   * From: {@link org.supercsv.io.ICsvListReader}
   * This method is used to get an optional header of the CSV file and move the file cursor to the first row
   * containing data (the second row from the top). The header can subsequently be used as the
   * <code>nameMapping</code> array for read operations.
   *
   * @param firstLineCheck if true, ensures that this method is only called when reading the first line (as that's where the
   * header is meant to be)
   *
   * @return the array of header fields, or null if EOF is encountered
   *
   * @throws IOException if an I/O exception occurs
   * @throws org.supercsv.exception.SuperCsvException if firstLineCheck == true and it's not the first line being read
   * @since 1.0
   */
  @Override
  public Object[] getHeader(boolean firstLineCheck) throws IOException {
    ICsvListReader reader = getCsvListReader();
    return reader.getHeader(firstLineCheck);
  }

  /**
   * From: {@link org.supercsv.io.ICsvListReader}
   * Gets the current position in the file. The first line of the file is line number 1.
   *
   * @since 1.0
   */
  @Override
  public int getLineNumber() {
    try {
      return getCsvListReader().getLineNumber();
    }
    catch(FileNotFoundException e) {
      LOGGER.warn("could not get line number from reader due to exception - returning 0", e);
    }
    return 0;
  }

  /**
   * From: {@link java.io.Closeable}
   * Closes this stream and releases any system resources associated
   * with it. If the stream is already closed then invoking this
   * method has no effect.
   *
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void close() throws IOException {
    try {
      ICsvListReader reader = getCsvListReader();
      if(reader != null) {
        reader.close();
      }
    }
    catch(FileNotFoundException e) {
      LOGGER.info("could not close reader because reader's file could not be found", e);
    }
    csvListReader = null;
  }

  protected ICsvListReader getCsvListReader() throws FileNotFoundException {
    if(csvListReader == null) {
      Reader reader = getFileReader(pathToDataFile);
      csvListReader = new CsvListReader(reader, csvPreference);
    }
    return csvListReader;
  }

  private Reader getFileReader(String pathToDataFile) throws FileNotFoundException {
    return new FileReader(pathToDataFile);
  }
}
