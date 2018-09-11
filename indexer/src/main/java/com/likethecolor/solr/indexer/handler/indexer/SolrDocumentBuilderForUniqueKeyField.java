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

import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import org.apache.solr.common.SolrInputDocument;

/**
 * Adds the value for solr's unique field (id).
 */
public class SolrDocumentBuilderForUniqueKeyField {
  private String uniqueKeyFieldName;
  private UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator;

  public SolrDocumentBuilderForUniqueKeyField(final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator, final String uniqueKeyFieldName) {
    this.uniqueKeyFieldName = uniqueKeyFieldName;
    this.uniqueKeyFieldValueGenerator = uniqueKeyFieldValueGenerator;
  }

  /**
   * Generate and add the unique key field values to the solr document as the
   * value for the unique field.
   *
   * @param doc solr input document
   */
  public void build(final SolrInputDocument doc) {
    final String uniqueKeyFieldValueId = uniqueKeyFieldValueGenerator.getId();
    if(uniqueKeyFieldValueId != null && uniqueKeyFieldValueId.trim().length() > 0) {
      doc.addField(uniqueKeyFieldName, uniqueKeyFieldValueId);
    }
  }
}
