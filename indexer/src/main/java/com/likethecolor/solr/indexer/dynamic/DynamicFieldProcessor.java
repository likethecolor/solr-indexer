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
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicField;
import com.likethecolor.solr.indexer.exception.DynamicClassException;
import org.apache.solr.common.SolrInputDocument;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DynamicFieldProcessor {
  private static final String DYNAMIC_METHOD_NAME = "populate";
  private Configuration configuration;
  private SolrInputDocument solrInputDocument;

  public DynamicFieldProcessor(final Configuration configuration, final SolrInputDocument solrInputDocument) {
    this.configuration = configuration;
    this.solrInputDocument = solrInputDocument;
  }

  public void process(DynamicField dynamicField) {
    try {
      Class<DynamicClass> dynamicClass = dynamicField.getDynamicClass();

      Constructor ctor = dynamicClass.getDeclaredConstructor(Configuration.class, SolrInputDocument.class);
      ctor.setAccessible(true);
      Object iClass = ctor.newInstance(configuration, solrInputDocument);

      Method method = dynamicClass.getDeclaredMethod(DYNAMIC_METHOD_NAME, List.class);
      List<String> args = new ArrayList<>();
      args.add(dynamicField.getFieldName());
      for(String arg : dynamicField.getFieldNameArguments()) {
        args.add(arg);
      }
      method.invoke(iClass, args);
    }
    catch(NoSuchMethodException e) {
      throw new DynamicClassException(e);
    }
    catch(IllegalAccessException e) {
      throw new DynamicClassException(e);
    }
    catch(InstantiationException e) {
      throw new DynamicClassException(e);
    }
    catch(InvocationTargetException e) {
      throw new DynamicClassException(e);
    }
  }
}
