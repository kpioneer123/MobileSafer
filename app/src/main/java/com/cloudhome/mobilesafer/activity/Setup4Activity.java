package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否做过设置向导，如果没有做过就跳转到设置向导页面的第一个页面，否则就加载手机防盗页面
        setContentView(R.layout.activity_setup4);

        sp =getSharedPreferences("config",MODE_PRIVATE);

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