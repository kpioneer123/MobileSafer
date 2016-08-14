package com.cloudhome.mobilesafer.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class AppInfo {

    private Drawable icon;
    private String   name;
    private String   packName;

    /**
     * true安装在内部
     * false安装在sdcard
     */
    private  boolean isRom;

    /**
     * true 用户程序
     * false 系统程序
     */
    private boolean isUser;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "isUser=" + isUser +
                ", isRom=" + isRom +
                ", name='" + name + '\'' +
                ", packName='" + packName + '\'' +
                '}';
    }
}
