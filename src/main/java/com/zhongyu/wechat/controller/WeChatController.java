package com.zhongyu.wechat.controller;

import com.zhongyu.wechat.utils.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ZhongYu on 2017/3/8.
 */
@Controller
public class WeChatController {

    @GetMapping("security")
    public void doGet(HttpServletRequest request, HttpServletResponse response, String signature, String timestamp, String nonce, String echostr) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                writer.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }

    @PostMapping("security")
    public void doPost() {

    }

}
