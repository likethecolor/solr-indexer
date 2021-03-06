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
import org.supercsv.prefs.CsvPreference;

public class CsvPreferencesBuilder {
  private Configuration configuration;

  public CsvPreferencesBuilder(final Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * per docs of {@link org.supercsv.prefs.CsvPreference} end of line is used
   * for writing but this app doesn't write so pick one
   * 
   * @return {@link CsvPreferencesBuilder}
   */
  public CsvPreference getCsvPreference() {
    return new CsvPreference.Builder(configuration.getCsvQuoteCharacter(),
        configuration.getCsvDelimiter(),
        CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols()).build();
  }
}
