package com.cloudhome.mobilesafer.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xionghu on 2016/8/11.
 * Email：965705418@qq.com
 */
public class KillProcessService extends Service {




    private Timer timer;//定时器
    private TimerTask task;
    private ScreenReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                System.out.println("定时器开始干活");

            }
        };
        /**
         *从两秒钟开始 4s 钟轮询一次
         */
        timer.schedule(task, 2000, 4000);
        //监听锁屏事件
        receiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        //设置监听锁屏
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter );
    }

    private class ScreenReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            //杀死后台进程
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            for(ActivityManager.RunningAppProcessInfo processInfo :am.getRunningAppProcesses())
            {
                am.killBackgroundProcesses(processInfo.processName);

            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消监听锁屏
        unregisterReceiver(receiver);
        receiver = null;
        timer.cancel();
        task.cancel();
        timer = null;
        task = null;


    }
}
