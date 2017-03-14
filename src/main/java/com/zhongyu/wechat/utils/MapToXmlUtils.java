package com.zhongyu.wechat.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZhongYu on 3/14/2017.
 */
public class MapToXmlUtils {

    public static String MapToXml(Map<String, String> map) {
        return MapToXmlWithTag(map, "xml");
    }

    public static String MapToXmlWithTag(Map<String, String> map, String tag) {
        String xml = "<" + tag + ">";
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            xml += "<" + key + ">" + val + "</" + key + ">";
        }
        xml += "</" + tag + ">";
        return xml;
    }

    public static Map<String, String> doXMLParse(String xml) throws XmlPullParserException, IOException {
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Map<String, String> map = null;
        XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
        pullParser.setInput(inputStream, "UTF-8");
        int eventType = pullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    map = new HashMap<String, String>();
                    break;
                case XmlPullParser.START_TAG:
                    String key = pullParser.getName();
                    if (key.equals("xml"))
                        break;
                    String value = pullParser.nextText();
                    map.put(key, value);
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = pullParser.next();
        }
        return map;
    }

    public static Map<String, String> doXMLParseInputStream(InputStream inputStream) throws XmlPullParserException, IOException {
        Map<String, String> map = null;
        XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
        pullParser.setInput(inputStream, "UTF-8");
        int eventType = pullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    map = new HashMap<String, String>();
                    break;
                case XmlPullParser.START_TAG:
                    String key = pullParser.getName();
                    if (key.equals("xml"))
                        break;
                    String value = pullParser.nextText();
                    map.put(key, value);
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = pullParser.next();
        }
        return map;
    }

}
