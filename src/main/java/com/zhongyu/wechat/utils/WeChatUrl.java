package com.zhongyu.wechat.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUrl {

    private static Properties wechatProperties;

    private static synchronized void loadProperties() {
        if (null == wechatProperties) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = WeChatUrl.class.getClassLoader().getResourceAsStream("wechat.properties");
                properties.load(inputStream);
                wechatProperties = properties;
            } catch (Exception e) {
                throw new RuntimeException("未找到微信URL配置文件.");
            }
        }
    }

    public static String get(String key) {
        loadProperties();
        return wechatProperties.getProperty(key);
    }

}
