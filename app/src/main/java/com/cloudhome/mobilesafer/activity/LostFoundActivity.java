package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class LostFoundActivity extends BaseSetupActivity{

    private TextView  tv_safenumber;
    private ImageView iv_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp =getSharedPreferences("config",MODE_PRIVATE);
        boolean configed =sp.getBoolean("configed",false);

        if(configed)
        {
            //手机防盗页面
            setContentView(R.layout.activity_lost_find);

            tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
            iv_status = (ImageView) findViewById(R.id.iv_status);

            tv_safenumber.setText(sp.getString("safenumber",""));

            sp.getBoolean("protectting",false);
            boolean protectting = sp.getBoolean("protectting",false);

            if(protectting)
            {
                //防盗保护已经开启

                iv_status.setImageResource(R.mipmap.lock);
            }else{
                //防盗保护没有开启

                iv_status.setImageResource(R.mipmap.lock);
            }

        }else{


            enterSetting();
        }
    }

    @Override
    public void showPre() {

    }

    @Override
    public void showNext() {

    }

    /**
     * 进入设置向导页面
     */
    private void enterSetting() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);
    }

    public void reEnterSetting(View view)
    {
        enterSetting();
    }
}
