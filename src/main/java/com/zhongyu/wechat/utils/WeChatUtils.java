package com.zhongyu.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.bean.AccessToken;
import com.zhongyu.wechat.common.RequestType;
import com.zhongyu.wechat.common.WeChatConfig;
import com.zhongyu.wechat.common.menu.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.zhongyu.wechat.common.WeChatConfig.MCHSECRET;

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

    public static String unifiedOrder(Map<String, String> params) throws Exception {
        Map<String, String> paraMap = new HashMap<>();
        String requestUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        paraMap.putAll(params);
        paraMap.put("appid", WeChatConfig.APPID);
        paraMap.put("mch_id", WeChatConfig.MCHID);
        paraMap.put("nonce_str", PayUtils.create_nonce_str());
        paraMap.put("trade_type", "JSAPI");
        paraMap.put("notify_url", WeChatConfig.NOTIFY_URL);
        String sign = PayUtils.getSign(paraMap, MCHSECRET);
        paraMap.put("sign", sign);
        String xml = MapUtils.MapToXml(paraMap);
        String xmlStr = HttpUtils.post(requestUrl, xml);
        return xmlStr;
    }

    public static void createMenu(Menu menu) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", "得到的Token");
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
