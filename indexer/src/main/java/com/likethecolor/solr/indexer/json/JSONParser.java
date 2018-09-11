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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONParser {
  private static final Logger LOGGER = LoggerFactory.getLogger(JSONParser.class);
  private Configuration configuration;

  public JSONParser(Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * The public method to be called to fetch the populated map.  The keys of the
   * map should be the same as the field values defined in solr's schema.xml.
   *
   * @param rowOfData json string used to populate the map
   *
   * @return map populated from the json string
   *
   * @throws JSONException error processing the json
   */
  public Map<String, String> parse(String rowOfData) throws JSONException {
    Map<String, String> map = new HashMap<>();
    if((rowOfData != null) && (rowOfData.trim().length() > 0)) {
      JSONObject jsonObject = getJSONObject(rowOfData);
      if(jsonObject != null) {
        map = getSpecificMap(jsonObject);
      }
    }
    return map;
  }

  /**
   * Subclasses should do the actual populating of the map from a JSONObject.
   * The keys of the map should be the same as the field values defined in solr's
   * schema.xml.
   *
   * @param jsonObject the json object used to populate the map
   *
   * @return map populated from the json string
   */
  private Map<String, String> getSpecificMap(JSONObject jsonObject) throws JSONException {
    Fields2JSON fields2JSON = new Fields2JSON(configuration);

    Map<String, String> map = new HashMap<>();

    Iterator<String> fieldsIter = fields2JSON.iterator();
    while(fieldsIter.hasNext()) {
      String key = fieldsIter.next();
      Object value = getValue(fields2JSON.get(key), jsonObject);
      String strValue = null;
      if(value != null) {
        strValue = value.toString();
      }
      map.put(key, strValue);
    }
    return map;
  }

  /**
   * Check to make sure the JSONObject has the specified key and if so return
   * the value as a string. If no key is found "" is returned.
   *
   * @param key name of the field to fetch from the json object
   * @param jsonObject data used to populate the map
   *
   * @return string value corresponding to the key or "" if key not found
   *
   * @throws JSONException error processing the json
   */
  private Object getValue(String key, JSONObject jsonObject) throws JSONException {
    return getValue(key, jsonObject, "");
  }

  /**
   * Check to make sure the JSONObject has the specified key and if so return
   * the value as a string. If no key is found "" is returned.
   *
   * @param key name of the field to fetch from the json object
   * @param jsonObject data used to populate the map
   * @param defaultValue value to use by default
   *
   * @return string value corresponding to the key or "" if key not found
   *
   * @throws JSONException error processing the json
   */
  private Object getValue(String key, JSONObject jsonObject, Object defaultValue) throws JSONException {
    Object value = defaultValue;
    if(hasKey(key, jsonObject)) {
      value = jsonObject.get(key);
    }
    return value;
  }

  /**
   * Fetch a JSONObject from the provided string.
   *
   * @param rowOfData json string
   *
   * @return json object representing the string or null if there is an exception
   */
  private JSONObject getJSONObject(String rowOfData) throws JSONException {
    JSONObject json;
    try {
      json = new JSONObject(rowOfData);
    }
    catch(JSONException e) {
      LOGGER.warn("could not parse string into JSONObject", e);
      LOGGER.warn(rowOfData);
      throw e;
    }
    return json;
  }

  /**
   * Does the JSONObject have a specified key?
   *
   * @param key key to check
   * @param jsonObject object to check
   *
   * @return true if the key exists
   */
  private boolean hasKey(String key, JSONObject jsonObject) {
    return jsonObject != null
           && jsonObject.has(key);
  }
}
