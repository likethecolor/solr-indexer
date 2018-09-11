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
package com.likethecolor.solr.indexer.configuration;

import com.likethecolor.solr.indexer.Constants;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The only reason this class was created was to add new lines before the list
 * of options/descriptions and between each option/description.  The rest
 */
public class OptionsHelpFormatter extends HelpFormatter implements Constants {
  /**
   * /**
   * The only reason this class was created was to add new lines before the list
   * of options/descriptions and between each option/description.  The rest of
   * this method is the same as what is in {@link org.apache.commons.cli.HelpFormatter}
   *
   * Render the specified Options and return the rendered Options
   * in a StringBuffer.
   *
   * @param sb The StringBuffer to place the rendered Options into.
   * @param width The number of characters to display per line
   * @param options The command line Options
   * @param leftPad the number of characters of padding to be prefixed
   * to each line
   * @param descPad the number of characters of padding to be prefixed
   * to each description line
   *
   * @return the StringBuffer with the rendered Options contents.
   */
  protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
    final String descriptionPadding = createPadding(descPad);

    // first create list containing only <leftPadding>-a,--aaa where
    // -a is opt and --aaa is long opt; in parallel look for
    // the longest opt string this list will be then used to
    // sort options ascending
    int max = 0;
    StringBuffer optBuf;
    List<StringBuffer> prefixList = new ArrayList<>();

    List<Option> optList = new ArrayList<>(options.getOptions());

    Collections.sort(optList, getOptionComparator());

    for(Object anOptList : optList) {
      Option option = (Option) anOptList;
      optBuf = new StringBuffer(8);

      if(option.getOpt() == null) {
        // removed left padding and spacing here since there will be no short option names
        optBuf.append(getLongOptPrefix()).append(option.getLongOpt());
      }
      else {
        // commented out since there will be no short option names
//        optBuf.append(leftPadding).append(getOptPrefix()).append(option.getOpt());

        if(option.hasLongOpt()) {
          optBuf.append(',').append(getLongOptPrefix()).append(option.getLongOpt());
        }
      }

      if(option.hasArg()) {
        if(option.hasArgName()) {
          optBuf.append(" <").append(option.getArgName()).append(">");
        }
        else {
          optBuf.append(' ');
        }
      }

      prefixList.add(optBuf);
      max = (optBuf.length() > max) ? optBuf.length() : max;
    }

    int x = 0;

    // this line was added to the original found in HelpFormatter
    sb.append(getNewLine());
    for(Iterator<Option> i = optList.iterator(); i.hasNext(); ) {
      Option option = i.next();
      optBuf = new StringBuffer(prefixList.get(x++).toString());

      // added this line so desc will appear on a line below the option
      optBuf.append(getNewLine());
      if(optBuf.length() < max) {
        // replaced this since we're on a new line and do not need to pad to the
        // right of the option
//        optBuf.append(createPadding(max - optBuf.length()));
        optBuf.append(descriptionPadding);
      }

      if(option.getDescription() != null) {
        optBuf.append(option.getDescription());
      }

      renderWrappedText(sb, width, descPad, optBuf.toString());

      // this line was added to the original found in HelpFormatter
      sb.append(getNewLine());

      if(i.hasNext()) {
        sb.append(getNewLine());
      }
    }

    return sb;
  }
}
