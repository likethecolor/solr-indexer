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
package com.likethecolor.solr.indexer.reader;

import com.likethecolor.solr.indexer.configuration.Configuration;
import com.likethecolor.solr.indexer.field.FieldDefinition;
import com.likethecolor.solr.indexer.field.FieldsParser;
import com.likethecolor.solr.indexer.json.JSONParser;
import com.likethecolor.solr.indexer.json.MapToListValuesBuilder;
import com.likethecolor.solr.indexer.reader.JSONDataReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doCallRealMethod;

@RunWith (MockitoJUnitRunner.class)
public class JSONDataReaderTest {
  private static final String ONE_LINE_JSON = "{\"state\":\"mo\", \"country\":\"us\", \"source\":\"RouteView\", \"version\":\"20140811T205828\", \"doc_type\":\"ASN\", \"start_ip_num\":\"100000000\", \"end_ip_num\":\"100000255\", \"weight\":\"1\", \"city\":\"st. louis\", \"latitude\":\"400\", \"longitude\":\"400\", \"asn\":\"35819\", \"asn_owner\":\"mobily-as etihad etisalat company (mobily), sa\", \"zip\":\"63130\"}";
  private static final String TWO_LINES_JSON_FIRST = "{\"state\":\"mo\", \"country\":\"us\", \"source\":\"RouteView\", \"version\":\"20140811T205828\", \"doc_type\":\"ASN\", \"start_ip_num\":\"100000000\", \"end_ip_num\":\"100000255\", \"weight\":\"1\", \"city\":\"st. louis\", \"latitude\":\"400\", \"longitude\":\"400\", \"asn\":\"35819\", \"asn_owner\":\"mobily-as etihad etisalat company (mobily), sa\", \"zip\":\"63130\"}";
  private static final String TWO_LINES_JSON_SECOND = "{\"state\":\"il\", \"country\":\"us\", \"source\":\"Maxmind\", \"version\":\"20140811T205825\", \"doc_type\":\"ASN\", \"start_ip_num\":\"1000013824\", \"end_ip_num\":\"1000079359\", \"weight\":\"1.5\", \"city\":\"decatur\", \"latitude\":\"39.1231\", \"longitude\":\"-101.3453\", \"asn\":\"4134\", \"asn_owner\":\"chinanet\", \"zip\":\"62521\"}";
  private static final String FIELDS = "asn:string;asn_owner:string;city:string;country:string;doc_type:string;end_ip_num:long;latitude:double;longitude:double;source:string;start_ip_num:long;state:string;version:string;weight:double;zip:string";

  @Mock
  private BufferedReader bufferedReaderOneLine;
  @Mock
  private BufferedReader bufferedReaderTwoLines;

