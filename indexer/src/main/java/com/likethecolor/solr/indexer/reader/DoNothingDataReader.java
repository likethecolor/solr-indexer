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

import com.likethecolor.solr.indexer.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoNothingDataReader implements DataReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoNothingDataReader.class);

  public DoNothingDataReader() {
  }

  @Override
  public List<Object> read(CellProcessor[] cellProcessors) throws IOException {
    logDoNothingInformation();
    return new ArrayList<>();
  }

  @Override
  public Object[] getHeader(boolean firstLineCheck) throws IOException {
    logDoNothingInformation();
    return new Object[0];
  }

  @Override
  public int getLineNumber() {
    logDoNothingInformation();
    return 0;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public void close() throws IOException {
    logDoNothingInformation();
  }

  private void logDoNothingInformation() {
    LOGGER.warn("this is a do nothing handler");
    LOGGER.error("something went wrong");
    LOGGER.error("could it be that there is no data type (--{}) or no path to a data file (--{})",
        Constants.DATA_TYPE_OPTION, Constants.PATH_TO_DATA_FILE_OPTION);
  }
}
