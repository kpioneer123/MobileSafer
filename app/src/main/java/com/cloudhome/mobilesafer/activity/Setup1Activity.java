package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class Setup1Activity extends BaseSetupActivity {

    private SharedPreferences sp;
    public static Setup1Activity Setup1instance=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否做过设置向导，如果没有做过就跳转到设置向导页面的第一个页面，否则就加载手机防盗页面
        setContentView(R.layout.activity_setup1);

        Setup1instance =this;


    }

    @Override
    public void showPre() {

    }

    @Override
    public void showNext() {



            Intent intent = new Intent(this,Setup2Activity.class);

            startActivity(intent);


            overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);

    }


    //3.食用手势识别器


}