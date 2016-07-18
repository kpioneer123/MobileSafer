package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.view.SettingItemView;

/**
 * Created by xionghu on 2016/7/15.
 * Email：965705418@qq.com
 */
public class SettingActivity extends Activity {


    private SettingItemView siv_update;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config",MODE_PRIVATE);

        initView();
       initEvent();




    }

    private void initEvent() {

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sp.edit();

                //得到它是否被勾选

                if(siv_update.isChecked())
                {
                    siv_update.setChecked(false);
                 //   siv_update.setDescription("当前状态自动升级已经关闭");
                    editor.putBoolean("update",false);
                }else{


                    siv_update.setChecked(true);
                //    siv_update.setDescription("当前状态自动升级已经开启");
                    editor.putBoolean("update",true);
                }
                editor.commit();
            }
        });
    }

    private void initView() {
        siv_update = (SettingItemView) findViewById(R.id.siv_update);

        boolean update = sp.getBoolean("update",false);

        if(update){

            siv_update.setDescription("当前状态自动升级已经开启");

        }else{


            siv_update.setDescription("当前状态自动升级已经关闭");
        }
        siv_update.setChecked(update);
    }
}
