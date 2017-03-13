package com.zhongyu.wechat.utils;

import com.zhongyu.wechat.bean.AccessToken;
import org.junit.Test;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class WeChatUtilsTest {

    @Test
    public void testAccessToken(){
        AccessToken accessToken = WeChatUtils.getAccessToken("wxa0bf65511aa963b5","8e87ae09aeea790800eed01de553f975");
        System.out.println(accessToken.getAccessToken());
    }

}
