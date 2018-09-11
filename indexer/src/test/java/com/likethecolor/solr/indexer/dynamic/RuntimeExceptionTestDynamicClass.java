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

package com.likethecolor.solr.indexer.dynamic;

import com.likethecolor.solr.indexer.dynamic.DynamicClass;
import com.likethecolor.solr.indexer.configuration.Configuration;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public class RuntimeExceptionTestDynamicClass extends DynamicClass {
  public RuntimeExceptionTestDynamicClass(Configuration configuration, SolrInputDocument solrInputDocument) {
    super(configuration, solrInputDocument);
  }

  @Override
  public void populate(List<String> setFieldAndFieldNames) {
    throw new RuntimeException("this is intentional");
  }
}
