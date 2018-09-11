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

import com.likethecolor.solr.indexer.dynamic.DynamicClass;
import com.likethecolor.solr.indexer.dynamic.DynamicClassLoadingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DynamicFieldTest {
  @Test
  public void testClassName() {
    String className = "com.likethecolor.test.MyClass";

    DynamicField dynamicField = new DynamicField();

    dynamicField.setClassName(className);

    assertEquals(className, dynamicField.getClassName());


    dynamicField.setClassName(null);

    assertEquals("", dynamicField.getClassName());

    dynamicField.setClassName("  " + className + "\t  \r\n");

    assertEquals(className, dynamicField.getClassName());

    dynamicField.setClassName(className);
    dynamicField.setClassName("");

    assertEquals("", dynamicField.getClassName());

    dynamicField.setClassName(className);
    dynamicField.setClassName("\t   ");

    assertEquals("", dynamicField.getClassName());
  }

  @Test
  public void testDynamicClass() {
    String className = "com.likethecolor.solr.indexer.dynamic.DynamicClass";

    DynamicField dynamicField = new DynamicField();

    dynamicField.setClassName(className);

    assertEquals(DynamicClass.class, dynamicField.getDynamicClass());
  }

  @Test(expected = DynamicClassLoadingException.class)
  public void testDynamicClass_CannotFindClass() {
    String className = "com.likethecolor.Foo";

    DynamicField dynamicField = new DynamicField();

    dynamicField.setClassName(className);

    dynamicField.getDynamicClass();
  }

  @Test(expected = DynamicClassLoadingException.class)
  public void testDynamicClass_ClassIsNotInstanceOfDynamicClass() {
    String className = "com.likethecolor.solr.indexer.configuration.dynamic.DynamicFieldParser";

    DynamicField dynamicField = new DynamicField();

    dynamicField.setClassName(className);

    dynamicField.getDynamicClass();
  }

  @Test
  public void testFieldName() {
    String fieldName = "my_id";

    DynamicField dynamicField = new DynamicField();

    dynamicField.setFieldName(fieldName);

    assertEquals(fieldName, dynamicField.getFieldName());


    dynamicField.setFieldName(null);

    assertEquals("", dynamicField.getFieldName());

    dynamicField.setFieldName("  " + fieldName + "\t  \r\n");

    assertEquals(fieldName, dynamicField.getFieldName());

    dynamicField.setFieldName(fieldName);
    dynamicField.setFieldName("");

    assertEquals("", dynamicField.getFieldName());

    dynamicField.setFieldName(fieldName);
    dynamicField.setFieldName("\t   ");

    assertEquals("", dynamicField.getFieldName());
  }

  @Test
  public void testFieldNameArguments_OneArgument() {
    String fieldNameArg = "foo";

    DynamicField dynamicField = new DynamicField();

    assertEquals(0, dynamicField.getFieldNameArguments().size());

    dynamicField.addFieldNameArgument(fieldNameArg);

    assertEquals(1, dynamicField.getFieldNameArguments().size());

    assertEquals(fieldNameArg, dynamicField.getFieldNameArguments().get(0));


    dynamicField = new DynamicField();

    dynamicField.addFieldNameArgument("  " + fieldNameArg + "   \r");

    assertEquals(1, dynamicField.getFieldNameArguments().size());

    assertEquals(fieldNameArg, dynamicField.getFieldNameArguments().get(0));
  }

  @Test
  public void testFieldNameArguments_MultipleArguments() {
    int numberOfArguments = 11;
    String fieldNameArg = "foo";

    DynamicField dynamicField = new DynamicField();

    for(int i = 0; i < numberOfArguments; i++) {
      dynamicField.addFieldNameArgument(fieldNameArg + i);
      dynamicField.addFieldNameArgument(null); // not added
    }
    assertEquals(numberOfArguments, dynamicField.getFieldNameArguments().size());

    for(int i = 0; i < numberOfArguments; i++) {
      assertEquals(fieldNameArg + i, dynamicField.getFieldNameArguments().get(i));
    }
  }
}
