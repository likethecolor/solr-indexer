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
package com.likethecolor.solr.indexer.util.conversion;

import java.util.concurrent.TimeUnit;

public class ToTimeConversion {
  private long milliseconds;

  public ToTimeConversion(long milliseconds) {
    this.milliseconds = milliseconds;
  }

  public String getMinutesSecondsMillisecondsFromMilliseconds() {
    final long millis = toMilliseconds(milliseconds);
    return String.format("%d min, %02d sec, %03d ms",
        toMinutes(milliseconds),
        toSeconds(milliseconds),
        millis
    );
  }

  private long toMinutes(long milliseconds) {
    return TimeUnit.MILLISECONDS.toMinutes(milliseconds);
  }

  private long toSeconds(long milliseconds) {
    return TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
           TimeUnit.MINUTES.toSeconds(toMinutes(milliseconds));
  }

  private long toMilliseconds(long milliseconds) {
    final long minutes = toMinutes(milliseconds);
    final long seconds = toSeconds(milliseconds);
    return milliseconds - ((minutes * 60 * 1000) + (seconds * 1000));
  }
}
