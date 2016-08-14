package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.adapter.HomeAdapter;
import com.cloudhome.mobilesafer.utils.HttpMd5;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主页面
 * Created by xionghu on 2016/7/13.
 * Email：965705418@qq.com
 */
public class HomeActivity extends Activity {


    private static final String TAG = "HomeActivity";
    private GridView gv_home;
    private HomeAdapter madapter;

    private static final String names[] = {"手机防盗", "通讯卫士", "应用管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] mImgs = new int[]{R.mipmap.home_icon_guard, R.mipmap.home_icon_call, R.mipmap.home_icon_apps, R.mipmap.home_icon_progress,
            R.mipmap.home_icon_liuliang, R.mipmap.home_icon_virus,
            R.mipmap.home_icon_clean, R.mipmap.home_icon_tools, R.mipmap.home_icon_setting
    };

    private ArrayList<HashMap<String, Object>> home_list = new ArrayList<HashMap<String, Object>>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
        initData();
        initEvent();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        for (int i = 0; i < names.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("name", names[i]);
            map.put("img", mImgs[i]);

            home_list.add(map);
        }

    }


    private void initView() {

        gv_home = (GridView) findViewById(R.id.gv_home);
        madapter = new HomeAdapter(HomeActivity.this);


    }

    private void initEvent() {

        gv_home.setAdapter(madapter);
        madapter.setData(home_list);

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent;
                switch(position){


                    case 0://进入手机防盗

                        showLostFindDialog();

                        break;

                    case 1://通讯卫士

                        intent = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;
                    case 2://应用管理

                        intent = new Intent(HomeActivity.this,AppManagerActivity.class);
                        startActivity(intent);
                        break;

                    case 3://进程管理

                        intent = new Intent(HomeActivity.this,TaskManagerActivity.class);
                        startActivity(intent);
                        break;


                    case 7://进入高级工具
                         intent = new Intent(HomeActivity.this,AToolsActivity.class);


                        startActivity(intent);

                        break;

                    case 8://进入设置中心

                         intent = new Intent(HomeActivity.this,SettingActivity.class);

                                startActivity(intent);
                        break;

                    default:

                        break;
                }
            }
        });

    }

    /**
     * 根据当前情况，弹出不同的对话框
     */
    private void showLostFindDialog() {

        //判断是否设置了密码，如果没有设置就弹出设置对话框，否则就弹出输入对话框

        if(isSetupPwd())
        {
            //已经设置密码了

            showEnterDialog();
        }else{
            //没有设置密码了

            showSetupPwdDialog();

        }


    }

    private AlertDialog dialog;


    /**
     * 设置密码的对话框
     */
    private void showSetupPwdDialog() {

        AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);

        View view = View.inflate(HomeActivity.this,R.layout.dialog_setuppwd,null);
        final EditText et_password = (EditText) view.findViewById(R.id.et_password);
        final EditText et_password_confirm = (EditText) view.findViewById(R.id.et_password_confirm);

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {

                                   String password = et_password.getText().toString().trim();
                                      String password_confirm = et_password_confirm.getText().toString().trim();

                                      if(TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm))
                                      {
                                          Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();

                                          return;
                                      }


                                      if(password.equals(password_confirm))
                                      {
                                          SharedPreferences.Editor editor  =sp.edit();
                                          editor.putString("password", HttpMd5.getMD5(password));
                                          editor.commit();

                                          dialog.dismiss();


                                          Log.e(TAG,"保存密码，消掉对话框，进入手机防盗界面");
                                          Intent intent = new Intent(HomeActivity.this,LostFoundActivity.class);
                                          startActivity(intent);

                                      }else{

                                          Toast.makeText(HomeActivity.this,"输入的密码不一致",Toast.LENGTH_SHORT).show();
                                      }


                                  }
                              }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                dialog.dismiss();

            }
        });


//        builder.setView(view);
//        dialog =  builder.show();

        dialog = builder.create();
        dialog.setView(view,0,0,0,0);
        dialog.show();
    }


    /**
     * 输入密码的对话框
     */
    private void showEnterDialog() {



        AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);

        View view = View.inflate(HomeActivity.this,R.layout.dialog_enter_pwd,null);
        final EditText et_password = (EditText) view.findViewById(R.id.et_password);


        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {

                                      String password = et_password.getText().toString().trim();

                                      String passeord_save = sp.getString("password","");


                                      if(TextUtils.isEmpty(password))
                                      {
                                          Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();

                                          return;
                                      }


                                      if( HttpMd5.getMD5(password).equals(passeord_save))
                                      {

                                          dialog.dismiss();


                                        Intent intent = new Intent(HomeActivity.this,LostFoundActivity.class);
                                          startActivity(intent);
                                      }else{

                                          Toast.makeText(HomeActivity.this,"您输入的密码不正确",Toast.LENGTH_SHORT).show();
                                      }


                                  }
                              }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                dialog.dismiss();

            }
        });

// 1.第一种布局
//        builder.setView(view);
//        dialog =  builder.show();


        dialog = builder.create();
        dialog.setView(view,0,0,0,0);
        dialog.show();

    }

    private boolean isSetupPwd(){


        String password = sp.getString("password",null);



        return  !TextUtils.isEmpty(password);
    }

}
