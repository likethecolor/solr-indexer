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
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.UniqueKeyFieldValueGenerator;
import com.likethecolor.solr.indexer.reader.DataReader;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;
import java.util.Map;

public class SolrDocumentHandler extends AbstractSolrDocumentHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrDocumentHandler.class);

  public SolrDocumentHandler(final Configuration configuration, final DataReader dataReader) {
    super(configuration, dataReader);
  }

  /**
   * Loop through field definitions and add the value in the corresponding row
   * of values to a solr document.
   *
   * @param rowValues list of values from the data file   @return solr document populated with the values from the row of values
   */
  protected SolrInputDocument buildSolrInputDocument(List<Object> rowValues) {
    SolrInputDocument doc = new SolrInputDocument();

    try {
      final UniqueKeyFieldValueGenerator uniqueKeyFieldValueGenerator = getUniqueKeyFieldValueGenerator();
      final SolrDocumentBuilderForFields builderForFields = new SolrDocumentBuilderForFields(getFieldValueSetter(), uniqueKeyFieldValueGenerator, getConfiguration().getUniqueKeyFieldName());
      builderForFields.setSkipFields(getSkipFields());
      builderForFields.setFieldDefinitionMap(getFieldDefinitions());

      final SolrDocumentBuilderForLiterals builderForLiterals = new SolrDocumentBuilderForLiterals(uniqueKeyFieldValueGenerator, getLiteralsDefinitions());
      final SolrDocumentBuilderForUniqueKeyField builderForUniqueKeyField = new SolrDocumentBuilderForUniqueKeyField(uniqueKeyFieldValueGenerator, getConfiguration().getUniqueKeyFieldName());
      builderForUniqueKeyField.build(doc);

      builderForFields.build(doc, rowValues);
      builderForLiterals.build(doc);

      // NOTE: This should almost always fire last.  Previous builders will
      // populate values on the document and this will/may use those values.
      handleDynamicClasses(doc);
    }
    catch(IllegalStateException e) {
      LOGGER.error("processing error - skipping document", e);
      doc = null;
    }

    return doc;
  }


  /**
   * Sets up the processors used for the examples. If there are 10 CSV columns
   * there must be 10 processors defined. Empty columns are read as null (hence
   * the NotNull() for mandatory columns).
   *
   * @return the cell processors
   */
  protected CellProcessor[] getProcessors(final Map<String, FieldDefinition> fieldDefinitions) {
    // TODO: Perhaps define more specific processors
    // I had this set up to use NotNull() for those fields not in the skip
    // fields list.  However, in some cases I found that the data file contained
    // null fields.  I wanted to be able to just skip that row and continue;
    // I found that to be more complicated than I have time for so I'm just setting
    // all the processors to be null.
    return new CellProcessor[fieldDefinitions.size()];
  }
}
