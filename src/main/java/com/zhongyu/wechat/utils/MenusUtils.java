package com.zhongyu.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongyu.wechat.common.menu.Menu;
import com.zhongyu.wechat.common.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class MenusUtils {

    private static Logger logger = LoggerFactory.getLogger(MenusUtils.class);

    public static void create(Menu menu) {
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
