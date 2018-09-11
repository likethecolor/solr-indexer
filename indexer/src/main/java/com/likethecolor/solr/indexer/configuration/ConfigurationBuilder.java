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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Parses properties file first then the command line options to create a
 * {@link Configuration} object.
 * The command line options overwrite the properties file.
 */
public class ConfigurationBuilder {
  public Configuration build(final String[] args) {
    Configuration configuration = new Configuration();

    // parse the command line
    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine;

    final IndexerCommandLineOptions indexerCommandLineOptions = new IndexerCommandLineOptions();
    try {
      final Options options = indexerCommandLineOptions.getOptionDefinitions();
      commandLine = parser.parse(options, args);

      // if help, print out the usage and exit
      printUsageAndExit(commandLine, indexerCommandLineOptions);

      // this is a required option so no need to check for existence or null
      final String pathToPropertiesFile = commandLine.getOptionValue(Constants.PATH_TO_PROPERTIES_FILE_OPTION);

      final PropertyFileConfigurationBuilder propertyFileConfigurationBuilder = new PropertyFileConfigurationBuilder(configuration);
      propertyFileConfigurationBuilder.parse(pathToPropertiesFile);
      final CommandLineConfigurationBuilder commandLineConfigurationBuilder = new CommandLineConfigurationBuilder(configuration);
      commandLineConfigurationBuilder.parse(commandLine);
    }
    catch(MissingOptionException e) {
      // If there is a {@link org.apache.commons.cli.MissingOptionException}
      // the {@link org.apache.commons.cli.CommandLine} will be null.  So the
      // args is all there is to check.
      //
      // if --help was given then no need to show an exception stack of
      // a message indicating that an argument is missing
      if(hasHelp(args)) {
        indexerCommandLineOptions.printHelp();
        return null;
      }
      e.printStackTrace();
      indexerCommandLineOptions.setFooter(e.getMessage());
      indexerCommandLineOptions.printHelp();
      configuration = null;
    }
    catch(ParseException e) {
      indexerCommandLineOptions.printHelp();
      e.printStackTrace();
      configuration = null;
    }
    return configuration;
  }

  private void printUsageAndExit(final CommandLine commandLine, final IndexerCommandLineOptions indexerCommandLineOptions) {
    if(commandLine.hasOption(Constants.HELP_OPTION)) {
      indexerCommandLineOptions.printHelp();
      System.exit(0);
    }
  }

  /**
   * Return true if the help argument was given.  If there is a
   * {@link org.apache.commons.cli.MissingOptionException} the
   * {@link org.apache.commons.cli.CommandLine} will be null.
   *
   * @param args command line args
   *
   * @return true if the help argument was given
   */
  private boolean hasHelp(final String[] args) {
    boolean hasHelp = false;
    final String helpArg = "--" + Constants.HELP_OPTION;
    if(args != null && args.length > 0) {
      for(String arg : args) {
        if(helpArg.equalsIgnoreCase(arg)) {
          hasHelp = true;
          break;
        }
      }
    }
    return hasHelp;
  }
}
