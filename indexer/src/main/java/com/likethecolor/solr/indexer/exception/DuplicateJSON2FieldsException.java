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

package com.likethecolor.solr.indexer.exception;

public class DuplicateJSON2FieldsException extends RuntimeException {
  private static final long serialVersionUID = 1;
  private static final String DUPLICATE_MSG_PATTERN = "json field \"%s\" field: \"%s\"";

  public DuplicateJSON2FieldsException() {
  }

  public DuplicateJSON2FieldsException(String message) {
    super(message);
  }

  public DuplicateJSON2FieldsException(String jsonField, String fieldName) {
    super(String.format(DUPLICATE_MSG_PATTERN, jsonField, fieldName));
  }

  public DuplicateJSON2FieldsException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateJSON2FieldsException(Throwable cause) {
    super(cause);
  }

  public DuplicateJSON2FieldsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
