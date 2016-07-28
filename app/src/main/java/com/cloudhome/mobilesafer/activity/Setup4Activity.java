package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class Setup4Activity extends BaseSetupActivity {


    private CheckBox cb_protectting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否做过设置向导，如果没有做过就跳转到设置向导页面的第一个页面，否则就加载手机防盗页面
        setContentView(R.layout.activity_setup4);



        cb_protectting = (CheckBox) findViewById(R.id.cb_protectting);
        boolean protectting =sp.getBoolean("protectting",false);
        if(protectting)
        {
            //手机防盗已经开启
            cb_protectting.setText("当前状态:手机防盗已经开启");

        }else{

            //手机防盗已经关闭
            cb_protectting.setText("当前状态:手机防盗已经关闭");
        }
        cb_protectting.setChecked(protectting);
        cb_protectting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protectting",isChecked);
                editor.commit();
                if(isChecked)
                {

                    cb_protectting.setText("当前的状态：手机防盗已经开启");

                }else{
                    cb_protectting.setText("当前的状态：手机防盗已经关闭");
                }

            }
        });
    }

    @Override
    public void showPre() {

        //当前页面关闭
        finish();
        overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
    }

    @Override
    public void showNext() {

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed",true);
        editor.commit();

        if(Setup1Activity.Setup1instance!=null)
        {
            Setup1Activity.Setup1instance.finish();
        }

        if(Setup2Activity.Setup2instance!=null)
        {
            Setup2Activity.Setup2instance.finish();
        }
        if(Setup3Activity.Setup3instance!=null)
        {
            Setup3Activity.Setup3instance.finish();
        }


        //当前页面关闭
        finish();
        overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);
    }




}