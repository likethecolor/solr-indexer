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

import com.likethecolor.solr.indexer.util.annotation.LongType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LongTypeTest {
  @Test
  public void testLongType() {
    final Object longObject = 9798602000L;
    final Object longPrimitive = 2061231153252L;

    assertTrue(new LongType(longObject.getClass()).isLongType());
    assertTrue(new LongType(longPrimitive.getClass()).isLongType());
  }

  @Test
  public void testLongType_NotLongType() {
    final Object stringValue = "31415914121";

    assertFalse(new LongType(stringValue.getClass()).isLongType());
  }
}
