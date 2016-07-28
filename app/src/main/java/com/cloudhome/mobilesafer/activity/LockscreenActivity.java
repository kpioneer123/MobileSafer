package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloudhome.mobilesafer.receiver.MyAdmin;

/**
 * Created by xionghu on 2016/7/26.
 * Email：965705418@qq.com
 */
public class LockscreenActivity extends Activity {


    /**
     * 设备策略管理员
     */
    private DevicePolicyManager dpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dpm = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

        finish();

    }


    public void lockscreen(View view)
    {
        ComponentName who  = new ComponentName(this, MyAdmin.class);
        if(dpm.isAdminActive(who))
        {
            dpm.lockNow();
            dpm.resetPassword("", 0);//设置密码

            //dpm.wipeData(0);//让手机恢复成出厂设置-远程删除数据
            //dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除手机sdcard的数据-
        }else{
            openAdmin(null);
        }
    }

    private void openAdmin(View view) {


        //定义意图，动作：添加设备管理员
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //激活的组件
        ComponentName  mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        //激活的说明
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "你激活设备管理员权限，可以一键锁屏，更加安全...");
        startActivity(intent);


    }



    //卸载软件
    public void uninstall(View view){
        //1.把权限干掉
        ComponentName  who = new ComponentName(this, MyAdmin.class);
        dpm.removeActiveAdmin(who);

        //2.当成普通应用卸载
//		  <action android:name="android.intent.action.VIEW" />
//          <action android:name="android.intent.action.DELETE" />
//          <category android:name="android.intent.category.DEFAULT" />
//          <data android:scheme="package" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
      intent.setData(Uri.parse("package:" +getPackageName()));
        startActivity(intent);
    }

}
