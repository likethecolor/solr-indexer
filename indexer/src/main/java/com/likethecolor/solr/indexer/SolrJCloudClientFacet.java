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

package com.likethecolor.solr.indexer;

import com.likethecolor.solr.indexer.util.conversion.ToTimeConversion;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SolrJCloudClientFacet {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrJCloudClientFacet.class);
  private static final boolean SOFT_COMMIT_WAIT_FLUSH = false;
  private static final boolean SOFT_COMMIT_WAIT_SEARCHER = false;
  private static final boolean SOFT_COMMIT_SOFT_COMMIT = true;

  /**
   * Call {@link org.apache.solr.client.solrj.impl.CloudSolrClient#commit()} to
   * perform a soft commit.
   *
   * The amount of time used for the commit call is logged.
   *
   * @param solr cloud server
   */
  public static void softCommit(final CloudSolrClient solr) {
    final long start = System.currentTimeMillis();
    LOGGER.info(getStartOfProcessMessage(String.format("sending soft commit call " +
                                                       "[waitFlush = %s, waitSearcher = %s, softCommit = %s]",
        SOFT_COMMIT_WAIT_FLUSH, SOFT_COMMIT_WAIT_SEARCHER, SOFT_COMMIT_SOFT_COMMIT)));
    try {
      solr.commit(SOFT_COMMIT_WAIT_FLUSH, SOFT_COMMIT_WAIT_SEARCHER, SOFT_COMMIT_SOFT_COMMIT);
    }
    catch(SolrServerException e) {
      LOGGER.error("could not perform soft commit", e);
    }
    catch(IOException e) {
      LOGGER.error("could not perform soft commit", e);
    }
    LOGGER.info(getEndOfProcessMessage("soft commit call", start, System.currentTimeMillis()));
  }

  /**
   * Call {@link org.apache.solr.client.solrj.impl.CloudSolrClient#commit()} then
   * {@link #shutdown(org.apache.solr.client.solrj.impl.CloudSolrClient)}.
   * The amount of time used for the commit call is logged.
   *
   * @param solr cloud server
   *
   * @throws SolrServerException when there is an exception with the solr server
   * @throws IOException when there is an exception with IO
   */
  public static void commitShutdown(final CloudSolrClient solr) throws IOException, SolrServerException {
    final long start = System.currentTimeMillis();
    LOGGER.info(getStartOfProcessMessage("sending commit call"));
    solr.commit();
    LOGGER.info(getEndOfProcessMessage("commit call", start, System.currentTimeMillis()));
    shutdown(solr);
  }

  /**
   * Call {@link org.apache.solr.client.solrj.impl.CloudSolrClient#optimize()}.
   * The amount of time used for the optimize call is logged.
   *
   * @param solr cloud server
   * @param optimize true to optimize
   *
   * @throws SolrServerException when there is an exception with the solr server
   * @throws IOException when there is an exception with IO
   */
  public static void optimize(final CloudSolrClient solr, final boolean optimize) throws SolrServerException, IOException {
    if(optimize) {
      final String processName = "sending optimize call";
      long start = System.currentTimeMillis();
      LOGGER.info(getStartOfProcessMessage(processName));
      solr.optimize();
      LOGGER.info(getEndOfProcessMessage(processName, start, System.currentTimeMillis()));
    }
  }

  /**
   * Call {@link org.apache.solr.client.solrj.impl.CloudSolrClient#close()}
   * The amount of time used for the shutdown call is logged.
   *
   * @param solr cloud server
   */
  public static void shutdown(final CloudSolrClient solr) {
    final String processName = "releasing cloud server client resources";
    final long start = System.currentTimeMillis();
    LOGGER.info(getStartOfProcessMessage(processName));
    try {
      solr.close();
    }
    catch(IOException e) {
      LOGGER.error("error closing CloudSolrClient", e);
    }
    LOGGER.info(getEndOfProcessMessage(processName, start, System.currentTimeMillis()));
  }

  /**
   * Return a string formatted using the given values.
   * <p/>
   * start: [what]
   *
   * @param whatStarted string indicating what start
   *
   * @return string formatted using the given values
   */
  private static String getStartOfProcessMessage(final String whatStarted) {
    return String.format("start: %s", whatStarted);
  }

  /**
   * Return a string formatted using the given values.
   * end: [what] [(endTimeInMs-startTimeInMs) ms = (endTimeInMs-startTimeInMs/1000) seconds]
   *
   * @param what string indicating what completed
   * @param startTimeInMs start time of the process  in milliseconds
   * @param endTimeInMs end time of the process in milliseconds
   *
   * @return string formatted using the given values
   */
  private static String getEndOfProcessMessage(final String what, final long startTimeInMs, final long endTimeInMs) {
    final long durationInMs = endTimeInMs - startTimeInMs;
    return String.format("end: %s [%s]", what, new ToTimeConversion(durationInMs).getMinutesSecondsMillisecondsFromMilliseconds());
  }
}
