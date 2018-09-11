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

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.dynamic.DynamicClass;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public class UseThisToTestDynamicCode extends DynamicClass {
  public UseThisToTestDynamicCode(Configuration configuration, SolrInputDocument solrInputDocument) {
    super(configuration, solrInputDocument);
  }

  @Override
  public void populate(List<String> setFieldAndFieldNames) {
    // The first argument is the name of the field whose value should be changed
    // by this method by DynamicFieldProcessor.  Fields in the solr input
    // document will likely look like Map("set", value).  This field was added
    // by the system so there's no need to call getFieldValue.
    //
    String fieldName = setFieldAndFieldNames.get(0);

    // DynamicFieldProcessor passes the field value found in in setFieldAndFieldNames.
    // The getFieldValue  
    String field0 = getFieldValue(setFieldAndFieldNames.get(1));
    String field1 = getFieldValue(setFieldAndFieldNames.get(2));
    String field2 = getFieldValue(setFieldAndFieldNames.get(3));

    setFieldValue(fieldName, field0 + field1 + field2);
  }
}