  @Mock
  private JSONDataReader dataReaderOneLine;
  @Mock
  private JSONDataReader dataReaderTwoLines;
  @Mock
  private JSONDataReader dataReaderTwoLinesGetHeader;


  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);

    Configuration configuration = getConfiguration();

    // set up the rows of data returned from the buffered reader
    given(bufferedReaderOneLine.readLine())
        .willReturn(ONE_LINE_JSON)
        .willReturn(null);

    given(bufferedReaderTwoLines.readLine())
        .willReturn(TWO_LINES_JSON_FIRST)
        .willReturn(TWO_LINES_JSON_SECOND)
        .willReturn(null);

    // data reader using buffered reader that returns 1 row
    doCallRealMethod().when(dataReaderOneLine).read(null);
    doCallRealMethod().when(dataReaderOneLine).readLine();
    given(dataReaderOneLine.getBufferedReader())
        .willReturn(bufferedReaderOneLine);
    given(dataReaderOneLine.getJsonParser())
        .willReturn(new JSONParser(configuration));

    // data reader using buffered reader that returns 2 rows
    doCallRealMethod().when(dataReaderTwoLines).read(null);
    doCallRealMethod().when(dataReaderTwoLines).readLine();
    given(dataReaderTwoLines.getBufferedReader())
        .willReturn(bufferedReaderTwoLines);
    given(dataReaderTwoLines.getJsonParser())
        .willReturn(new JSONParser(configuration));

    // data reader using buffered reader that returns 2 rows
    // expecting that the getHeader method will be called as the first method, then read
    doCallRealMethod().when(dataReaderTwoLinesGetHeader).read(null);
    doCallRealMethod().when(dataReaderTwoLinesGetHeader).readLine();
    doCallRealMethod().when(dataReaderTwoLinesGetHeader).getHeader(anyBoolean());
    given(dataReaderTwoLinesGetHeader.getBufferedReader())
        .willReturn(bufferedReaderTwoLines);
    given(dataReaderTwoLinesGetHeader.getJsonParser())
        .willReturn(new JSONParser(configuration));

    // basic Configuration set up used in all tests
    configuration.setMultivalueFieldDelimiter("\u0002");
    configuration.setFields(FIELDS);
    Map<String, FieldDefinition> fieldDefinitionMap = new FieldsParser(configuration).parse(configuration.getFields());
    MapToListValuesBuilder mapToListValuesBuilder = new MapToListValuesBuilder(fieldDefinitionMap);
    given(dataReaderOneLine.getMapToListValuesBuilder())
        .willReturn(mapToListValuesBuilder);
    given(dataReaderTwoLines.getMapToListValuesBuilder())
        .willReturn(mapToListValuesBuilder);
    given(dataReaderTwoLinesGetHeader.getMapToListValuesBuilder())
        .willReturn(mapToListValuesBuilder);
  }

  @Test
  public void testRead_OneRow() throws IOException {
    List<Object> row;
    List<List<Object>> rows = new ArrayList<>();
    while((row = dataReaderOneLine.read(null)) != null) {
      rows.add(row);
    }
    assertEquals(1, rows.size());
    row = rows.get(0);

    assertEquals("35819", row.get(0));
    assertEquals("mobily-as etihad etisalat company (mobily), sa", row.get(1));
    assertEquals("st. louis", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("100000255", row.get(5));
    assertEquals("400", row.get(6));
    assertEquals("400", row.get(7));
    assertEquals("RouteView", row.get(8));
    assertEquals("100000000", row.get(9));
    assertEquals("mo", row.get(10));
    assertEquals("20140811T205828", row.get(11));
    assertEquals("1", row.get(12));
    assertEquals("63130", row.get(13));
  }

  @Test
  public void testRead_TwoRows() throws IOException {
    List<Object> row;
    List<List<Object>> rows = new ArrayList<>();
    while((row = dataReaderTwoLines.read(null)) != null) {
      rows.add(row);
    }
    assertEquals(2, rows.size());
    row = rows.get(0);
    assertEquals("35819", row.get(0));
    assertEquals("mobily-as etihad etisalat company (mobily), sa", row.get(1));
    assertEquals("st. louis", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("100000255", row.get(5));
    assertEquals("400", row.get(6));
    assertEquals("400", row.get(7));
    assertEquals("RouteView", row.get(8));
    assertEquals("100000000", row.get(9));
    assertEquals("mo", row.get(10));
    assertEquals("20140811T205828", row.get(11));
    assertEquals("1", row.get(12));
    assertEquals("63130", row.get(13));

    row = rows.get(1);
    assertEquals("4134", row.get(0));
    assertEquals("chinanet", row.get(1));
    assertEquals("decatur", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("1000079359", row.get(5));
    assertEquals("39.1231", row.get(6));
    assertEquals("-101.3453", row.get(7));
    assertEquals("Maxmind", row.get(8));
    assertEquals("1000013824", row.get(9));
    assertEquals("il", row.get(10));
    assertEquals("20140811T205825", row.get(11));
    assertEquals("1.5", row.get(12));
    assertEquals("62521", row.get(13));
  }

  @Test
  public void testGetHeader_Read_TwoRows() throws IOException {
    Object[] header = dataReaderTwoLinesGetHeader.getHeader(true);
    List<Object> row;
    List<List<Object>> rows = new ArrayList<>();
    while((row = dataReaderTwoLinesGetHeader.read(null)) != null) {
      rows.add(row);
    }

    assertEquals("35819", header[0]);
    assertEquals("mobily-as etihad etisalat company (mobily), sa", header[1]);
    assertEquals("st. louis", header[2]);
    assertEquals("us", header[3]);
    assertEquals("ASN", header[4]);
    assertEquals("100000255", header[5]);
    assertEquals("400", header[6]);
    assertEquals("400", header[7]);
    assertEquals("RouteView", header[8]);
    assertEquals("100000000", header[9]);
    assertEquals("mo", header[10]);
    assertEquals("20140811T205828", header[11]);
    assertEquals("1", header[12]);
    assertEquals("63130", header[13]);

    assertEquals(1, rows.size());
    row = rows.get(0);
    assertEquals("4134", row.get(0));
    assertEquals("chinanet", row.get(1));
    assertEquals("decatur", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("1000079359", row.get(5));
    assertEquals("39.1231", row.get(6));
    assertEquals("-101.3453", row.get(7));
    assertEquals("Maxmind", row.get(8));
    assertEquals("1000013824", row.get(9));
    assertEquals("il", row.get(10));
    assertEquals("20140811T205825", row.get(11));
    assertEquals("1.5", row.get(12));
    assertEquals("62521", row.get(13));
  }

  @Test
  public void testReadCalledThenGetHeader_TwoRows() throws IOException {
    List<Object> row;
    List<List<Object>> rows = new ArrayList<>();
    while((row = dataReaderTwoLinesGetHeader.read(null)) != null) {
      rows.add(row);
    }

    Object[] header = dataReaderTwoLinesGetHeader.getHeader(true);
    assertEquals("35819", header[0]);
    assertEquals("mobily-as etihad etisalat company (mobily), sa", header[1]);
    assertEquals("st. louis", header[2]);
    assertEquals("us", header[3]);
    assertEquals("ASN", header[4]);
    assertEquals("100000255", header[5]);
    assertEquals("400", header[6]);
    assertEquals("400", header[7]);
    assertEquals("RouteView", header[8]);
    assertEquals("100000000", header[9]);
    assertEquals("mo", header[10]);
    assertEquals("20140811T205828", header[11]);
    assertEquals("1", header[12]);
    assertEquals("63130", header[13]);

    assertEquals(2, rows.size());
    row = rows.get(0);
    assertEquals("35819", row.get(0));
    assertEquals("mobily-as etihad etisalat company (mobily), sa", row.get(1));
    assertEquals("st. louis", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("100000255", row.get(5));
    assertEquals("400", row.get(6));
    assertEquals("400", row.get(7));
    assertEquals("RouteView", row.get(8));
    assertEquals("100000000", row.get(9));
    assertEquals("mo", row.get(10));
    assertEquals("20140811T205828", row.get(11));
    assertEquals("1", row.get(12));
    assertEquals("63130", row.get(13));

    row = rows.get(1);
    assertEquals("4134", row.get(0));
    assertEquals("chinanet", row.get(1));
    assertEquals("decatur", row.get(2));
    assertEquals("us", row.get(3));
    assertEquals("ASN", row.get(4));
    assertEquals("1000079359", row.get(5));
    assertEquals("39.1231", row.get(6));
    assertEquals("-101.3453", row.get(7));
    assertEquals("Maxmind", row.get(8));
    assertEquals("1000013824", row.get(9));
    assertEquals("il", row.get(10));
    assertEquals("20140811T205825", row.get(11));
    assertEquals("1.5", row.get(12));
    assertEquals("62521", row.get(13));
  }

  @Test
  public void testRead_BufferedReaderIsNull() throws IOException {
    given(dataReaderOneLine.getBufferedReader())
        .willReturn(null);

    List<Object> row;
    List<List<Object>> rows = new ArrayList<>();
    while((row = dataReaderOneLine.read(null)) != null) {
      rows.add(row);
    }
    assertEquals(0, rows.size());
  }

  private Configuration getConfiguration() {
    String fields2JSONMapString = "aol:AOL;anonymizer_status:AnonymizerStatus;area_code:AreaCode;connection:Connection;continent:Continent;dma:DMA;ip_address:IPAddress;ip_routing_type:IPRoutingType;ip_type:IPType;line_speed:LineSpeed;msa:MSA;pmsa:PMSA;region:Region;second_level_domain:SecondLevelDomain;timezone:TimeZone;top_level_domain:TopLevelDomain;asn:asn;asn_owner:asn_owner;city:city;country:country;doc_type:doc_type;end_ip_num:end_ip_num;latitude:latitude;longitude:longitude;source:source;start_ip_num:start_ip_num;state:state;version:version;weight:weight;zip:zip;ip_range_id:CALCULATED;subnet_class:CALCULATED";
    Configuration configuration = new Configuration();
    configuration.setFieldsToJSON(fields2JSONMapString);
    return configuration;
  }
}
