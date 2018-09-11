/**
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
package com.likethecolor.solr.indexer.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ConfigFactory {
  private static ConfigFactory instance = new ConfigFactory();
  private HashMap<String, ConfigProperties> configMap = new HashMap<>();

  public static ConfigFactory getInstance() {
    return instance;
  }

  private ConfigFactory() {
  }

  synchronized public ConfigProperties getConfigProperties(String filePath) {
    ConfigProperties config = configMap.get(filePath);
    if (config == null) {
      config = new ConfigProperties(filePath);
      configMap.put(filePath, config);
    }

    return config;
  }

  synchronized public ConfigProperties getConfigPropertiesFromAbsolutePath(String abPath) {
    ConfigProperties config = configMap.get(abPath);
    if (config == null) {
      File file;
      InputStream in = null;
      try {
        file = new File(abPath);
        in = new FileInputStream(file);
        config = new ConfigProperties();
        config.load(in);
        configMap.put(abPath, config);
      } catch (IOException e) {
        throw new IllegalStateException("Failed to load config file from absolute path: " + abPath, e);
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (final IOException e) {
            // do nothing
          }
        }
      }
    }
    return config;
  }
}
