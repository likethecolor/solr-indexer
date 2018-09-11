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

import com.likethecolor.solr.indexer.util.annotation.DoubleType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleTypeTest {
  @Test
  public void testDoubleType() {
    final Object doubleObject = 6.02D;
    final Object doublePrimitive = 2.06D;

    assertTrue(new DoubleType(doubleObject.getClass()).isDoubleType());
    assertTrue(new DoubleType(doublePrimitive.getClass()).isDoubleType());
  }

  @Test
  public void testDoubleType_NotDoubleType() {
    final Object stringValue = "3.14159";

    assertFalse(new DoubleType(stringValue.getClass()).isDoubleType());
  }
}
