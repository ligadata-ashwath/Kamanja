package com.ligadata.adapters.hdfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ligadata.adapters.AdapterConfiguration;
import com.ligadata.adapters.BufferedMessageProcessor;

public class BufferedPartitionedAvroSink implements BufferedMessageProcessor {
	static Logger logger = LogManager.getLogger(BufferedPartitionedAvroSink.class); 
	
	private String name;
	private PartitionStrategy partitioner;
	private Map<String, ArrayList<Record>> buffer;
	private AvroHDFSWriter hdfsWriter;
	private Schema schema;
	private boolean createNewFile = false;
	
	public BufferedPartitionedAvroSink() {
	}
		
	private Record json2Record(String jsonStr) throws IOException {
		DatumReader<Record> reader = new GenericDatumReader<Record>(schema);
		Decoder decoder = DecoderFactory.get().jsonDecoder(schema, jsonStr);
		return reader.read(null, decoder);
	}

	private void addRecord(Record rec) {
		String partitionKey = partitioner.getPartition(rec);
		logger.debug("partition key=[" + partitionKey + "]");
		ArrayList<Record> partition = buffer.get(partitionKey);
		if(partition == null) {
			partition = new ArrayList<Record>();
			buffer.put(partitionKey, partition);
		}
		
		partition.add(rec);
	}
	
	@Override
	public void init(AdapterConfiguration configuration) throws Exception {
		this.name = configuration.getProperty(AdapterConfiguration.FILE_PREFIX, "Log") + Thread.currentThread().getId();
		this.buffer = new HashMap<String, ArrayList<Record>>();
		logger.info("Using partition startegy: " + configuration.getProperty(AdapterConfiguration.PARTITION_STRATEGY)); 
		this.partitioner = new PartitionStrategy(configuration.getProperty(AdapterConfiguration.PARTITION_STRATEGY),
				configuration.getProperty(AdapterConfiguration.INPUT_DATE_FORMAT, "yyyy-MM-dd"));
		
		String schemaFile = configuration.getProperty(AdapterConfiguration.SCHEMA_FILE, "InstrumentationLog.avsc");
		logger.info("Using avro schema from file: " + schemaFile);
		this.schema = new Schema.Parser().parse(new File(schemaFile));

		String fileMode = configuration.getProperty(AdapterConfiguration.FILE_MODE, "append");
		this.createNewFile = "new".equalsIgnoreCase(fileMode);
		if(this.createNewFile)
			logger.info("Will create a new file in partition directory for every write.");
		else
			logger.info("Will append to a file in partition directory if the file exists.");

		this.hdfsWriter = new AvroHDFSWriter(schema, 
				configuration.getProperty(AdapterConfiguration.HDFS_URI),
				configuration.getProperty(AdapterConfiguration.FILE_COMPRESSION));
		this.hdfsWriter.setResourceFile(configuration.getProperty(AdapterConfiguration.HDFS_RESOURCE_FILE));
		this.hdfsWriter.setKeytabFile(configuration.getProperty(AdapterConfiguration.HDFS_KERBEROS_KEYTABFILE));
		this.hdfsWriter.setKerberosPrincipal(configuration.getProperty(AdapterConfiguration.HDFS_KERBEROS_PRINCIPAL));

	}

	@Override
	public boolean addMessage(String message) {
		Record record = null;
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(message);

			if (jsonObject.get("dedup") != null && "1".equals(jsonObject.get("dedup").toString())) {
				logger.debug("ignoring duplicate message.");
				return false;
			}

			record = json2Record(message);
		} catch (Exception e) {
			logger.error("Error parsing message: " + e.getMessage() + " - ignoring message : " + message, e);
		}
		
		if (record != null) {
			addRecord(record);
			return true;
		} else
			return false;
	}

	@Override
	public void processAll() throws Exception {
		for (String key : buffer.keySet()) {
			try {
				ArrayList<Record> records = buffer.get(key);
				if(records != null && records.size() > 0) {
					logger.debug("Writing partition [" + key + "]");
					String filename = createNewFile ? name + System.currentTimeMillis() + ".avro" : name + ".avro";
					if(key != null && !key.equalsIgnoreCase(""))
						filename = key + "/" + filename;
					hdfsWriter.open(filename);
					for (Record rec : records) {
						hdfsWriter.write(rec);
					}
					logger.info("Sucessfully wrote " + records.size() + " records to partition [" + key + "]");
					hdfsWriter.close();
				}
			} catch(Exception e) {
				hdfsWriter.closeAll();
				throw e;
			}
		}		
	}

	@Override
	public void clearAll() {
		buffer.clear();			
	}

	@Override
	public void close() {
	}
}