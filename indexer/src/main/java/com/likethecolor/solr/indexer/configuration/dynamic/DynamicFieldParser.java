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

package com.likethecolor.solr.indexer.configuration.dynamic;

import com.likethecolor.solr.indexer.Constants;
import com.likethecolor.solr.indexer.exception.DynamicFieldDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DynamicFieldParser implements Constants {
  private static final Logger LOGGER = LoggerFactory.getLogger(DynamicFieldParser.class);
  private String originaDynamicFieldDefinitionString;

  public Map<String, DynamicField> parse(String dynamicFieldDefinitionString) {
    originaDynamicFieldDefinitionString = dynamicFieldDefinitionString;
    Map<String, DynamicField> map = new HashMap<>();

    if(dynamicFieldDefinitionString != null) {
      // ip_range_id=com.likethecolor.solr.dynamic.IpRangeID(start_ip_num,end_ip_num)
      String[] split = dynamicFieldDefinitionString.split(LIST_DELIMITER);
      LOGGER.debug("split on " + LIST_DELIMITER + " as: " + split);
      for(String dynamicFieldDefinition : split) {
        DynamicField dynamicField = new DynamicField();
        String[] fieldClassArgs = dynamicFieldDefinition.split(EQUALS_LIST_DELIMITER);
        LOGGER.debug("split on " + EQUALS_LIST_DELIMITER + " as: " + fieldClassArgs);
        if(fieldClassArgs.length != 2) {
          throw new DynamicFieldDefinitionException(originaDynamicFieldDefinitionString + " (could not split into 2 using delimiter '" + EQUALS_LIST_DELIMITER + "')");
        }
        String fieldName = fieldClassArgs[0];
        dynamicField.setFieldName(fieldName);

        handleClass(fieldClassArgs[1], dynamicField);
        handleArgs(fieldClassArgs[1], dynamicField);

        map.put(fieldName, dynamicField);
      }
    }
    return map;
  }

  private void handleClass(String classArgs, DynamicField dynamicField) {
    int leftIdx = classArgs.indexOf(ARGS_LEFT_DELIMITER);
    if(leftIdx == -1) {
      throw new DynamicFieldDefinitionException(originaDynamicFieldDefinitionString + " (missing left delimiter '" + ARGS_LEFT_DELIMITER + "')");
    }
    String className = classArgs.substring(0, leftIdx);
    dynamicField.setClassName(className);
  }

  private void handleArgs(String classArgs, DynamicField dynamicField) {
    int leftIdx = classArgs.indexOf(ARGS_LEFT_DELIMITER);
    int rightIdx = classArgs.indexOf(ARGS_RIGHT_DELIMITER);
    String args = classArgs.substring(leftIdx + 1, rightIdx);

    String[] theArgs = args.split(ARGS_LIST_DELIMITER);
    LOGGER.debug("split on " + ARGS_LIST_DELIMITER + " as: " + theArgs);
    for(String arg : theArgs) {
      dynamicField.addFieldNameArgument(arg);
    }
  }
}
