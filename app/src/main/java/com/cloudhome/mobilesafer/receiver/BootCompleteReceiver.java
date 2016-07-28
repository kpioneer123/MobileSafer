package com.cloudhome.mobilesafer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 监听开机广播
 * Created by xionghu on 2016/7/21.
 * Email：965705418@qq.com
 */
public class BootCompleteReceiver extends BroadcastReceiver {


    protected SharedPreferences sp;
    private TelephonyManager tm ;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 1.得到之前的sim卡信息
        sp =context.getSharedPreferences("config",context.MODE_PRIVATE);

        if (sp.getBoolean("protectting",false)) {
            // 2.得到当前手机的sim卡信息
            String save_sim = sp.getString("sim", "");

            tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

            String currentSim = tm.getSimSerialNumber();


            // 3.比较sim卡信息是否一致
            if (save_sim.equals(currentSim)) {

            } else {

                Toast.makeText(context, "sim卡变更了", Toast.LENGTH_LONG).show();

                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim changge from sim:"+currentSim, null,
                null);
            }

            // 4.如果不一致就发短信给安全号码

        }

    }
}
