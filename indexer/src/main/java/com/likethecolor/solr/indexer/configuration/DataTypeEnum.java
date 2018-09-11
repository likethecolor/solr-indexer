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

package com.likethecolor.solr.indexer.configuration;

import com.likethecolor.solr.indexer.Constants;

public enum DataTypeEnum implements Constants {
  CSV {
    @Override
    public String getName() {
      return DATA_TYPE_CSV;
    }
  },
  JSON {
    @Override
    public String getName() {
      return DATA_TYPE_JSON;
    }
  },
  DEFAULT {
    @Override
    public String getName() {
      return DATA_TYPE_DEFAULT;
    }
  };

  public static DataTypeEnum get(String dataType) {
    if(CSV.getName().equalsIgnoreCase(dataType)) {
      return CSV;
    }
    if(JSON.getName().equalsIgnoreCase(dataType)) {
      return JSON;
    }
    return DEFAULT;
  }

  public abstract String getName();

}
