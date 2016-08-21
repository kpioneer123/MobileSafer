package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.utils.GetVersion;
import com.cloudhome.mobilesafer.utils.IpConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;


/**
 * 启动页面
 * 1.显示logo
 * 2.判断是否有网络
 * 3.是否升级
 * 4.判断合法性
 * 5.检验是否有sdcard
 */
public class SplashActivity extends Activity {

    private static final int ENTER_HOME =1 ;
    private static final int SHOW_UPDATE_DIALOG = 2;

    private static final int URL_ERROR =3 ;

    private static final int NETWORK_ERROR =4 ;

    private static final int JSON_ERROR = 5;
    private TextView tv_splash_version;
    private static final String TAG="SplashActivity";

    private String versionName;
    private String versionCode;
    private String description;
    private String apkurl;

    private SharedPreferences sp;
    private ProgressDialog pBar;

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);

           switch (msg.what){
               case ENTER_HOME:  //进入主页面


                   enterHome();
                   break;


               case SHOW_UPDATE_DIALOG: //弹出对话框升级

                 //  enterHome();
                   Log.e(TAG,"弹出对话框");

                   showUpdateDialog();
                   break;


               case URL_ERROR:        //url异常



                   enterHome();
                   Toast.makeText(SplashActivity.this,"URL异常",Toast.LENGTH_SHORT).show();
                   break;


               case NETWORK_ERROR:    //网络异常


                   enterHome();
                   Toast.makeText(SplashActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                   break;


               case JSON_ERROR:       //json解析异常


                   enterHome();
                   Toast.makeText(getApplicationContext(),"JSON解析异常",Toast.LENGTH_SHORT).show();
                   break;
           }
       }
   };

    //创建快捷键
    private void createShortcut(){
        if(sp.getBoolean("installshortcut",false))
        {
         return;
        }

        Intent intent = new Intent();
        //动作
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //名称
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"安全卫士");
        //图片
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(),R.mipmap.logo));


        //点击我的时候要我干什么-一键报警
        Intent callIntent =new Intent();
        //直接进入主页面
        callIntent.setAction("com.cloudhome.mobilesafer.activity.HomeActivity");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,callIntent);

        sendBroadcast(intent);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("installshortcut",true);
        editor.commit();

        Log.d("5555","66666");

    }

    /**
     * 把assets目录下的address.db拷贝到/data/data/com.cloudhome.mobilesafer/files//address.db"
     */
    private void copyDB(String dbName){

        File file = new File(getFilesDir(),dbName);

        if (file.exists()&&file.length()>0){
            System.out.println("数据库已经存在，不要拷贝了...");
        }else {
            try {
                InputStream is = getAssets().open(dbName);

                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(description);
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //1.下载apk-Final
                //2.替换安装
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {



                    pBar = new ProgressDialog(SplashActivity.this);
                    pBar.setTitle("正在下载");
                    pBar.setMessage("请稍后...");
                    pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pBar.setCanceledOnTouchOutside(false);
                    pBar.setCancelable(false);
                    pBar.show();
                    pBar.setMax(100);
                    downAppFile(apkurl);


                }else{


                    Toast.makeText(getApplicationContext(),"sdcard不可用",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void downAppFile(final String url) {

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"mobilesafe2.0.apk") {
                    @Override
                    public void inProgress(float progress) {

                        pBar.setProgress((int) (100 * progress));
                     //   pBar.setProgressNumberFormat(String.format("%.2fM/%.2fM", percent, all));
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "onError :" + e.getMessage());

                        Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.e(TAG, "onResponse :" + response.getAbsolutePath());

                        haveDownLoad();
                    }
                });

    }


    protected void haveDownLoad() {

        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                // 弹出警告框 提示是否安装新的版本
                Dialog installDialog = new AlertDialog.Builder(
                        SplashActivity.this)
                        .setTitle("下载完成")
                        .setMessage("是否安装新的应用")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        installNewApk();
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                }).create();
                installDialog.setCancelable(false);
                installDialog.setCanceledOnTouchOutside(false);
                installDialog.show();
            }
        });
    }

    // 安装新的应用
    protected void installNewApk() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "mobilesafe2.0.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
    private void enterHome() {

        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("config",MODE_PRIVATE);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本名：" +getVersionName());

        if(sp.getBoolean("update",true))
        {
            //软件的升级
            checkVersion();
        }else{

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            }, 2000);
        }

        createShortcut();
        copyDB("address.db");
        copyDB("commonnum.db");


        AlphaAnimation aa= new AlphaAnimation(0.1f,1.0f);
        aa.setDuration(1000);
        findViewById(R.id.rl_splash_root).startAnimation(aa);
    }


    /**
     * 校验是否有新版本，如果有就升级
     */
    private void checkVersion() {


        OkHttpUtils.post()
                .url(IpConfig.getIp())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Message msg = new Message();
                        msg.what=NETWORK_ERROR;
                        handler.sendMessage(msg);


                    }

                    @Override
                    public void onResponse(String response) {
                        Message msg = Message.obtain();
                        //请求网络，得到最新的版本信息
                        try {


                            Log.d("55555",response);

                                JSONObject obj = new JSONObject(response);

                                //服务器最新的信息
                                String versionName =  obj.getString("versionName");
                                String versionCode =  obj.getString("versionCode");

                                if(GetVersion.getVerCode(SplashActivity.this)>=Integer.valueOf(versionCode))
                                {

                                    msg.what=ENTER_HOME;
                                }else{
                                    msg.what=SHOW_UPDATE_DIALOG;
                                }
                                description = obj.getString("description");
                                apkurl =  obj.getString("apkurl");


                        }catch (JSONException e) {
                            e.printStackTrace();
                            msg.what = JSON_ERROR;
                        }finally {
                            handler.sendMessage(msg);
                        }


                    }
                });



    }



//    private void checkVersion() {
//
//        String url ="http://10.10.10.45:8080/index.html?";
//                OkHttpUtils.post()
//                .url(url)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Message msg = new Message();
//                        msg.what=NETWORK_ERROR;
//                        handler.sendMessage(msg);
//
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Message msg = Message.obtain();
//                        //请求网络，得到最新的版本信息
//                        try {
//
//
//                            Log.d("55555",response);
//
//                                JSONObject obj = new JSONObject(response);
//
//
//                            JSONArray datalistArray = obj.getJSONObject("data").getJSONArray("datalist").getJSONObject(0).getJSONArray("list");
//
//
//
//                            for (int i = 0; i < datalistArray.length(); i++) {
//                                String jsonObject2 = datalistArray.get(i).toString();
//
//
//
//                                Object json = new JSONTokener(jsonObject2).nextValue();
//                                if(json instanceof JSONObject){
//                                    JSONObject jsonObject = (JSONObject)json;
//
//                                    Log.d("object","jsonObject:"+jsonObject.toString());
//                                }else if (json instanceof JSONArray){
//                                    JSONArray jsonArray = (JSONArray)json;
//                                    Log.d("object","jjsonArray:"+jsonArray.toString());
//                                }
//
//
//                            }
//
//
//
//                        }catch (JSONException e) {
//                            e.printStackTrace();
//                            msg.what = JSON_ERROR;
//                        }finally {
//                            handler.sendMessage(msg);
//                        }
//
//
//                    }
//                });
//
//
//    }
    /**
     * 得到版本名称
     *
     * @return
     */
    private String getVersionName() {

       //包管理器
        PackageManager pm = getPackageManager();
      //功能清单文件信息
        try {
          PackageInfo packInfo =  pm.getPackageInfo(getPackageName(),0);
          return   packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return "";
        }


    }
}
