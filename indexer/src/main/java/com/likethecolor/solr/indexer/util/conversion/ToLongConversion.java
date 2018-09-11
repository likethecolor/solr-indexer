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

public class ToLongConversion {
  private String valueToBeConverted;

  public ToLongConversion(String valueToBeConverted) {
    this.valueToBeConverted = valueToBeConverted;
  }

  public Long getLongValue() {
    return getLongValue(null);
  }

  public Long getLongValue(final Long defaultValue) {
    Long longValue = defaultValue;
    if(valueToBeConverted != null && valueToBeConverted.trim().length() > 0) {
      try {
        longValue = Long.parseLong(valueToBeConverted.trim());
      }
      catch(NumberFormatException ignore) {
      }
    }
    return longValue;
  }
}
