/**
 * Copyright 2018 Dan Brown <dan@likethecolor.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likethecolor.solr.indexer.dynamic;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.dynamic.SampleDynamicClass;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SampleDynamicClassTest {
  @Test
  public void testProcess() {
    Configuration configuration = new Configuration();
    String fieldName = "full-name";
    String fieldName0 = "prefix";
    String fieldName1 = "name";
    String fieldName2 = "suffix";
    String value0 = "Mr.";
    String value1 = "John Smith";
    String value2 = "Senior";

    List<String> setFieldAndFieldNames = new ArrayList<>();
    setFieldAndFieldNames.add(fieldName);
    setFieldAndFieldNames.add(fieldName0);
    setFieldAndFieldNames.add(fieldName1);
    setFieldAndFieldNames.add(fieldName2);
    
    SolrInputDocument solrInputDocument = new SolrInputDocument();
    Map<String, Object> map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value0);
    solrInputDocument.setField(fieldName0, map);
    map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value1);
    solrInputDocument.setField(fieldName1, map);
    map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value2);
    solrInputDocument.setField(fieldName2, map);

    SampleDynamicClass dynamicClass = new SampleDynamicClass(configuration, solrInputDocument);
    dynamicClass.populate(setFieldAndFieldNames);

    assertEquals(value0 + " " + value1 + " " + value2, solrInputDocument.getFieldValue(fieldName));
  }
}
