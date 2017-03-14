package com.zhongyu.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.bean.AccessToken;
import com.zhongyu.wechat.common.RequestType;
import com.zhongyu.wechat.common.WeChatConfig;
import com.zhongyu.wechat.common.menu.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUtils {

    private static Logger logger = LoggerFactory.getLogger(WeChatUtils.class);

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

    public static void createMenu(Menu menu) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", "-wxM51bzfD3DI01esFmUf1foextzOXaC-JQg3FI6nny99NYVHA6-E-3ff2i2i00yxGU7DuYs3aW06DnhiSzzGzGZlg3lgO_KHIhdyWqIRW8ZwS4fnvMeKCrLdlCbEuGzCOTaAJALXC");
        JSONObject jsonObject = HttpUtils.httpRequest(requestUrl, String.valueOf(RequestType.POST), JsonMapper.nonEmptyMapper().toJson(menu));
        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                logger.error("创建菜单失败! errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            } else {
                logger.info("创建菜单成功!");
            }
        }
    }

}
