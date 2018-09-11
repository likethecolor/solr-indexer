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
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ConfigurationBuilderTest {
  @Test
  public void testBuild() {
    String relativePath = ConfigurationBuilderTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    String propertiesFile = relativePath + "/configuration.properties";
    String dataFile = "/path/to/data.csv";
    long softCommitFrequency = 10;
    String softCommitFrequencyString = String.valueOf(softCommitFrequency);
    
    final ByteArrayOutputStream standardOut = new ByteArrayOutputStream();
    final ByteArrayOutputStream standardErr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(standardOut));
    System.setErr(new PrintStream(standardErr));

    String[] args = new String[]{
        "--" + Constants.PATH_TO_DATA_FILE_OPTION, dataFile,
        "--" + Constants.PATH_TO_PROPERTIES_FILE_OPTION, propertiesFile,
        "--" + Configuration.SOFT_COMMIT_FREQUENCY, softCommitFrequencyString
    };

    ConfigurationBuilder builder = new ConfigurationBuilder();
    Configuration configuration = builder.build(args);

    assertNotNull(configuration);
    assertEquals(propertiesFile, configuration.getPathToPropertiesFile());
    assertEquals(dataFile, configuration.getPathToDataFile());
    assertEquals(softCommitFrequency, configuration.getSoftCommitFrequency().longValue());
    assertTrue(standardErr.toString().equals(""));
    assertTrue(standardOut.toString().equals(""));
  }

  @Test
  public void testBuild_MissingOption() {
    final ByteArrayOutputStream standardOut = new ByteArrayOutputStream();
    final ByteArrayOutputStream standardErr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(standardOut));
    System.setErr(new PrintStream(standardErr));

    String[] args = new String[]{
        "--" + Constants.PATH_TO_DATA_FILE_OPTION, "/path/file.csv"
    };

    ConfigurationBuilder builder = new ConfigurationBuilder();
    Configuration configuration = builder.build(args);

    assertNull(configuration);
    assertTrue(standardErr.toString().contains("MissingOptionException"));
    assertTrue(standardErr.toString().contains("Missing required option: " + Constants.PATH_TO_PROPERTIES_FILE_OPTION));
    assertTrue(standardOut.toString().contains("Missing required option: " + Constants.PATH_TO_PROPERTIES_FILE_OPTION));
  }

  @Test
  public void testBuild_Help() {
    final ByteArrayOutputStream standardOut = new ByteArrayOutputStream();
    final ByteArrayOutputStream standardErr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(standardOut));
    System.setErr(new PrintStream(standardErr));

    String[] args = new String[]{"--" + Constants.HELP_OPTION};

    ConfigurationBuilder builder = new ConfigurationBuilder();
    Configuration configuration = builder.build(args);

    assertNull(configuration);
    assertTrue("".equals(standardErr.toString()));
    assertTrue(!standardOut.toString().contains("MissingOptionException"));
  }


  @Test
  public void testBuild_MissingOption_WithHelp() {
    final ByteArrayOutputStream standardOut = new ByteArrayOutputStream();
    final ByteArrayOutputStream standardErr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(standardOut));
    System.setErr(new PrintStream(standardErr));

    String[] args = new String[]{
        "--" + Constants.PATH_TO_DATA_FILE_OPTION, "/path/file.csv",
        "--" + Constants.HELP_OPTION
    };

    ConfigurationBuilder builder = new ConfigurationBuilder();
    Configuration configuration = builder.build(args);

    assertNull(configuration);
    assertTrue("".equals(standardErr.toString()));
    assertTrue(!standardOut.toString().contains("Missing required option: " + Constants.PATH_TO_PROPERTIES_FILE_OPTION));
  }

  @Test
  public void testBuild_ParseException() {
    final ByteArrayOutputStream standardOut = new ByteArrayOutputStream();
    final ByteArrayOutputStream standardErr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(standardOut));
    System.setErr(new PrintStream(standardErr));

    String[] args = new String[]{"---"
    };

    ConfigurationBuilder builder = new ConfigurationBuilder();
    Configuration configuration = builder.build(args);

    assertNull(configuration);
    assertTrue(standardErr.toString().contains("UnrecognizedOptionException"));
    assertTrue(standardErr.toString().contains("Unrecognized option: ---"));
    assertTrue(!standardOut.toString().equals(""));
    assertTrue(!standardOut.toString().contains("Unrecognized"));
  }
}
