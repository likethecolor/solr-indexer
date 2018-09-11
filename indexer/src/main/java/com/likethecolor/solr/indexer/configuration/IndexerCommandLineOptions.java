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

/**
 * Handle command line options.
 */
public class IndexerCommandLineOptions implements Constants {
  private static final int DEFAULT_WIDTH = 80;
  private String footer;

  public static void main(String[] args) {
    new IndexerCommandLineOptions().printHelp();
  }

  /**
   * Return the string used for the footer.
   *
   * @return string used for the footer
   */
  private String getFooter() {
    return footer;
  }

  public void setFooter(final String footer) {
    this.footer = footer;
  }

  public Options getOptionDefinitions() {
    Options options = new Options();

    // boolean options
    Option option = Option.builder()
        .longOpt(FIRST_ROW_IS_HEADER_OPTION)
        .desc(String.format("Use if the first row in the data file is a data row (first row is data, not column names, and should be read as data).  " +
                            "Note that this value is ignored when the data type (--%s) is JSON.  That is, all rows in the JSON data file are indexed.", DATA_TYPE_OPTION))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(HELP_OPTION)
        .desc("This usage message.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(OPTIMIZE_INDEX_OPTION)
        .desc("Use if the index should be optimized.")
        .build();
    options.addOption(option);

    // requires an argument
    option = Option.builder()
        .longOpt(SOFT_COMMIT_FREQUENCY)
        .hasArg()
        .argName(String.valueOf(DEFAULT_SOFT_COMMIT_FREQUENCY))
        .desc("Use to make soft commits.  Value of 0 means never make soft commits.  A value >= 1 means do a soft commit after that many batches.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(BATCH_SIZE_OPTION)
        .hasArg()
        .argName(String.valueOf(DEFAULT_BATCH_SIZE))
        .desc(getDescription("Number of documents to process before sending the documents to the solr server.  Note must be an integer > 0.", DEFAULT_BATCH_SIZE))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(COLLECTION_NAME_OPTION)
        .hasArg()
        .argName("name")
        .desc("The name of the collection.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(CSV_DELIMITER_OPTION)
        .hasArg()
        .argName(String.valueOf(DEFAULT_CSV_DELIMITER))
        .desc("The character that delimits the fields in the data file.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(CSV_QUOTE_CHARACTER_OPTION)
        .hasArg()
        .argName(String.valueOf(DEFAULT_CSV_QUOTE_CHARACTER))
        .desc(getDescription("The character that surrounds the value in each column", String.valueOf(DEFAULT_CSV_QUOTE_CHARACTER)))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(DATA_TYPE_OPTION)
        .hasArg()
        .desc(String.format("Format of data file to be indexed (%s, %s)", DATA_TYPE_CSV, DATA_TYPE_JSON))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(DYNAMIC_OPTION)
        .hasArg()
        .desc("Set up a way to dynamically generate values.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(FIELDS_OPTION)
        .hasArg()
        .argName("code:string;create_datetime:datetime:MM/dd/yyyy hh:mm:ssa[;...]")
        .desc("The data file will have columns.  This is a list of every column in that file.  If they are to be indexed (see skip fields) the name in this list should correspond to the solr field name.  Note that there should be no spaces in the list.  If there are the entire list should be surrounded by quotes.  The types are the data type of the field.  Valid types: multivalued, datetime, double, int, long, string  If no type is provided string will be assumed.  For datetime a third element may be given as the format of the date [default: MM/dd/yyyy hh:mm:ssa]  The date is parsed by SimpleDateFormat.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(LITERALS_OPTION)
        .hasArg()
        .argName("index_type:string:site,type_sort:int:3[;...]")
        .desc("Field definition names that are in the solr schema and have a literal value.  The first element (to the left of the colon delimiter) is the field name as it appears in the solr schema.  The second element (to the right of the first colon delimiter) is the data type.  The third element is the literal value.  The semi-colon allows for more than one name:value pair.  Multivalue type is not supported at this time.  For type datetime the date format is assumed to be yyyy-MM-dd hh:mm:ssa (parsed by SimpleDateFormat)")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(MULTIVALUE_FIELD_DELIMITER_OPTION)
        .hasArg()
        .argName(DEFAULT_MULTIVALUE_FIELD_DELIMITER)
        .desc("For those data types that are multi-valued this flag indicates the delimiter of those values.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(PATH_TO_DATA_FILE_OPTION)
        .hasArg()
        .argName("/path/to/file.csv")
        .desc("Full path to the file containing the data to be indexed in CSV format.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(RETRY_COUNT_OPTION)
        .hasArg()
        .argName("count")
        .desc(getDescription("Number of times to retry sending a batch due to failure.", DEFAULT_RETRY_COUNT))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(SKIP_FIELDS_OPTION)
        .hasArg()
        .argName("field0[;field1;...]")
        .desc("FieldDefinition names pointing to columns in the data file that should be skipped.  A name in this skip list should match the name in the field names list.  Note that there should be no spaces in the list.  If there are the entire list should be surrounded by quotes.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(SLEEP_BETWEEN_RETRIES_OPTION)
        .hasArg()
        .argName("count")
        .desc(getDescription("Number of milliseconds to sleep between retries.", DEFAULT_SLEEP_MILLIS_BETWEEN_RETRIES))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(THREAD_COUNT_OPTION)
        .hasArg()
        .argName("count")
        .desc(getDescription("Number of threads in which to run this process.", DEFAULT_THREAD_COUNT))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(UNIQUE_KEY_FIELD_NAME_OPTION)
        .hasArg()
        .argName(DEFAULT_UNIQUE_KEY_FIELD_NAME)
        .desc(getDescription("Name of the id field (unique key).", DEFAULT_UNIQUE_KEY_FIELD_NAME))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(UNIQUE_KEY_FIELD_VALUE_OPTION)
        .hasArg()
        .argName("id;type")
        .desc("If needed the values in this list will be used to generate the value used for the unique_key_field_name - in the order given.  These fields must come from " + FIELDS_OPTION + " and/or " + LITERALS_OPTION)
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(UNIQUE_KEY_FIELD_VALUE_DELIMITER_OPTION)
        .hasArg()
        .argName("-")
        .desc("If needed the values in this list will be used to generate the value used for the unique_key_field_name - in the order given.  These fields must come from " + FIELDS_OPTION + " and/or " + LITERALS_OPTION)
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(ZOOKEEPER_HOST_OPTION)
        .hasArg()
        .argName("host:port[;host1:port1;...][/rooted/node]")
        .desc("A list of zookeeper host names and ports.  The first element (to the right of the colon delimiter) is the name of the host.  The second element (to the left of the colon delimiter) is the port for that host.  The semi-colon allows for more than one name:value pair.  Note that there should be no spaces in the list.  If there are the entire list should be surrounded by quotes.")
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT_OPTION)
        .hasArg()
        .argName(String.valueOf(DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT))
        .desc(getDescription("Number of milliseconds allowed for the zookeeper client connection.", DEFAULT_ZOOKEEPER_CLIENT_CONNECTION_TIMEOUT))
        .build();
    options.addOption(option);

    option = Option.builder()
        .longOpt(ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT_OPTION)
        .hasArg()
        .argName(String.valueOf(DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT))
        .desc(getDescription("Number of milliseconds allowed for the zookeeper ensemble connection.", DEFAULT_ZOOKEEPER_ENSEMBLE_CONNECTION_TIMEOUT))
        .build();
    options.addOption(option);

    // required options (with a required argument)
    option = Option.builder()
        .longOpt(PATH_TO_PROPERTIES_FILE_OPTION)
        .hasArg()
        .argName("/path/to/file.properties")
        .desc("Load in the properties from this properties file (note that any of these command line flags will override the values in the properties file).")
        .required()
        .build();
    options.addOption(option);

    return options;
  }

  public void printHelp() {
    final OptionsHelpFormatter formatter = new OptionsHelpFormatter();
    formatter.setWidth(DEFAULT_WIDTH);
    formatter.setLeftPadding(HelpFormatter.DEFAULT_LEFT_PAD);
    formatter.setLeftPadding(HelpFormatter.DEFAULT_DESC_PAD);
    formatter.printHelp(
        COMMAND_STR,
        getHeader(),
        getOptionDefinitions(),
        getFooter(),
        true);
  }

  private String getDescription(final String description, final Object defaultValue) {
    return getDescription(description, defaultValue.toString());
  }

  private String getDescription(final String description, final String defaultValue) {
    final StringBuilder builder = new StringBuilder(description)
        .append(NEW_LINE);
    if(defaultValue != null) {
      builder.append(" ")
          .append("[default: ").append(defaultValue);
    }

    return builder.append("]").toString();
  }

  private String getHeader() {
    return new StringBuilder("").append(NEW_LINE)
        .append("SolrCloud Uploader - uploads data to solr for indexing.  Note")
        .append(" that the properties file is the intended means of configuring")
        .append(" the index process.  These command line flags are here only ")
        .append("to override the properties file.  NOTE: If one line in the data")
        .append(" file has a problem this application will stop processing and")
        .append(" exit.  In a case like this the current batch will be rolled back.")
        .toString();
  }
}
