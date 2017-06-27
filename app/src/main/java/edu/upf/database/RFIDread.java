package edu.upf.database;

/**
 * Created by rober on 15/06/2017.
 */
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RFIDread {
    public static String[] returnTags(String url) throws InterruptedException {
        InputStream devices = getURL(url);
        Document xml = parseXML(devices);
        String deviceID = xml.getElementsByTagName("id").item(0).getTextContent();
        //System.out.println(deviceID);
        return getTags(deviceID);
    }

    public static String[] getTags(String deviceID) {
        String url = "http://localhost:3161/devices/" + deviceID + "/inventory";
        Document xml = parseXML(getURL(url));
        NodeList tags = xml.getElementsByTagName("epc");
        String[] tagsEPC = new String[tags.getLength()];
        for(int i=0;i<tags.getLength();i++) {
            tagsEPC[i] = tags.item(i).getTextContent();
        }
        return tagsEPC;
    }

    public static void startDevice(String deviceID) {
        getURL("http://localhost:3161/devices/" + deviceID + "/start");
    }

    public static InputStream getURL(String url) {
        InputStream output = null;
        try {
            return output = new URL(url).openStream();
        }
        catch(Exception e) {
            e.printStackTrace();
            return output;
        }
    }

    public static Document parseXML(InputStream xml_input) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xml;
        try {
            builder = factory.newDocumentBuilder();
            xml = builder.parse(xml_input);
            return xml;
        }
        catch(Exception e) {
            e.printStackTrace();
            return xml = null;
        }

    }

}
