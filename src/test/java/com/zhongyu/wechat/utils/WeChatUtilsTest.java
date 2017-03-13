package com.zhongyu.wechat.utils;

import com.zhongyu.wechat.bean.AccessToken;
import org.junit.Test;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUtilsTest {

    @Test
    public void testAccessToken() {
        AccessToken accessToken = WeChatUtils.getAccessToken();
        System.out.println(accessToken.getAccessToken());
    }

}
