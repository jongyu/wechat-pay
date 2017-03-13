package com.zhongyu.wechat.common.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongYu on 3/13/2017.
 */
public class Menu implements Serializable {

    @JsonProperty("button")
    private List<MenuItem> menus = new ArrayList<>();

    public List<MenuItem> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuItem> menus) {
        this.menus = menus;
    }

    public Menu add(MenuItem menuItem) {
        this.menus.add(menuItem);
        return this;
    }

}
