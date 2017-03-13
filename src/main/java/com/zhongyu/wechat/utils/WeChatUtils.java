package com.zhongyu.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.bean.AccessToken;
import com.zhongyu.wechat.common.RequestType;
import com.zhongyu.wechat.common.WeChatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUtils {

    private static Logger logger = LoggerFactory.getLogger(WeChatUtils.class);

    /**
     * 获取微信access_token
     *
     * @return
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

}
