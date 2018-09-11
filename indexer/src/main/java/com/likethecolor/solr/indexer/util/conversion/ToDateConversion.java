/*
 * Copyright (c) 2018.  Dan Brown <dan@likethecolor.com>
 * <p><p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p><p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.likethecolor.solr.indexer.util.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDateConversion {
  private static final Logger LOGGER = LoggerFactory.getLogger(ToDateConversion.class);
  private String dateFormat;
  private String dateValue;

  public ToDateConversion(String dateValue, String dateFormat) {
    this.dateValue = dateValue;
    this.dateFormat = dateFormat;
  }

  /**
   * Parse the date string value that is in the given format.  The format pattern
   * is from {@link java.text.SimpleDateFormat}.
   *
   * This method if the format or value is null/empty or if the date string value
   * does not match the format.
   *
   * @return date built from the date string
   */
  public Date parseDate() {
    if(dateFormat == null || dateFormat.trim().length() == 0) {
      LOGGER.info("format value cannot be null/empty - returning null");
      return null;
    }
    if(dateValue == null || dateValue.trim().length() == 0) {
      LOGGER.info("date string value cannot be null/empty - returning null");
      return null;
    }

    Date returnDateValue;
    try {
      returnDateValue = new SimpleDateFormat(dateFormat.trim()).parse(dateValue.trim().toUpperCase());
    }
    catch(ParseException e) {
      throw new RuntimeException(String.format("cannot parse date value given the format: [format:%s] [date value:%s]", dateFormat, dateValue), e);
    }
    return returnDateValue;
  }
}
