package com.zhongyu.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.utils.WeChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZhongYu on 3/13/2017.
 */
@Controller
public class WeChatPayController {

    private static Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    @GetMapping("WeChatPay/pay/redirect")
    public String wechatCallBack(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("state") String state) {
        JSONObject jsonObject = WeChatUtils.getOAuth2AccessToken(code);
        request.getSession().setAttribute("openid", jsonObject.getString("openid"));
        request.getSession().setAttribute("access_token", jsonObject.getString("access_token"));
        return "pay";
    }



}
