package com.zhongyu.wechat.common;

import com.zhongyu.wechat.utils.WeChatProperties;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatConfig {

    public static final String APPID = WeChatProperties.get("AppId");

    public static final String APPSECRET = WeChatProperties.get("AppSecret");

    public static final String TOKEN = WeChatProperties.get("Token");

}
