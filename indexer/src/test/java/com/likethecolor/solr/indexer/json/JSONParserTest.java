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

package com.likethecolor.solr.indexer.json;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.json.JSONParser;
import org.json.JSONException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JSONParserTest {
  @Test
  public void testParse_String() throws JSONException {
    Configuration configuration = new Configuration();
    String key = "string-key";
    String value = "string value";
    configuration.setFieldsToJSON(key + ":" + key);

    String jsonString = "{" +
                        "\"" + key + "\":\"" + value + "\"" +
                        "}";

    JSONParser parser = new JSONParser(configuration);

    Map<String, String> actualMap = parser.parse(jsonString);

    assertEquals(value, actualMap.get(key));
  }

  @Test(expected = JSONException.class)
  public void testParse_String_DuplicateKey() throws JSONException {
    String key = "string-key";
    String value0 = "7986384824792709";
    String value1 = "string value";

    String jsonString = "{" +
                        "\"" + key + "\":" + value0 + "," +
                        "\"" + key + "\":\"" + value1 + "\"" +
                        "}";

    JSONParser parser = new JSONParser(new Configuration());

    parser.parse(jsonString);
  }

  @Test
  public void testParse_NullString() throws JSONException {
    JSONParser parser = new JSONParser(new Configuration());

    Map<String, String> actualMap = parser.parse(null);

    assertEquals(0, actualMap.size());
  }

  @Test
  public void testParse_EmptyString() throws JSONException {
    JSONParser parser = new JSONParser(new Configuration());

    Map<String, String> actualMap = parser.parse("");

    assertEquals(0, actualMap.size());
  }

  @Test
  public void testParse_EmptyWhiteSpaceString() throws JSONException {
    JSONParser parser = new JSONParser(new Configuration());

    Map<String, String> actualMap = parser.parse("\t  \n\r");

    assertEquals(0, actualMap.size());
  }
}
