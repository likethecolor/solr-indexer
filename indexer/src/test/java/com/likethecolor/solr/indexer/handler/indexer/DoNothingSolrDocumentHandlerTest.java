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

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.handler.indexer.DoNothingSolrDocumentHandler;
import com.likethecolor.solr.indexer.handler.indexer.DocumentHandler;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class DoNothingSolrDocumentHandlerTest {
  @Test
  public void testHandle_Returns0() throws InterruptedException, IOException, ExecutionException, SolrServerException {
    Configuration configuration = new Configuration();
    DocumentHandler handler = new DoNothingSolrDocumentHandler(configuration, null);

    assertEquals(0, handler.handle(null));
  }
}
