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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicField;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DynamicFieldProcessorTest {
  @Test
  public void testProcess() {
    Configuration configuration = new Configuration();
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.indexer.dynamic.UseThisToTestDynamicCode";
    String arg0 = "arg0";
    String arg1 = "arg1";
    String arg2 = "arg2";
    String value0 = "value-zero";
    String value1 = "value-one";
    String value2 = "value-two";
    DynamicField dynamicField = new DynamicField();
    dynamicField.setClassName(className);
    dynamicField.setFieldName(fieldName);
    dynamicField.addFieldNameArgument(arg0);
    dynamicField.addFieldNameArgument(arg1);

    dynamicField.addFieldNameArgument(arg2);

    SolrInputDocument solrInputDocument = new SolrInputDocument();
    Map<String, Object> map = new HashMap<>();
    map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value0);
    solrInputDocument.setField(arg0, map);

    map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value1);
    solrInputDocument.setField(arg1, map);

    map = new HashMap<>();
    map.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, value2);
    solrInputDocument.setField(arg2, map);

    DynamicFieldProcessor dynamicFieldProcessor = new DynamicFieldProcessor(configuration, solrInputDocument);
    dynamicFieldProcessor.process(dynamicField);

    assertEquals(value0 + value1 + value2, solrInputDocument.getFieldValue(fieldName));
  }

  @Test
  public void testProcess_ValueNotMap() {
    Configuration configuration = new Configuration();
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.indexer.dynamic.UseThisToTestDynamicCode";
    String arg0 = "arg0";
    String arg1 = "arg1";
    String arg2 = "arg2";

    DynamicField dynamicField = new DynamicField();
    dynamicField.setClassName(className);
    dynamicField.setFieldName(fieldName);
    dynamicField.addFieldNameArgument(arg0);
    dynamicField.addFieldNameArgument(arg1);
    dynamicField.addFieldNameArgument(arg2);

    SolrInputDocument solrInputDocument = new SolrInputDocument();
    solrInputDocument.setField(fieldName, null);
    DynamicFieldProcessor dynamicFieldProcessor = new DynamicFieldProcessor(configuration, solrInputDocument);
    dynamicFieldProcessor.process(dynamicField);

    assertEquals("", solrInputDocument.getFieldValue(fieldName));
  }

  @Test
  public void testProcess_NoFieldInDocMatchingFieldName() {
    Configuration configuration = new Configuration();
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.indexer.dynamic.UseThisToTestDynamicCode";
    String arg0 = "arg0";
    String arg1 = "arg1";
    String arg2 = "arg2";

    DynamicField dynamicField = new DynamicField();
    dynamicField.setClassName(className);
    dynamicField.addFieldNameArgument(fieldName);
    dynamicField.addFieldNameArgument(arg0);
    dynamicField.addFieldNameArgument(arg1);
    dynamicField.addFieldNameArgument(arg2);

    SolrInputDocument solrInputDocument = new SolrInputDocument();
    DynamicFieldProcessor dynamicFieldProcessor = new DynamicFieldProcessor(configuration, solrInputDocument);
    dynamicFieldProcessor.process(dynamicField);

    assertNull(solrInputDocument.getFieldValue(fieldName));
  }

  @Test(expected = DynamicClassLoadingException.class)
  public void testProcess_DynamicClassDoesNotExist() {
    String className = "com.likethecolor.solr.indexer.dynamic.Foo";
    DynamicField dynamicField = new DynamicField();
    dynamicField.setClassName(className);

    DynamicFieldProcessor dynamicFieldProcessor = new DynamicFieldProcessor(new Configuration(), new SolrInputDocument());
    dynamicFieldProcessor.process(dynamicField);
  }

  @Test(expected = DynamicClassLoadingException.class)
  public void testProcess_DynamicClassThrowsException() {
    String className = "com.likethecolor.solr.indexer.dynamic.RuntimeExceptionSampleDynamicClassForTesting";
    DynamicField dynamicField = new DynamicField();
    dynamicField.setClassName(className);

    DynamicFieldProcessor dynamicFieldProcessor = new DynamicFieldProcessor(new Configuration(), new SolrInputDocument());
    dynamicFieldProcessor.process(dynamicField);
  }
}
