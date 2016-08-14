package com.cloudhome.mobilesafer.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息
 * Created by xionghu on 2016/8/11.
 * Email：965705418@qq.com
 */
public class TaskInfo {

    /**
     * true被选中
     * false没有被选中
     */
    private boolean isChecked;
    private Drawable icon ;
    private String   name ;
    private String   packName;



    /**
     * 单位byte
     */
    private long     meminfosize;
    /**
     * true用户进程
     * false系统进程
     */
    private boolean isUser;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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

    public long getMeminfosize() {
        return meminfosize;
    }

    public void setMeminfosize(long meminfosize) {
        this.meminfosize = meminfosize;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "isChecked=" + isChecked +
                ", name='" + name + '\'' +
                ", packName='" + packName + '\'' +
                ", meminfosize=" + meminfosize +
                ", isUser=" + isUser +
                '}';
    }

}
