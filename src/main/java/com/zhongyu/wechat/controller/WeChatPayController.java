package com.zhongyu.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.common.WeChatConfig;
import com.zhongyu.wechat.utils.MapUtils;
import com.zhongyu.wechat.utils.PayUtils;
import com.zhongyu.wechat.utils.WeChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        logger.info(jsonObject.getString("openid"));
        logger.info(jsonObject.getString("access_token"));
        return "pay";
    }

    @ResponseBody
    @PostMapping("wx_prepay")
    public Map<String, String> nextSingle(HttpServletRequest request, String money) {
        try {
            Map<String, String> payMap = new TreeMap<String, String>();
            String openId = (String) request.getSession().getAttribute("openid");
            if (StringUtils.isBlank(openId)) {
                return null;
            }
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("body", "微信支付");
            paraMap.put("out_trade_no", PayUtils.createOrder());
            paraMap.put("total_fee", PayUtils.getMoney(money));
            paraMap.put("spbill_create_ip", PayUtils.getIpAddress(request));
            paraMap.put("openid", openId);
            String xmlStr = WeChatUtils.unifiedOrder(paraMap);
            String prepay_id = "", nonce_str = "", sign = "";
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = MapUtils.doXMLParse(xmlStr);
                logger.info(" get doXMLParse:{}", map);
                prepay_id = MapUtils.getString(map, "prepay_id");
                nonce_str = MapUtils.getString(map, "nonce_str");
                sign = MapUtils.getString(map, "sign");
            } else {
                logger.warn("=== 支付错误 failed! ===");
            }
            payMap.put("appId", WeChatConfig.APPID);
            payMap.put("timeStamp", PayUtils.createTimestamp());
            payMap.put("nonceStr", nonce_str);
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = PayUtils.getSign(payMap, WeChatConfig.MCHSECRET);
            payMap.put("pg", prepay_id);
            payMap.put("paySign", paySign);
            logger.info("===  unifiedOrder :{}  ===", payMap);
            return payMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("pay_notify")
    public String appPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            String userid = (String) request.getSession().getAttribute("userid");
            InputStream in = request.getInputStream();
            Map<String, String> map = MapUtils.doXMLParseInputStream(in);
            String return_code = MapUtils.getString(map, "return_code");
            String openid = MapUtils.getString(map, "openid");
            String out_trade_no = MapUtils.getString(map, "out_trade_no");
            String transaction_id = MapUtils.getString(map, "transaction_id");
            String total_fee = MapUtils.getString(map, "total_fee");
            String time_end = MapUtils.getString(map, "time_end");
            String is_subscribe = MapUtils.getString(map, "is_subscribe");
            System.out.println("状态码：" + return_code);
            System.out.println("用户ID：" + openid);
            System.out.println("商家订单：" + out_trade_no);
            System.out.println("微信订单：" + transaction_id);
            System.out.println("支付金额：" + total_fee + " 分");
            System.out.println("结束时间：" + time_end);
            System.out.println("是否关注：" + is_subscribe);
            logger.info(" wxshop charge callback, orderid:{}, result_code:{}, MAP:{}", out_trade_no, return_code, map);
            if (StringUtils.isBlank(out_trade_no)) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[订单不存在]]></return_msg></xml>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[解析错误]]></return_msg></xml>";
        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

}
