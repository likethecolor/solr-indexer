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
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class IndexService implements Callable<SolrjUpdateResponse> {
  private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
  private CloudSolrClient cloudSolrServer;
  private Collection<SolrInputDocument> docs;
  private Configuration configuration;

  public IndexService(Configuration configuration, final CloudSolrClient cloudSolrServer, final Collection<SolrInputDocument> docs) {
    this.cloudSolrServer = cloudSolrServer;
    this.configuration = configuration;
    this.docs = new ArrayList<>(docs);
  }

  public SolrjUpdateResponse call() throws InterruptedException {
    SolrjUpdateResponse response = makeTheCall();
    if(response.isUpdateSucceeded()) {
      response.setNumberOfAttempts(1);
      return response;
    }

    // start at 1 since we already tried once above
    for(int i = 1; i < configuration.getRetryCount(); i++) {
      LOGGER.info("retrying batch - attempt #{}", i);
      response = makeTheCall();
      // +1 since one attempt was made above
      response.setNumberOfAttempts(i + 1);
      if(response.isUpdateSucceeded()) {
        LOGGER.info("retry of #{} was a success", i);
        break;
      }
      try {
        Thread.sleep(configuration.getSleepMillisBetweenRetries());
      }
      catch(InterruptedException e) {
        LOGGER.warn("retry thread interrupted", e);
      }
    }
    if(!response.isUpdateSucceeded()) {
      LOGGER.error("after {} attempts retry failed", response.getNumberOfAttempts());
      throw new InterruptedException("too many attempts with too many failures");
    }
    return response;
  }

  private SolrjUpdateResponse makeTheCall() {
    final SolrjUpdateResponse response = new SolrjUpdateResponse();
    response.setDocSize(docs.size());
    UpdateResponse updateResponse = new UpdateResponse();
    try {
      updateResponse = cloudSolrServer.add(docs);
      response.setUpdateSucceeded(true);
    }
    catch(Exception e) {
      LOGGER.error("could not add documents to the solr server", e);
      response.setUpdateSucceeded(false);
    }
    response.setUpdateResponse(updateResponse);
    return response;
  }
}
