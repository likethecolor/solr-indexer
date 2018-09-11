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

import org.apache.solr.common.SolrInputDocument;

import java.util.List;

/**
 * The {@link DynamicClass} implements this interface.  All dynamic classes
 * should extend {@link DynamicClass}.
 */
public interface Dynamic {
  /**
   * Sets the value on a {@link SolrInputDocument}.  The first element of the
   * parameter is the name of the field in the {@link SolrInputDocument} on
   * which the final value is set.  The rest of the elements are the name(s) of
   * the field(s) found in the {@link SolrInputDocument} used to make up the
   * final value.
   * 
   * The general process is to first get the values of the non-first element
   * from the {@link SolrInputDocument}, process them, then set the value of
   * the first element to be the processed value.
   * 
   * @param setFieldAndFieldNames list of field names
   */
  void populate(List<String> setFieldAndFieldNames);
}
