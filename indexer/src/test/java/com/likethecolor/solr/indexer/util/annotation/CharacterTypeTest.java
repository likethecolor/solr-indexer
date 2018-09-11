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

import com.likethecolor.solr.indexer.util.annotation.CharacterType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterTypeTest {
  @Test
  public void testCharacterType() {
    final Object characterObject = 'a';
    final Object characterPrimitive = 'b';

    assertTrue(new CharacterType(characterObject.getClass()).isCharacterType());
    assertTrue(new CharacterType(characterPrimitive.getClass()).isCharacterType());
  }

  @Test
  public void testCharacterType_NotCharacterType() {
    final Object stringValue = "a";

    assertFalse(new CharacterType(stringValue.getClass()).isCharacterType());
  }
}
