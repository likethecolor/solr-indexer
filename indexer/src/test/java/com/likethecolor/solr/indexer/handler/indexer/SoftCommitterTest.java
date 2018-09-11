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
import com.likethecolor.solr.indexer.handler.indexer.SoftCommitter;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SoftCommitterTest {
  @Test
  public void testDoSoftCommit_NeverDoCommit() {
    CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    MockCloudSolrClient solr = new MockCloudSolrClient(builder);
    Configuration configuration = new Configuration();
    configuration.setSoftCommitFrequency(0L);
    SoftCommitter committer = new SoftCommitter(configuration, solr);

    for(int i = 0; i < 10; i++) {
      committer.doSoftCommit(i);
    }

    assertEquals(0, solr.commitCount);
  }

  @Test
  public void testDoSoftCommit_NeverDoCommit_NegativeFrequency() {
    CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    MockCloudSolrClient solr = new MockCloudSolrClient(builder);
    Configuration configuration = new Configuration();
    configuration.setSoftCommitFrequency(-1001L);
    SoftCommitter committer = new SoftCommitter(configuration, solr);

    for(int i = 0; i < 10; i++) {
      committer.doSoftCommit(i);
    }

    assertEquals(0, solr.commitCount);
  }

  @Test
  public void testDoSoftCommit_DoCommit_5() {
    CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    MockCloudSolrClient solr = new MockCloudSolrClient(builder);
    Configuration configuration = new Configuration();
    configuration.setSoftCommitFrequency(5L);
    SoftCommitter committer = new SoftCommitter(configuration, solr);

    for(int i = 0; i < 20; i++) {
      committer.doSoftCommit(i);
    }

    assertEquals(3, solr.commitCount);
  }

  @Test
  public void testDoSoftCommit_DoCommit_23() {
    CloudSolrClient.Builder builder = new MockBuilder(Arrays.asList("http://localhost:8983/solr", "http://example.com:8983/solr"));
    MockCloudSolrClient solr = new MockCloudSolrClient(builder);
    Configuration configuration = new Configuration();
    configuration.setSoftCommitFrequency(23L);
    SoftCommitter committer = new SoftCommitter(configuration, solr);

    for(int i = 0; i < 1000; i++) {
      committer.doSoftCommit(i);
    }

    assertEquals(43, solr.commitCount);
  }

  private class MockCloudSolrClient extends CloudSolrClient {
    long commitCount;

    public MockCloudSolrClient(CloudSolrClient.Builder builder) {
      super(builder);
      commitCount = 0;
    }

    public UpdateResponse commit(boolean waitFlush, boolean waitSearcher, boolean softCommit) {
      if(!waitFlush && !waitSearcher && softCommit) {
        commitCount++;
      }
      return null;
    }
  }

  class MockBuilder extends CloudSolrClient.Builder {
    public MockBuilder(List<String> solrUrls) {
      super(solrUrls);
      zkHosts = null;
    }
  }
}
