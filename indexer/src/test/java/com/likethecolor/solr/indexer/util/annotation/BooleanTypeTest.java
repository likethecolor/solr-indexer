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

import com.likethecolor.solr.indexer.util.annotation.BooleanType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanTypeTest {
  @Test
  public void testIsBooleanType() {
    final Object booleanObject = Boolean.TRUE;
    final Object booleanPrimitive = true;

    assertTrue(new BooleanType(booleanObject.getClass()).isBooleanType());
    assertTrue(new BooleanType(booleanPrimitive.getClass()).isBooleanType());

    final Object stringValue = "true";

    assertFalse(new BooleanType(stringValue.getClass()).isBooleanType());
  }

  @Test
  public void testIsBooleanType_NotBooleanType() {
    final Object stringValue = "true";

    assertFalse(new BooleanType(stringValue.getClass()).isBooleanType());
  }
}
