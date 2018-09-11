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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to place on a setter method to wire a configuration value onto the
 * {@link Configuration} object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigurationValues {
  /**
   * Name of the long command line option.
   *
   * @return name of the long command line option
   */
  String optionName();

  /**
   * Default string value.
   *
   * @return default string value
   */
  String defaultValue() default "";

  /**
   * Default character value.
   *
   * @return default character value
   */
  char defaultValueCharacter() default '\0';

  /**
   * Default boolean value.
   *
   * @return default boolean value
   */
  boolean defaultValueBoolean() default false;

  /**
   * Default double value.
   *
   * @return default double value
   */
  double defaultValueDouble() default 0.0D;

  /**
   * Default integer value.
   *
   * @return default integer value
   */
  int defaultValueInteger() default 0;

  /**
   * Default string value.
   *
   * @return default long value
   */
  long defaultValueLong() default 0L;
}
