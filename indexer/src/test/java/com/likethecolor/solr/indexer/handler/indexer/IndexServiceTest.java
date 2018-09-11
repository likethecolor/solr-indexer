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
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IndexServiceTest {
  @Test
  public void testCall() throws MalformedURLException, InterruptedException {
    final Configuration configuration = new Configuration();
    configuration.setRetryCount(0);
    final int numberOfDocs = 10;
    final CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    final MockCloudSolrClient cloudSolrServer = new MockCloudSolrClient(builder);

    final List<SolrInputDocument> docs = getListOfDocs(numberOfDocs);

    final IndexService service = new IndexService(configuration, cloudSolrServer, docs);

    final SolrjUpdateResponse response = service.call();

    assertEquals(numberOfDocs, response.getDocSize());
    assertEquals(1, response.getNumberOfAttempts());

    assertEquals(cloudSolrServer.docs, docs);
  }

  @Test(expected = InterruptedException.class)
  public void testCall_MultipleAttempts() throws MalformedURLException, InterruptedException {
    final int numberOfAttempts = 5;
    final Configuration configuration = new Configuration();
    configuration.setRetryCount(numberOfAttempts);
    configuration.setSleepMillisBetweenRetries(0L);
    final int numberOfDocs = 10;
    final CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    final MockCloudSolrClient cloudSolrServer = new MockCloudSolrClient(builder);
    cloudSolrServer.throwException = true;

    final List<SolrInputDocument> docs = getListOfDocs(numberOfDocs);

    final IndexService service = new IndexService(configuration, cloudSolrServer, docs);

    final SolrjUpdateResponse response = service.call();

    assertEquals(numberOfDocs, response.getDocSize());
    assertEquals(numberOfAttempts, response.getNumberOfAttempts());

    assertEquals(cloudSolrServer.docs, docs);
  }

  @Test(expected = InterruptedException.class)
  public void testCall_ThrowException() throws MalformedURLException, InterruptedException {
    final Configuration configuration = new Configuration();
    configuration.setRetryCount(0);
    final int numberOfDocs = 10;
    final CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    final MockCloudSolrClient cloudSolrServer = new MockCloudSolrClient(builder);
    cloudSolrServer.throwException = true;

    final List<SolrInputDocument> docs = getListOfDocs(numberOfDocs);

    final IndexService service = new IndexService(configuration, cloudSolrServer, docs);

    final SolrjUpdateResponse response = service.call();

    assertEquals(numberOfDocs, response.getDocSize());

    assertEquals(cloudSolrServer.docs, docs);
  }

  private List<SolrInputDocument> getListOfDocs(int numberOfDocs) {
    final List<SolrInputDocument> docs = new ArrayList<>(numberOfDocs);
    for(int i = 0; i < numberOfDocs; i++) {
      SolrInputDocument doc = new SolrInputDocument();
      doc.addField("field" + i, i);
      docs.add(doc);
    }
    return docs;
  }

  class MockCloudSolrClient extends CloudSolrClient {
    Collection<SolrInputDocument> docs;
    boolean throwException;

    public MockCloudSolrClient(Builder builder) {
      super(builder);
    }

    public UpdateResponse add(final Collection<SolrInputDocument> docs) throws SolrServerException, IOException {
      this.docs = docs;
      if(throwException) {
        throw new IOException("this exception is expected as part of the test");
      }
      return new UpdateResponse();
    }
  }

  class MockBuilder extends CloudSolrClient.Builder {
    public MockBuilder(List<String> solrUrls) {
      super(solrUrls);
      zkHosts = null;
    }
  }
}
