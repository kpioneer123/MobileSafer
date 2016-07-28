package com.cloudhome.mobilesafer.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.activity.LockscreenActivity;
import com.cloudhome.mobilesafer.service.GPSService;

/**
 * Created by xionghu on 2016/7/25.
 * Email：965705418@qq.com
 */
public class SMSReceiver extends BroadcastReceiver{

    private SharedPreferences sp;
    private DevicePolicyManager dpm;
    @Override
    public void onReceive(Context context, Intent intent) {

        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
     Object[] pdus = (Object[]) intent.getExtras().get("puds");
        for(Object pdu : pdus){
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);

            //得到发送者
            String sender = sms.getOriginatingAddress();//15555215556

            String safenumber = sp.getString("safenumber","");//
            //得到内容
            String body = sms.getMessageBody();

            if(sender.contains(safenumber))
            {
                if("#location#".equals(body)) {
                    //得到手机的GPS位置
                    System.out.println("得到手机的GPS位置");

                    Intent gpsServiceIntent = new Intent(context, GPSService.class);

                    context.startService(gpsServiceIntent);

                    String lastlocation =sp.getString("lastlocation","");


                    if(TextUtils.isEmpty(lastlocation))
                    {
                        SmsManager.getDefault().sendTextMessage(sender,null,"getting location... for "+body,null,null);
                    }else{
                        SmsManager.getDefault().sendTextMessage(sender,null,lastlocation,null,null);
                    }


                    //终止广播
                    abortBroadcast();
                }else if("#alarm#".equals(body)){

                    MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
                    player.setVolume(1.0f,1.0f);
                    player.start();
                    //得到手机的GPS位置
                    System.out.println("播放报警音乐");

                    //终止广播
                    abortBroadcast();

                }
                else if("#wipedated#".equals(body)){
                    //得到手机的GPS位置
                    System.out.println("远程删除数据");



                    ComponentName who = new ComponentName(context,MyAdmin.class);
                    if (dpm.isAdminActive(who))
                    {
                        dpm.wipeData(0);//手机恢复出厂设置
                    }else{
                        openAdmin(context);
                    }




                    //终止广播
                    abortBroadcast();
                }
                else if("#lockscreen#".equals(body)){
                    //得到手机的GPS位置
                    System.out.println("远程锁屏");

                    ComponentName who = new ComponentName(context,MyAdmin.class);
                    if (dpm.isAdminActive(who))
                    {
                        dpm.lockNow();
                        dpm.resetPassword("123",0);
                    }else{
                        openAdmin(context);
                    }



                    //终止广播
                    abortBroadcast();
                }
            }

        }

    }

    private void openAdmin(Context context) {
        Intent openAdmin = new Intent(context, LockscreenActivity.class);
        openAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openAdmin);
    }
}
