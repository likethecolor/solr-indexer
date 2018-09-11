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

import com.likethecolor.solr.indexer.handler.indexer.SolrjUpdateResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolrjUpdateResponseTest {
  @Test
  public void testDocSize() {
    final int docSize = 10;

    final SolrjUpdateResponse response = new SolrjUpdateResponse();
    response.setDocSize(docSize);

    assertEquals(docSize, response.getDocSize());
  }

  @Test
  public void testUpdateResponse() {
    final UpdateResponse updateResponse = new UpdateResponse();

    final SolrjUpdateResponse response = new SolrjUpdateResponse();
    response.setUpdateResponse(updateResponse);

    assertEquals(updateResponse, response.getUpdateResponse());
  }
}
