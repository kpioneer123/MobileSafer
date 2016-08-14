package com.cloudhome.mobilesafer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.adapter.AppManageAdapter;
import com.cloudhome.mobilesafer.domain.AppInfo;
import com.cloudhome.mobilesafer.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class AppManagerActivity extends Activity implements View.OnClickListener {

    private TextView tv_avail_rom;
    private TextView tv_status;
    private TextView tv_avail_sdcard;
    private ListView lv_appmanger;
    private LinearLayout ll_loading;

    private LinearLayout ll_uninstall;
    private LinearLayout ll_start;
    private LinearLayout ll_share;

    private List<AppInfo> appInfos;//所有应用程序的信息
    private List<AppInfo> systemappInfos;//系统应用程序的信息
    private List<AppInfo> userappInfos;// 用户程序的信息
    private AppManageAdapter madapter;

    private PopupWindow window;
    private  AppInfo appInfo;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            madapter.setData(userappInfos,systemappInfos);
            lv_appmanger.setAdapter(madapter);
            ll_loading.setVisibility(View.GONE);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initView();


        initEvent();

        fillData();

    }


    private void initView() {
        tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
        tv_avail_sdcard = (TextView) findViewById(R.id.tv_avail_sdcard);
        tv_status = (TextView) findViewById(R.id.tv_status);

        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        lv_appmanger = (ListView) findViewById(R.id.lv_appmanger);

        madapter =new AppManageAdapter(AppManagerActivity.this);
    }


    private void initEvent() {
        tv_avail_rom.setText("内存可用: " + getAvailSpace(Environment.getDataDirectory().getAbsolutePath()));
        tv_avail_sdcard.setText("sd卡可用: " + getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath()));
        lv_appmanger.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if(systemappInfos ==null||userappInfos==null)
                {
                    return;
                }
                if(firstVisibleItem > userappInfos.size())
                {
                    //显示系统程序
                    tv_status.setText("系统程序("+systemappInfos.size()+")");

                }else{
                    //用户程序
                    tv_status.setText("用户程序("+userappInfos.size()+")");
                }
            }
        });


        lv_appmanger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = lv_appmanger.getItemAtPosition(position);
                if(obj != null)
                {
                    dismissPopupWindow();//
                    appInfo = (AppInfo) obj;
                    System.out.println("appInfo==" + appInfo.getPackName());
					/*
					 * TextView contentView = new
					 * TextView(AppManagerActivity.this);
					 * contentView.setTextColor(Color.RED);
					 * contentView.setText(appInfo.getPackName());
					 */
                    View contentView = View.inflate(AppManagerActivity.this,
                            R.layout.popupwindow_item, null);

                    ll_uninstall = (LinearLayout) contentView
                            .findViewById(R.id.ll_uninstall);
                    ll_start = (LinearLayout) contentView
                            .findViewById(R.id.ll_start);
                    ll_share = (LinearLayout) contentView
                            .findViewById(R.id.ll_share);
                    ll_uninstall.setOnClickListener(AppManagerActivity.this);
                    ll_start.setOnClickListener(AppManagerActivity.this);
                    ll_share.setOnClickListener(AppManagerActivity.this);
                    window = new PopupWindow(contentView,  ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    // **要想PopupWindow播放动画，需要加背景
                    window.setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT));
                    int[] location = new int[2];
                    view.getLocationInWindow(location);// 得到ListView中某条的坐标
                    // 在代码里面写的长度单位都是像素
                    window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
                            60, location[1]);
                    // 渐变动画
                    AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
                    aa.setDuration(500);
                    // 缩放动画
                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
                            1.0f, Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    sa.setDuration(500);

                    AnimationSet set = new AnimationSet(false);
                    set.addAnimation(aa);
                    set.addAnimation(sa);
                    contentView.startAnimation(set);

                }


            }
        });
    }

    /**
     * 消掉popupWindow
     */
    private void dismissPopupWindow() {
        if (window != null && window.isShowing()) {
            window.dismiss();
            window = null;
        }
    }


    /**
     * 加载数据
     */
    private void fillData() {

        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){

            public void run()
            {
               appInfos =  AppInfoProvider.getAllAppInfos(AppManagerActivity.this);
               systemappInfos = new ArrayList<AppInfo>();
                userappInfos  = new ArrayList<AppInfo>();

                //划分数据
                for (AppInfo appInfo: appInfos ){
                    if(appInfo.isUser())
                    {
                        userappInfos.add(appInfo);
                    }else{
                        systemappInfos.add(appInfo);
                    }


                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 得到某个目录的可用空间
     *
     * @param path
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getAvailSpace(String path) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        long blocks;
        long size;
        if (currentapiVersion < 18) {
            StatFs fs = new StatFs(path);
            //得到多少块可用空间
            blocks = fs.getAvailableBlocks();
            //得到每块的可用大小
            size = fs.getBlockSize();
        } else {
            StatFs fs = new StatFs(path);
            blocks = fs.getAvailableBlocksLong();
            size = fs.getBlockSizeLong();
        }

        return Formatter.formatFileSize(this, blocks * size);
    }

    @Override
    public void onClick(View v) {

        dismissPopupWindow();
        switch (v.getId()) {
            case R.id.ll_uninstall:// 卸载
                System.out.println("卸载"+appInfo.getPackName());
                uninstallApp();
                break;

            case R.id.ll_start:// 启动
                System.out.println("启动"+appInfo.getPackName());
//			startApp();
                startApp2();

                break;
            case R.id.ll_share:// 分享
                System.out.println("分享"+appInfo.getPackName());
                shareApp();

                break;
        }

    }

    /**
     * 启动应用
     */
    private void startApp2() {
        //得到包名，用包名启动
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackName());
        startActivity(intent);



    }

    /**
     * 启动应用
     */
    private void startApp() {
        Intent intent = new Intent();
        PackageManager pm = getPackageManager();
        String packName = appInfo.getPackName();
        try {
            //得到所有的Activity
            PackageInfo packInfo = pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] infos =  packInfo.activities;
            if(infos != null&&infos.length>0){
                //得到第一个Activity
                ActivityInfo actiivtyInfo = infos[0];
                String name = actiivtyInfo.name;//全类名
                intent.setClassName(packName, name);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "亲，这个程序没有页面", Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 分享软件-分享到微博，微信，陌陌，QQ空间
     */
    private void shareApp() {
        Intent intent = new Intent();
        // <action android:name="android.intent.action.SEND" />
        //<category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");//文本类型 还有视频类型 图片类型
        intent.putExtra(Intent.EXTRA_TEXT, "最近使用："+appInfo.getName()+",下载地址：http://www.appchina.com/app/"+appInfo.getPackName());
        startActivity(intent);

    }

    /**
     * 卸载软件
     */
    private void uninstallApp() {
        if(appInfo.isUser()){

//			 <action android:name="android.intent.action.VIEW" />
//	         <action android:name="android.intent.action.DELETE" />
//	         <category android:name="android.intent.category.DEFAULT" />
//	         <data android:scheme="package" />


            Intent intent = new Intent();
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:"+appInfo.getPackName()));
            startActivityForResult(intent, 0);

        }else{
            Toast.makeText(this, "需要root权限才能卸载", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

}
