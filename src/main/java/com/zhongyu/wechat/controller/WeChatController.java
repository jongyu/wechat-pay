package com.zhongyu.wechat.controller;

import com.zhongyu.wechat.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ZhongYu on 3/12/2017.
 */
@Controller
public class WeChatController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("security")
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "signature", required = true) String signature,
                      @RequestParam(value = "timestamp", required = true) String timestamp,
                      @RequestParam(value = "nonce", required = true) String nonce,
                      @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter writer = response.getWriter();
                writer.print(echostr);
                writer.close();
            } else {
                logger.info("存在非法请求!");
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

}
