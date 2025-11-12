package main.java.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LogManager {


    /**
     *  Returns a Logger through Logger.getInstance() following Singleton design
     * @param xml to be DOM Parsed for configuration
     * @return a logger instance
     */
    public static Logger getLogger(File xml){
        Logger logger = null;
        try{
            logger = Logger.getInstance(createDocument(xml));
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.err.println("Problem retrieving a Logger instance with XML");
        }
        return logger;
    }

    /**
     *  Returns a Logger through Logger.getInstance(LogConfig) following Singleton design
     * @param json to be Data Bound for configuration using Jackson
     * @return a logger instance
     */
    public static Logger getLoggerFromJson(File json){

        Logger logger = null;
        try{
            LogConfig config = loadFromJson(json);
            logger = Logger.getInstance(config);
        } catch (Exception e) {
            System.err.println("Problem retrieving a Logger instance with JSON");
        }
        return logger;
    }

    /**
     * Calls DocumentBuilderFactory to parse an XML file
     * @param xml
     * @return Document with configuration info for the logger
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private static Document createDocument(File xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xml);
    }

    /**
     * Loads the configuration from a Json file through Jackson's ObjectMapper
     * @param json
     * @return a LogConfig object
     */
    public static LogConfig loadFromJson(File json){

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, LogConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // or throw a RuntimeException
        }    }

}
