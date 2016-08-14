package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.service.AddressService;
import com.cloudhome.mobilesafer.service.CallSmsSafeService;
import com.cloudhome.mobilesafer.utils.ServiceStatusUtils;
import com.cloudhome.mobilesafer.view.SettingClickView;
import com.cloudhome.mobilesafer.view.SettingItemView;

/**
 * Created by xionghu on 2016/7/15.
 * Email：965705418@qq.com
 */
public class SettingActivity extends Activity {

    //设置自动更新
    private SettingItemView siv_update;
    private SharedPreferences sp;

    // 设置号码归属地显示
    private SettingItemView siv_changebg;

    private Intent addressIntent;

    //设置归属地显示框的背景
    private SettingItemView siv_showaddress;

    private SettingClickView scv_changebg;
    private SettingClickView scv_changeposition;

    //设置黑名单拦截
    private SettingItemView siv_blacknumber;
    private Intent blacknumberIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config",MODE_PRIVATE);

        initView();
       initEvent();




    }



    private void initView() {
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_showaddress = (SettingItemView) findViewById(R.id.siv_showaddress);
        siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);


        //设置归属地显示框的风格
        scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
        //设置归属地显示框的位置
        scv_changeposition= (SettingClickView) findViewById(R.id.scv_changeposition);

        addressIntent = new Intent(this, AddressService.class);
        boolean addressService = ServiceStatusUtils.isRunningService(this,"com.cloudhome.mobilesafer.service.AddressService");

        if(addressService){
            siv_showaddress.setChecked(true);
        }else{
            siv_showaddress.setChecked(false);
        }

        siv_showaddress.setChecked(addressService);


        boolean update = sp.getBoolean("update",false);

        if(update){

            siv_update.setDescription("当前状态自动升级已经开启");



        }else{


            siv_update.setDescription("当前状态自动升级已经关闭");


        }
        siv_update.setChecked(update);
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


        siv_showaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (siv_showaddress.isChecked()) {
                    siv_showaddress.setChecked(false);
                    //关闭服务
                    stopService(addressIntent);
                } else {

                    siv_showaddress.setChecked(true);
                    //开启服务
                    startService(addressIntent);
                }
            }
        });
        scv_changeposition.setTitle("归属地提示框位置");
        scv_changeposition.setDescription("设置归属地提示框显示位置");
        scv_changeposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转到拖动的Activity里面
                Intent intent =new Intent(SettingActivity.this,DragViewActivity.class);
                startActivity(intent);

            }
        });

        final  String items[] = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
         int which = sp.getInt("which",0);
        scv_changebg.setDescription(items[which]);
        scv_changebg.setTitle("归属地显示框风格");
        scv_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int tt = sp.getInt("which",0);

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("归属地显示框风格");
                builder.setSingleChoiceItems(items, tt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //1.保存起来
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which",which);
                        editor.commit();

                        //2.设置
                        scv_changebg.setDescription(items[which]);
                        dialog.dismiss();


                    }
                });
                builder.setNegativeButton("cancel",null);
                builder.show();
            }
        });


        blacknumberIntent = new Intent(this,CallSmsSafeService.class);
        boolean blacknumberService = ServiceStatusUtils.isRunningService(this, "com.cloudhome.mobilesafer.service.CallSmsSafeService");
        siv_blacknumber.setChecked(blacknumberService);
        siv_blacknumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(siv_blacknumber.isChecked()){
                    //变为非勾选
                    siv_blacknumber.setChecked(false);
                    //关闭服务
                    stopService(blacknumberIntent);
                }else{
                    //勾选
                    siv_blacknumber.setChecked(true);
                    //开启服务
                    startService(blacknumberIntent);
                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        addressIntent = new Intent(this, AddressService.class);
        boolean addressService = ServiceStatusUtils.isRunningService(this,"com.cloudhome.mobilesafer.service.AddressService");

        if(addressService){
            siv_showaddress.setChecked(true);
        }else{
            siv_showaddress.setChecked(false);
        }


    }
}
