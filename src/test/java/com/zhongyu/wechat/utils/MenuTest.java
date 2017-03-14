package com.zhongyu.wechat.utils;

import com.zhongyu.wechat.common.menu.Menu;
import com.zhongyu.wechat.common.menu.MenuItem;
import com.zhongyu.wechat.common.menu.MenuType;
import org.junit.Test;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class MenuTest {

    @Test
    public void testCreate() {
        Menu menu = new Menu();

        MenuItem one = new MenuItem().url("https://chungyu.vip/").type(MenuType.view).name("个人博客");
        menu.add(one);


        MenuItem two = new MenuItem().name("菜单二");
        MenuItem two_1 = new MenuItem().key("key1").type(MenuType.click).name("点击");
        MenuItem two_2 = new MenuItem().url("https://chungyu.vip/").type(MenuType.view).name("个人博客");
        two.add(two_1);
        two.add(two_2);
        menu.add(two);


        MenuItem three = new MenuItem().name("菜单三");
        MenuItem three_1 = new MenuItem().url("https://baidu.com").type(MenuType.view).name("百度");
        three.add(three_1);
        menu.add(three);

        WeChatUtils.createMenu(menu);
    }

}
