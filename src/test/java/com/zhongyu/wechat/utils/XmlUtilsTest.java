package com.zhongyu.wechat.utils;

import com.zhongyu.wechat.bean.UnifiedOrder;
import com.zhongyu.wechat.common.WeChatConfig;
import org.junit.Test;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class XmlUtilsTest {

    @Test
    public void test() {
        UnifiedOrder unifiedOrder = new UnifiedOrder();
        unifiedOrder.setAppid(WeChatConfig.APPID);
        unifiedOrder.setAttach("支付测试");
        unifiedOrder.setBody("JSAPI支付测试");
        unifiedOrder.setMch_id(WeChatConfig.MCHID);
        unifiedOrder.setNonce_str(WeChatUtils.getNonceStr());
        unifiedOrder.setNotify_url(WeChatConfig.NOTIFYURL);
    }

}
