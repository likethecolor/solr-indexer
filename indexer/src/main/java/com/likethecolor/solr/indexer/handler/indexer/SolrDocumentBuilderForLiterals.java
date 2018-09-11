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

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import org.apache.solr.common.SolrInputDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to add the literal values to a document.
 */
public class SolrDocumentBuilderForLiterals {
  private Map<String, FieldDefinition> literalsDefinitions;
  private UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator;

  public SolrDocumentBuilderForLiterals(final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator, final Map<String, FieldDefinition> literalsDefinitions) {
    this.literalsDefinitions = literalsDefinitions;
    this.uniqueKeyFieldValueGenerator = uniqueKeyFieldValueGenerator;
  }

  /**
   * Add the literal fields to the solr input document.
   *
   * @param doc solr input document
   */
  public void build(final SolrInputDocument doc) {
    // handle values in the fields list - making sure to exclude skip fields
    for(String literalFieldName : literalsDefinitions.keySet()) {
      FieldDefinition literalsFieldDefinitionDefinition = literalsDefinitions.get(literalFieldName);

      // use this map to always update fields
      // see: http://lucene.472066.n3.nabble.com/Updating-document-with-the-Solr-Java-API-td3998411.html
      Map<String, Object> updateMap = new HashMap<>();
      updateMap.put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, literalsFieldDefinitionDefinition.getValue());
      doc.addField(literalsFieldDefinitionDefinition.getName(), updateMap);
      uniqueKeyFieldValueGenerator.addField(literalsFieldDefinitionDefinition.getName(), literalsFieldDefinitionDefinition.getValue());
    }
  }
}
