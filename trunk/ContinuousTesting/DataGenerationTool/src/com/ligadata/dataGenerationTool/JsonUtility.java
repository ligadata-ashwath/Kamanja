package com.ligadata.dataGenerationTool;

import com.ligadata.dataGenerationTool.bean.ConfigObj;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;

public class JsonUtility {

    final Logger logger = Logger.getLogger(JsonUtility.class);

    public JSONObject ReadJsonFile(String messageFileString) {
        logger.info("Reading JSON file...");
        BufferedReader bufferedReader;
        JSONObject req = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(
                    messageFileString));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line).append("\n");
            }
            bufferedReader.close();
            String configJsonString = stringBuffer.toString();
            req = new JSONObject(configJsonString);
            return req;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);

        } finally {
            logger.info("Reading JSON file successful.");
        }
        return req;

    }

    public LinkedHashMap<String, String> ReadMessageFields(JSONObject req, ConfigObj configObj) {

        boolean lineAlreadyWritten;
        String value = "";
        RandomGenerator random = new RandomGenerator();
        JSONObject locs = req.getJSONObject("fields");
        JSONArray recs = locs.getJSONArray("field");
        LinkedHashMap<String, String> fields = new LinkedHashMap<String,String>();

        try {
            for (int i = 0; i < recs.length(); ++i) {
                lineAlreadyWritten = false;
                JSONObject rec = recs.getJSONObject(i);
                String fieldName = rec.getString("name");
                String fieldType = rec.getString("type");
                int fieldLength = rec.getInt("length");

//                System.out.println(rec.getString("name"));
//                System.out.println(rec.getString("type"));
//                System.out.println(rec.getInt("length"));

                // before generating random value
                if (fieldName.equalsIgnoreCase("msgname")) {
                    value = "com.ligadata.kamanja.samples.messages.SubscriberUsage";
                    lineAlreadyWritten = true;
                }

                // generate random value
                if (lineAlreadyWritten == false) {
                    value = random.GenerateRandomRecord(fieldType, fieldLength, configObj);
                }

                // after generating random value
                if (fieldName.equalsIgnoreCase("msisdn")) {
                    value = "425111" + value;
                }
                fields.put(fieldName, value);
            } // end for loop
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return fields;
    }


    public ConfigObj CreateConfigObj(JSONObject configJson) {
        logger.info("Parsing JSON object to config object...");
        ConfigObj configObj = new ConfigObj();
        configObj.setDataGenerationRate(configJson
                .getDouble("DataGenerationRate"));
        configObj.setStartDate(configJson.getString("StartDate"));
        configObj.setEndDate(configJson.getString("EndDate"));
        configObj.setDurationInHours(configJson.getDouble("DurationInHours"));
        configObj.setDropInFiles(configJson.getBoolean("DropInFiles"));
        configObj.setPushToKafka(configJson.getBoolean("PushToKafka"));
        configObj.setFileSplitPer(configJson.getString("FileSplitPer"));
        configObj.setDelimiter(configJson.getString("Delimiter"));
        configObj.setTemplatePath(configJson.getString("TemplatePath"));
        configObj.setDestiniationPath(configJson.getString("DestiniationPath"));
        configObj.setCompressFormat(configJson.getString("CompressFormat"));
        configObj.setSequenceID(configJson.getLong("SequenceId"));
        configObj.setMessageType(configJson.getString("MessageType"));
        logger.info("Value of DataGenerationRate: "
                + configJson.getDouble("DataGenerationRate"));
        logger.info("Value of StartDate: " + configJson.getString("StartDate"));
        logger.info("Value of EndDate: " + configJson.getString("EndDate"));
        logger.info("Value of DurationInHours: "
                + configJson.getDouble("DurationInHours"));
        logger.info("Value of DropInFiles: "
                + configJson.getBoolean("DropInFiles"));
        logger.info("Value of PushToKafka: "
                + configJson.getBoolean("PushToKafka"));
        logger.info("Value of FileSplitPer: "
                + configJson.getString("FileSplitPer"));
        logger.info("Value of TemplatePath: "
                + configJson.getString("TemplatePath"));
        logger.info("Value of LogFilePath: "
                + configJson.getString("DestiniationPath"));
        logger.info("Value of SequenceID: "
                + configJson.getString("SequenceId"));
        logger.info("Parsing JSON object to config object successful.");
        return configObj;
    }
}
