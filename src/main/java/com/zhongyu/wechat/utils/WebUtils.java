package com.zhongyu.wechat.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZhongYu on 3/24/2017.
 */
public class WebUtils {

    public static String getURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(uri.lastIndexOf("/")) + 1;
    }

    public static String getWebRootPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

}
