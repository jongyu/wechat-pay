package com.zhongyu.wechat.controller;

import com.zhongyu.wechat.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZhongYu on 3/12/2017.
 */
@Controller
public class WeChatController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("security")
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "signature", required = true) String signature,
                      @RequestParam(value = "timestamp", required = true) String timestamp,
                      @RequestParam(value = "nonce", required = true) String nonce,
                      @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            logger.info("开始签名校验");
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                response.getWriter().println(echostr);
                logger.info("签名校验通过");
            } else {
                logger.info("签名校验失败");
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    @PostMapping("security")
    public void doPost(){

    }

}
