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
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import com.likethecolor.solr.indexer.handler.indexer.SolrDocumentBuilderForUniqueKeyField;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolrDocumentBuilderForUniqueKeyFieldTest {
  private static final String MAP_PREFIX = "name";

  @Test
  public void testBuild_ZeroFields() {
    final String uniqueKeyFieldName = "id";
    final Configuration configuration = new Configuration();
    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);

    final SolrDocumentBuilderForUniqueKeyField builder = new SolrDocumentBuilderForUniqueKeyField(uniqueKeyFieldValueGenerator, uniqueKeyFieldName);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    assertEquals(0, doc.keySet().size());

    assertEquals("", uniqueKeyFieldValueGenerator.getId());
  }

  @Test
  public void testBuild_OneField() {
    final String uniqueKeyFieldName = "id";
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 0);

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 0, 0);

    final SolrDocumentBuilderForUniqueKeyField builder = new SolrDocumentBuilderForUniqueKeyField(uniqueKeyFieldValueGenerator, uniqueKeyFieldName);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals("0", field.getValue());
  }

  @Test
  public void testBuild_TwoFields() {
    final String uniqueKeyFieldName = "id";
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 11 + ";" + MAP_PREFIX + 10);
    configuration.setUniqueKeyFieldValueDelimiter("-");

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 11, 11);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 10, 10);

    final SolrDocumentBuilderForUniqueKeyField builder = new SolrDocumentBuilderForUniqueKeyField(uniqueKeyFieldValueGenerator, uniqueKeyFieldName);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    final String expectedValue = "11-10";
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(expectedValue, field.getValue());
  }

  @Test
  public void testBuild_MultipleFields() {
    final String uniqueKeyFieldName = "id";
    final Configuration configuration = new Configuration();
    configuration.setUniqueKeyFieldValue(MAP_PREFIX + 11 + ";" + MAP_PREFIX + 10 + ";" + MAP_PREFIX + 3 + ";" + MAP_PREFIX + 8);
    configuration.setUniqueKeyFieldValueDelimiter("-");

    final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = new UniqueKeyFieldValueGenerator(configuration);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 11, 11);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 10, 10);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 3, 3);
    uniqueKeyFieldValueGenerator.addField(MAP_PREFIX + 8, 8);

    final SolrDocumentBuilderForUniqueKeyField builder = new SolrDocumentBuilderForUniqueKeyField(uniqueKeyFieldValueGenerator, uniqueKeyFieldName);

    final SolrInputDocument doc = new SolrInputDocument();

    builder.build(doc);

    final String expectedValue = "11-10-3-8";
    SolrInputField field = doc.get(uniqueKeyFieldName);
    assertEquals(uniqueKeyFieldName, field.getName());
    assertEquals(expectedValue, field.getValue());
  }
}
