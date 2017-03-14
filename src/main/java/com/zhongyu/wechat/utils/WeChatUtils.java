package com.zhongyu.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.bean.AccessToken;
import com.zhongyu.wechat.common.RequestType;
import com.zhongyu.wechat.common.WeChatConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUtils {

    private static Logger logger = LoggerFactory.getLogger(WeChatUtils.class);

    /**
     * 获取微信access_token
     */
    public static AccessToken getAccessToken() {
        AccessToken accessToken = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        requestUrl = requestUrl.replace("APPID", WeChatConfig.APPID).replace("APPSECRET", WeChatConfig.APPSECRET);
        JSONObject jsonObject = HttpUtils.httpRequest(requestUrl, String.valueOf(RequestType.GET), null);
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setAccessToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
                logger.info(String.valueOf(accessToken));
            } catch (Exception e) {
                logger.error("获取access_token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    public static JSONObject getOAuth2AccessToken(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", WeChatConfig.APPID).replace("SECRET", WeChatConfig.APPSECRET).replace("CODE", code);
        JSONObject jsonObject = HttpUtils.httpRequest(requestUrl, String.valueOf(RequestType.GET), null);
        if (null != jsonObject) {
            return jsonObject;
        }
        return null;
    }

    public static String unifiedOrder(Map<String, String> params) throws Exception {
        String requestUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        Map<String, String> map = new HashMap<>();
        map.putAll(params);
        map.put("appid", WeChatConfig.APPID);
        map.put("mch_id", WeChatConfig.MCHID);
        map.put("nonce_str", getNonceStr());
        map.put("trade_type", "JSAPI");
        map.put("notify_url", WeChatConfig.DOMAIN);
        map.put("out_trade_no", createOrder());
        map.put("sign", getSign(map, WeChatConfig.MCHSECRET));
        String xml = MapToXml(map);
        String xmlStr = HttpUtils.post(requestUrl, xml);
        return xmlStr;
    }

    public static String createOrder() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder stringBuilder = new StringBuilder(format.format(new Date()));
        for (int i = 0; i < 1; i++)
            stringBuilder.append(new Random().nextInt(2));
        return stringBuilder.toString();
    }

    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    public static String getNonceStr() {
        String currTime = getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = createRandom(4) + "";
        return strTime + strRandom;
    }

    public static int createRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getSign(Map<String, String> params, String appkey) {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            temp.append(valueString);
        }
        String stringSignTemp = temp.toString() + "&key=" + appkey;
        String signValue = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
        return signValue;
    }

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
