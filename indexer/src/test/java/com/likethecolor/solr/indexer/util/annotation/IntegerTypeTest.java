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
package com.likethecolor.solr.indexer.util.annotation;

import com.likethecolor.solr.indexer.util.annotation.IntegerType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegerTypeTest {
  @Test
  public void testIntegerType() {
    final Object integerObject = 602000;
    final Object integerPrimitive = 2061231;

    assertTrue(new IntegerType(integerObject.getClass()).isIntegerType());
    assertTrue(new IntegerType(integerPrimitive.getClass()).isIntegerType());
  }

  @Test
  public void testIntegerType_NotIntegerType() {
    final Object stringValue = "314159";

    assertFalse(new IntegerType(stringValue.getClass()).isIntegerType());
  }
}
