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
package com.likethecolor.solr.indexer.handler;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.handler.CsvPreferencesBuilder;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

import static org.junit.Assert.assertEquals;

public class CsvPreferencesBuilderTest {
  @Test
  public void testGetCsvPreference_Defaults() {
    final char quoteCharacter = '"';
    final char delimiterCharacter = ',';
    final String endOfLineString = "\r\n";

    final Configuration configuration = new Configuration();

    final CsvPreferencesBuilder builder = new CsvPreferencesBuilder(configuration);

    final CsvPreference preferences = builder.getCsvPreference();

    assertEquals(quoteCharacter, preferences.getQuoteChar());
    assertEquals(delimiterCharacter, preferences.getDelimiterChar());
    assertEquals(endOfLineString, preferences.getEndOfLineSymbols());
  }

  @Test
  public void testGetCsvPreference() {
    final char quoteCharacter = '\u0000';
    final char delimiterCharacter = '\u0002';
    final String endOfLineString = CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols();

    final Configuration configuration = new Configuration();
    configuration.setCsvDelimiter(delimiterCharacter);
    configuration.setCsvQuoteCharacter(quoteCharacter);

    final CsvPreferencesBuilder builder = new CsvPreferencesBuilder(configuration);

    final CsvPreference preferences = builder.getCsvPreference();

    assertEquals(quoteCharacter, preferences.getQuoteChar());
    assertEquals(delimiterCharacter, preferences.getDelimiterChar());
    assertEquals(endOfLineString, preferences.getEndOfLineSymbols());
  }
}
