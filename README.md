# LikeTheColor SOLR Indexer
## About
The LikeTheColor SOLR Indexer uploads data to SOLR for indexing from either a CSV or JSON data file.  With data files coming from a variety of sources (e.g., third parties, ETL pipelines) and formats I found the need to create this indexer.  There are a lot of options that allows for tweaking how the indexer works.

This indexer requires that SOLR be in "cloud" mode as it connects to ZooKeeper to figure where to send the data.

The indexer uses both a properties file and command line options.  The properties file is intended to be for common settings while the command line options allow you to override the values in the properties file.  To make things easier the names of the command line options are the same as the property keys.

Unlike the Data Import Handler this indexer does not replace entire documents.  Rather, it will update individual fields.  Set the `uniqueKey` value in the schema and use that field to specify the document to be updated.

**Be sure to check out the [ChangeLog](CHANGELOG.md)**

## Build
This project uses [Apache Maven](https://maven.apache.org/) to build. To build and install into your local Maven repository:

	mvn clean install

Then in the final artifact will be located here: `assembler/target/ltc-solr-indexer-1.0.jar`

## Binary
A binary version is included with this repository in the `dist` directory.

## Properties/Command Line Arguments
There are sample properties files in the repository under `indexer/sample-properties`

The command line arguments are the same as the property names prepended with two hyphens (e.g., `--batch-size 5000`).  The use of an equals sign on command line is optional (e.g., `--batch-size 5000` is the same as `--batch-size=5000`).

### batch-size
* data type: integer
* default value: 1000

This tells the indexer how many documents to gather before being sent to SOLR for indexing.  

Be careful with this value:

* The batch of documents are in memory.  Setting the value too high will use too much memory.  SOLR has to process the batch so a batch size that is too large may tax the SOLR server too much.

Note that the indexer explicitly sets `CloudSolrClient.Builder#withParallelUpdates(boolean)` to true.  When this is true each batch is split by the number of servers and each are sent to a server in a separate thread.

### collection-name
* data type: string
* default value: n/a

The name of the collection to be indexed (e.g., "my-collection.sept2018" not something like "my-collection.sept2018\_shard1\_replica2").  This can also be an alias.

### csv-delimiter
* data type: character
* default value: `,`

If the `data-type` value is `csv` this value is the character that separates each column.  Note: This must be a single character.  If more than one character is given only the first (left-most) character will be used.

### csv-quote-character
* data type: character
* default value: `"`

If the `data-type` value is `csv` the file containing data to index may have columns that contain the `csv-delimiter` character.  To indicate that the `csv-delimiter` character is data and not a column delimiter the data should be surrounded by the `csv-quote-character`.

For example, take this row of data:

	"first column","second column contains,  a delimiter","third column"

The second column of data contains a `csv-delimiter`.  So that the system parses the second column as `"second column contains, a delimiter"` the data is surrounded by the `csv-quote-character`.

### data-type
* data type: string
* default value: n/a

	**REQUIRED**

Tells the indexer the format of the source data file.  The value can be either `csv` or `json`.

### dynamic-fields
* data type: list
* default value: n/a

This option provides a mechanism for dynamically generating values.  There is a sample class that shows a little bit how to do this (`com.likethecolor.solr.indexer.dynamic.SampleDynamicClass`).  In the sample-properties you'll find how to configure it.

### fields
* data type: list
* default value: n/a

The system does not assign meaning to any meta data in the data file (e.g., first row containing column names).  It only knows about position.  The value of the `fields` property is a list of field names, one is required for each column in the data file.  If the field contains data to be indexed the field name must be the name of the SOLR field to which the data corresponds.  If there are columns in the data file that are to be skipped (not indexed) the field name can be anything (see `skip-fields`).

This list may also contain data types and, in the case of datetime data type, the format.

Valid data types:

	datetime
	double
	int
	long
	multivalued
	string

Format: `solr_field_name0:data_type[:format];solr_field_name1:data_type[;...]`

Example:  A SOLR schema like this:

	<field name="isbn" type="exact_match_equals" ... multiValued="false" />
	<field name="doc_id" type="long" ... multiValued="false" />
	<field name="create_dt" type="date" ... multiValued="false" />
	<field name="doc_name" type="text" ... multiValued="false" />
	<field name="doc_url" type="string" ... multiValued="false" />
	<field name="doc_test" type="string" ... multiValued="true" />

A CSV file containing these columns:

	"ID","Code","Create DateTime","Update DateTime","Name","URL","Country","Currency","Language","Mobile","Multivalue Test"

Might have a fields value like this:

	doc_id:long;isbn:string;create_dt:datetime:MM/dd/yyyy hh:mm:ssa;update_dt:datetime:MM/dd/yyyy hh:mm:ssa;doc_name:string;doc_url:string;country:string;currency:string;language:string;mobile:string;doc_test:multivalued

In this case, only these columns are indexed: `doc_id, isbn, doc_name, doc_url, doc_test`

**Note**: If data_type is not given string is assumed.

**Note2**: If the data type is `datetime` and the date format is not given the date format assumed is `MM/dd/yyyy hh:mm:ssa` (see Java's `SimpleDateFormat`)

**Note3**: Even if a column is to be skipped a data type should be used.  This will avoid exceptions with data type recognition.

### first-row-is-header
* data type: boolean
* default value: false

Use this option if the first row in the file containing the data to be parsed is a header row.  That is, the row contains column names and not data to be parsed.

### help
* data type: boolean
* default value: false

Use this argument to receive a print out of all the options available and details about them.

### literals
* data type: list
* default value: n/a

There will be times where it will be useful to include literal values in the index.  The value of this option is a list of SOLR field names, data types, and the default values.

For example, say an index contains multiple types and each of those types has a sort order.  The value for literals might be:

	type:string:periodical;type_sort:int:3

What this tells the indexer is that for every document created use the value "periodical" for the SOLR field `type` and parse it as a `string` data type.  Similarly, every document should use the value "3" for the SOLR field `type_sort` and parse it as an `int` data type.

### multivalue-field-delimiter
* data type: string
* default value: `,`

For those fields whose data type is `multivalued` use this string as the delimiter for that field.  For example, say the data to be parsed is "data0^Bdata1^Bdata2" the `multivalue-field-delimiter` should have a value of ^B (that is control-b or \u0002).  If the value is in the properties file set the value as is (that is, e.g., literally \u0002).  If it is set on the command line be sure to surround the value with double quotes if the value could be interpreted by the shell.

### optimize-index
* data type: boolean
* default value: false

Use this argument if the indexer should optimized the index after indexing completes.  This has a default value of `false` because optimization is a costly operation and the user should be certain that s/he wants it to be done.

### path-to-data-file
* data type: string
* default value: n/a

	**REQUIRED** [either this or `path-to-json-data-file`]

This is a required argument and the value should be the path to the CSV data file to be indexed.

### path-to-json-data-file
* data type: string
* default value: n/a

	**REQUIRED** [either this or `path-to-data-file`]

This is a required argument and the value should be the path to the JSON data file to be indexed.

### path-to-properties-file
* data type: string
* default value: n/a

	**REQUIRED ON THE COMMAND LINE**

This is an argument that is required on the command line and should point to the properties file.  The system is designed so that a properties file is the main configuration where command line arguments are only there to alter values in the properties file.

### retry-count
* data type: integer
* default value: 4

If there is an error uploading the data to SOLR the system will retry.  If the number of attempts exceeds this value the system will throw an exception and exit with an exit code of 1.

Note that a value of < 0 will set the value to the default value.

### skip-fields
* data type: list
* default value: n/a

This option ties into the `fields` option and is a list of field names that should **not** be indexed.  A name in this list should match the field name exactly.

Example: Say the list of values in the fields option are: 

	doc_id:long;a_date:datetime:isbn:string;foo:string;bar:string

The skip-fields value could be: `a_date;foo;bar`

This would tell the indexer to index `doc_id` and `isbn` but do not index `a_date`, `foo`, or `bar`.

The fields to be skipped listed in the fields option can have any name as long as the same names appear in the `skip-fields` option.

### sleep-millis-between-retries
* data type: long
* default value: 5000

If there is an error uploading the data to SOLR the system will retry.  Between each retry the system will sleep this number of milliseconds.

**Note** that a value of `< 0` will set the value to `0`

### soft-commit-frequency
* data type: integer
* default value: 5000

The indexer will perform a soft commit after so many batches.  Value of `0` means never make soft commits.  A value `>= 1` means do a soft commit after that many batches.

### thread-count
* data type: integer
* default value: 4

Each batch sent to SOLR in a thread.  This value is the number of threads in the thread pool.

**Note** that a value of `<= 0` will set the value to the minimum value of `1`.

### unique-key-field-name
* data type: string
* default value: `id`

SOLR should have a unique key field that tells it how to uniquely identify a document.  This is the name of that field.

### unique-key-field-value
* data type: list
* default value: n/a

In some data sets the `unique-key-field-name` can be composed of multiple fields.  The value of this option is the list of field names from `fields` and/or from `literals`.  During the indexing process the values of the fields in this list will be concatenated, in the same order as they appear in this list, and be used as the value for the `unique-key-field-name`.

Example: Say there is this configuration:

	fields=doc_id:long;a_date:datetime:isbn:string;foo:string;bar:string
	literals=type:string:periodical;type_sort:int:3
	unique-key-field-name=id
	unique-key-field-value=doc_id;type

During indexing the value of the SOLR `id` field will be whatever the value of `doc_id` is concatenated with `type`.  The value of `doc_id` comes from the data being indexed.  The value of `type` comes from the literal value `periodical` since `type` appears in the `literals` property.

### unique-key-field-value-delimiter
* data type: string
* default value: `-`

This value will be used to delimit the values defined in `unique-key-field-value`.

Example: Say there is this configuration:

	fields=doc_id:long;a_date:datetime:isbn:string;foo:string;bar:string
	literals=type:string:periodical;type_sort:int:3
	unique-key-field-name=id
	unique-key-field-value=doc_id;type
	unique-key-field-value-delimiter=-+-

During indexing the value of the SOLR `id` field will be whatever the value of `doc_id` is concatenated with `type` (e.g., `123-+-periodical`).  The value of `doc_id` comes from the data being indexed.  The value of `type` comes from the literal value `periodical`.

### zookeeper-client-connection-timeout
* data type: integer
* default value: 1000

This is "the timeout to the zookeeper ensemble in milliseconds".

### zookeeper-ensemble-connection-timeout
* data type: integer
* default value: 1000

This is "the connect timeout to the zookeeper ensemble in milliseconds".

### zookeeper-host
* data type: list
* default value: n/a

This is a list of hosts and ports that zookeeper is running on.  A comma separated host:port pairs, each corresponding to a zookeeper server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002"  If the optional chroot suffix is used the example would look like: "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a" where the client would be rooted at "/app/a" and all paths would be relative to this root - i.e., getting/setting/etc... "/foo/bar" would result in operations being run on "/app/a/foo/bar" (from the server perspective).

## Example command line
### Help/Usage Message

	java -jar ltc-solr-indexer-<version>.jar --help

### Index
	java -jar ltc-solr-indexer-<version>.jar --data-type csv --path-to-properties-file /path/to/file.properties

## Authors
Dan Brown [dan@likethecolor.com](dan@likethecolor.com)

## Contributing
1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new pull request