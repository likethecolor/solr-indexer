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
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicField;
import com.likethecolor.solr.indexer.configuration.dynamic.DynamicFieldParser;
import com.likethecolor.solr.indexer.exception.DynamicFieldDefinitionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DynamicFieldParserTest {
  @Test
  public void testParse_NullArg() {
    DynamicFieldParser parser = new DynamicFieldParser();

    Map<String, DynamicField> actualMap = parser.parse(null);

    assertEquals(0, actualMap.size());
  }

  @Test
  public void testParse_NoArgs() {
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.dynamic.MyDynamicClass";
    String dynamicString = fieldName + Constants.EQUALS_LIST_DELIMITER + className + "()";

    DynamicFieldParser parser = new DynamicFieldParser();

    Map<String, DynamicField> actualMap = parser.parse(dynamicString);

    assertTrue(actualMap.containsKey(fieldName));

    DynamicField actualDynamicField = actualMap.get(fieldName);
    assertEquals(fieldName, actualDynamicField.getFieldName());
    assertEquals(className, actualDynamicField.getClassName());
    assertEquals(0, actualDynamicField.getFieldNameArguments().size());
  }

  @Test
  public void testParsee_Args() {
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.dynamic.MyDynamicClass";
    List<String> args = new ArrayList<>();
    args.add("arg0");
    args.add("arg1");
    args.add("arg2");
    StringBuilder dynamicString = new StringBuilder(fieldName)
        .append(Constants.EQUALS_LIST_DELIMITER)
        .append(className)
        .append(Constants.ARGS_LEFT_DELIMITER);
    int idx = 0;
    for(String arg : args) {
      if(idx++ > 0) {
        dynamicString.append(Constants.ARGS_LIST_DELIMITER);
      }
      dynamicString.append(arg);
    }
    dynamicString.append(Constants.ARGS_RIGHT_DELIMITER);

    DynamicFieldParser parser = new DynamicFieldParser();

    Map<String, DynamicField> actualMap = parser.parse(dynamicString.toString());

    assertTrue(actualMap.containsKey(fieldName));

    DynamicField actualDynamicField = actualMap.get(fieldName);
    assertEquals(fieldName, actualDynamicField.getFieldName());
    assertEquals(className, actualDynamicField.getClassName());
    assertEquals(args, actualDynamicField.getFieldNameArguments());
  }

  @Test
  public void testParse_MultipleEntriesWithArgs() {
    String[] fieldNames = new String[]{"my_id0", "my_id1", "my_id2"};
    String[] classNames = new String[]{"com.likethecolor.solr.dynamic.MyDynamicClass0", "com.likethecolor.solr.dynamic.MyDynamicClass1", "com.likethecolor.solr.dynamic.MyDynamicClass2"};
    List<String> args0 = new ArrayList<>();
    args0.add("arg0");
    args0.add("arg1");
    args0.add("arg2");
    List<String> args1 = new ArrayList<>();
    args1.add("arg0");

    StringBuilder dynamicString = new StringBuilder(fieldNames[0])
        .append(Constants.EQUALS_LIST_DELIMITER)
        .append(classNames[0])
        .append(Constants.ARGS_LEFT_DELIMITER);
    int idx = 0;
    for(String arg : args0) {
      if(idx++ > 0) {
        dynamicString.append(Constants.ARGS_LIST_DELIMITER);
      }
      dynamicString.append(arg);
    }
    dynamicString.append(Constants.ARGS_RIGHT_DELIMITER);

    dynamicString.append(Constants.LIST_DELIMITER);

    dynamicString.append(fieldNames[1])
        .append(Constants.EQUALS_LIST_DELIMITER)
        .append(classNames[1])
        .append(Constants.ARGS_LEFT_DELIMITER);
    idx = 0;
    for(String arg : args1) {
      if(idx++ > 0) {
        dynamicString.append(Constants.ARGS_LIST_DELIMITER);
      }
      dynamicString.append(arg);
    }
    dynamicString.append(Constants.ARGS_RIGHT_DELIMITER);

    dynamicString.append(Constants.LIST_DELIMITER);

    dynamicString.append(fieldNames[2])
        .append(Constants.EQUALS_LIST_DELIMITER)
        .append(classNames[2])
        .append(Constants.ARGS_LEFT_DELIMITER);
    dynamicString.append(Constants.ARGS_RIGHT_DELIMITER);


    DynamicFieldParser parser = new DynamicFieldParser();

    Map<String, DynamicField> actualMap = parser.parse(dynamicString.toString());

    assertTrue(actualMap.containsKey(fieldNames[0]));
    DynamicField actualDynamicField = actualMap.get(fieldNames[0]);
    assertEquals(fieldNames[0], actualDynamicField.getFieldName());
    assertEquals(classNames[0], actualDynamicField.getClassName());
    assertEquals(args0, actualDynamicField.getFieldNameArguments());

    assertTrue(actualMap.containsKey(fieldNames[1]));
    actualDynamicField = actualMap.get(fieldNames[1]);
    assertEquals(fieldNames[1], actualDynamicField.getFieldName());
    assertEquals(classNames[1], actualDynamicField.getClassName());
    assertEquals(args1, actualDynamicField.getFieldNameArguments());

    assertTrue(actualMap.containsKey(fieldNames[2]));
    actualDynamicField = actualMap.get(fieldNames[2]);
    assertEquals(fieldNames[2], actualDynamicField.getFieldName());
    assertEquals(classNames[2], actualDynamicField.getClassName());
    assertEquals(0, actualDynamicField.getFieldNameArguments().size());
  }

  @Test(expected = DynamicFieldDefinitionException.class)
  public void testParse_NoEquals() {
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.dynamic.MyDynamicClass";
    String dynamicString = fieldName + Constants.LIST_DELIMITER + className;

    DynamicFieldParser parser = new DynamicFieldParser();

    parser.parse(dynamicString);
  }

  @Test(expected = DynamicFieldDefinitionException.class)
  public void testParse_NoArgsNoParens() {
    String fieldName = "my_id";
    String className = "com.likethecolor.solr.dynamic.MyDynamicClass";
    String dynamicString = fieldName + Constants.EQUALS_LIST_DELIMITER + className;

    DynamicFieldParser parser = new DynamicFieldParser();

    parser.parse(dynamicString);
  }
}
