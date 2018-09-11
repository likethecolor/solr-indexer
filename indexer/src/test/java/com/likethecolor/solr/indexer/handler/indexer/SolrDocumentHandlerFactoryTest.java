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

package com.likethecolor.solr.indexer.handler.indexer;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.handler.indexer.DoNothingSolrDocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.DocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentHandlerFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SolrDocumentHandlerFactoryTest {
  private static final String PATH_TO_DATA_FILE = "/var/foo/file.data";

  @Test
  public void testGetHandler_DataTypeIsCSV() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_CSV);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    DocumentHandler handler = SolrDocumentHandlerFactory.getInstance().getHandler(configuration);

    assertTrue(handler.getClass().getSimpleName().equals(SolrDocumentHandler.class.getSimpleName()));
  }

  @Test
  public void testGetHandler_DataTypeIsJSON() {
    Configuration configuration = new Configuration();
    configuration.setDataType(Constants.DATA_TYPE_JSON);
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    DocumentHandler handler = SolrDocumentHandlerFactory.getInstance().getHandler(configuration);

    assertTrue(handler.getClass().getSimpleName().equals(SolrDocumentHandler.class.getSimpleName()));
  }

  @Test
  public void testGetHandler_NoDataType() {
    Configuration configuration = new Configuration();
    configuration.setPathToDataFile(PATH_TO_DATA_FILE);

    DocumentHandler handler = SolrDocumentHandlerFactory.getInstance().getHandler(configuration);

    assertTrue(handler.getClass().getSimpleName().equals(DoNothingSolrDocumentHandler.class.getSimpleName()));
  }
}
