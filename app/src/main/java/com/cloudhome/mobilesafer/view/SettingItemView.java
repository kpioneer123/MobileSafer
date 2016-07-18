package com.cloudhome.mobilesafer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudhome.mobilesafer.R;


/**
 * Created by xionghu on 2016/7/15.
 * Email：965705418@qq.com
 */
public class SettingItemView extends RelativeLayout {




    private CheckBox cb_status;
    private TextView tv_desc;
    private  TextView tv_title;
    private String update_off;
    private String update_on;

    //在代码中实例化的时候使用
    public SettingItemView(Context context) {
        super(context);

        initView( context);
    }

    /** 在布局文件实例化的时候使用
     * @param context
     * @param attrs 用来自定义控件属性 xml定义n： wrap_content attrs.getAttributeValue(n) 可能为-2
     *              id  attrs.getAttributeValue(0) 对应R.java
     */

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);


        initView(context);
        String  title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","title");
         update_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","update_off");
         update_on  = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","update_on");

        tv_title.setText(title);
    }

    //要设置样式的时候使用
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    /**
     * 初始化布局文件
     */
    private void initView(Context context) {

        //inflate 方法: 把布局文件--> View
        //最后一个参数: 添加谁进来，就是R.layout.setting_item_view 的父亲 ，也就是说，把布局文件挂载在传进来的这个控件上

        View.inflate(context, R.layout.setting_item_view,this);

        cb_status = (CheckBox) findViewById(R.id.cb_status);
        tv_desc  = (TextView) findViewById(R.id.tv_desc);
        tv_title = (TextView) findViewById(R.id.tv_title);

        //设置描述信息
        setDescription(update_on);
    }


    /**
     * 得到组合控件是否勾选
     * @return
     */
    public boolean isChecked()
    {

        return cb_status.isChecked();
    }

    /**
     * 设置组合控件勾选状态
     * @return
     */
    public void setChecked(boolean isChecked)
    {
        cb_status.setChecked(isChecked);

        if(isChecked){

            //开启
            setDescription(update_on);
        }else{
            //关闭
            setDescription(update_off);
        }
    }

    public void setDescription(String description)
    {
        tv_desc.setText(description);
    }

}
