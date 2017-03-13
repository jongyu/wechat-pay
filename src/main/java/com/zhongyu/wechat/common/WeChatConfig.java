package com.zhongyu.wechat.common;

import com.zhongyu.wechat.utils.WeChatProperties;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatConfig {

    public static final String APPID = WeChatProperties.get("AppId");

    public static final String APPSECRET = WeChatProperties.get("AppSecret");

    public static final String TOKEN = WeChatProperties.get("Token");

    public static final String MCHID = WeChatProperties.get("MchId");

    public static final String APIKEY = WeChatProperties.get("ApiKey");

    public static final String DOMAIN = WeChatProperties.get("Domain");

}
