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
import com.likethecolor.solr.indexer.field.FieldValueSetter;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to add the field values to a document.  This takes into account the
 * skip fields and will not add those fields.
 */
public class SolrDocumentBuilderForFields {
  private Map<String, FieldDefinition> fieldDefinitionMap;
  private FieldValueSetter fieldValueSetter;
  private List<String> skipFields;
  private String uniqueKeyFieldName;
  private UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator;

  public SolrDocumentBuilderForFields(final FieldValueSetter fieldValueSetter, final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator, final String uniqueKeyFieldName) {
    this.fieldValueSetter = fieldValueSetter;
    this.uniqueKeyFieldName = uniqueKeyFieldName;

    fieldDefinitionMap = new HashMap<>();
    skipFields = new ArrayList<>();
    this.uniqueKeyFieldValueGenerator = uniqueKeyFieldValueGenerator;
  }

  /**
   * Set the field definition map.  This will most often than not need to be used.
   *
   * @param fieldDefinitionMap map to set
   */
  public void setFieldDefinitionMap(final Map<String, FieldDefinition> fieldDefinitionMap) {
    this.fieldDefinitionMap = fieldDefinitionMap;
  }

  /**
   * Set the list of skip fields.  These are the fields that will not be included
   * in the document.
   *
   * @param skipFields list of field names to not include in the document.
   */
  public void setSkipFields(final List<String> skipFields) {
    this.skipFields = skipFields;
  }

  /**
   * Add the values from a row in the data file to the solr input document.  The
   * fields in the skip list will not be added to the document.
   *
   * @param doc solr input document
   * @param rowValues list of data values
   */
  public void build(final SolrInputDocument doc, final List<Object> rowValues) {
    int fieldIndex = 0;
    for(String fieldName : fieldDefinitionMap.keySet()) {
      // use clone to avoid overwriting the original field definitions
      FieldDefinition fieldDefinitionDefinition = fieldDefinitionMap.get(fieldName).clone();
      String value = (String) rowValues.get(fieldIndex);
      if(value == null || value.trim().length() == 0) {
        fieldIndex++;
        continue;
      }
      fieldValueSetter.setFieldValue(fieldDefinitionDefinition, value);
      uniqueKeyFieldValueGenerator.addField(fieldDefinitionDefinition.getName(), fieldDefinitionDefinition.getValue());
      fieldIndex++;

      // do not add the skip fields
      if(skipFields.contains(fieldName)) {
        continue;
      }


      // use this map to always update fields
      // see: http://lucene.472066.n3.nabble.com/Updating-document-with-the-Solr-Java-API-td3998411.html
      //
      // NOTE: This cannot be done on the unique id field - that field is unique
      // and will/should never get updated
      //
      Object valueToSet = fieldDefinitionDefinition.getValue();
      if(!fieldName.equalsIgnoreCase(uniqueKeyFieldName)) {
        valueToSet = new HashMap<String, Object>();
        ((Map<String, Object>) valueToSet).put(Constants.KEY_FOR_UPDATE_DOCUMENT_ACTION, fieldDefinitionDefinition.getValue());
      }
      doc.addField(fieldDefinitionDefinition.getName(), valueToSet);
    }
  }
}
