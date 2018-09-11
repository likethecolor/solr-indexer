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

import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.json.JSONParser;
import com.likethecolor.solr.indexer.json.MapToListValuesBuilder;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONDataReader implements DataReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(JSONDataReader.class);
  private BufferedReader bufferedReader;
  private Object[] firstRow;
  private String currentLine;
  private JSONParser jsonParser;
  private int lineNumber;
  private MapToListValuesBuilder mapToListValuesBuilder;
  private String pathToDataFile;

  public JSONDataReader(String pathToDataFile, Map<String, FieldDefinition> fieldDefinitionMap, JSONParser jsonParser) {
    this.pathToDataFile = pathToDataFile;
    this.jsonParser = jsonParser;
    lineNumber = 0;
    mapToListValuesBuilder = new MapToListValuesBuilder(fieldDefinitionMap);
  }

  @Override
  public List<Object> read(final CellProcessor[] cellProcessors) throws IOException {
    // cellProcessors ignored on purpose - here to meet interface obligation
    // TODO: is there another way to have one interface for both csv and json?
    String line = readLine();
    if(line == null) {
      return null;
    }
    if(firstRow == null && lineNumber == 1) {
      firstRow = getDataAsArray(line);
    }
    return getDataAsList(line);
  }

  @Override
  public Object[] getHeader(final boolean firstLineCheck) throws IOException {
    String line = readLine();
    if(firstRow == null && lineNumber == 1) {
      firstRow = getDataAsArray(line);
    }
    return firstRow;
  }

  @Override
  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public void close() throws IOException {
    if(bufferedReader != null) {
      bufferedReader.close();
    }
    bufferedReader = null;
  }

  String readLine() throws IOException {
    BufferedReader reader = getBufferedReader();
    if(reader != null) {
      String line = reader.readLine();
      if(line == null) {
        return null;
      }
      currentLine = line;
      lineNumber++;
    }
    return currentLine;
  }

  BufferedReader getBufferedReader() throws FileNotFoundException {
    if(bufferedReader == null) {
      bufferedReader = new BufferedReader(new FileReader(pathToDataFile));
    }
    return bufferedReader;
  }

  private List<Object> getDataAsList(String rowOfData) throws IOException {
    return getMapToListValuesBuilder().build(getDataMap(rowOfData));
  }

  private Object[] getDataAsArray(String rowOfData) throws IOException {
    List<Object> list = getDataAsList(rowOfData);
    return list.toArray(new Object[list.size()]);
  }

  private Map<String, String> getDataMap(String rowOfData) throws IOException {
    Map<String, String> map;
    try {
      map = getJsonParser().parse(rowOfData);
    }
    catch(JSONException e) {
      LOGGER.error("could not parse row of data - returning empty map", e);
      throw new IOException(e);
    }
    return map;
  }

  MapToListValuesBuilder getMapToListValuesBuilder() {
    return mapToListValuesBuilder;
  }

  JSONParser getJsonParser() {
    return jsonParser;
  }
}
