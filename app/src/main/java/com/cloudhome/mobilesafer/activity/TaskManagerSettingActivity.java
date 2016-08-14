package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.service.KillProcessService;
import com.cloudhome.mobilesafer.utils.ServiceStatusUtils;

/**
 * Created by xionghu on 2016/8/11.
 * Email：965705418@qq.com
 */
public class TaskManagerSettingActivity extends Activity {

    private CheckBox cb_show_system_process,cb_kill_process;
    private SharedPreferences sp;
    private Intent killProcessIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanagersetting);

        sp = getSharedPreferences("config",MODE_PRIVATE);
        cb_show_system_process = (CheckBox) findViewById(R.id.cb_show_system_process);
        cb_kill_process= (CheckBox) findViewById(R.id.cb_kill_process);




        boolean showsystem = sp.getBoolean("showsystem",true);

        if(showsystem)
        {
            cb_show_system_process.setText("当前状态：显示系统进程");
        }else{
            cb_show_system_process.setText("当前状态：隐藏系统进程");
        }
        cb_show_system_process.setChecked(showsystem);


        cb_show_system_process.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cb_show_system_process.setText("当前状态：显示系统进程");
                }else{
                    cb_show_system_process.setText("当前状态：隐藏系统进程");
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("showsystem",isChecked);
                editor.commit();
            }
        });


        killProcessIntent = new Intent(this,KillProcessService.class);
        boolean isRunningService = ServiceStatusUtils.isRunningService(this, "com.cloudhome.mobilesafer.service.KillProcessService");
        if(isRunningService){
            cb_kill_process.setText("当前状态：锁屏杀死后台进程");
        }else{
            cb_kill_process.setText("当前状态：锁屏不杀死后台进程");
        }
        cb_kill_process.setChecked(isRunningService);


        cb_kill_process.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    cb_show_system_process.setText("当前状态：锁屏杀死后台进程");
                    //启动服务

                    startService(killProcessIntent);
                }else{
                    cb_show_system_process.setText("当前状态：锁屏不杀死后台进程");
                    //关闭服务
                    stopService(killProcessIntent);
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("showsystem",isChecked);
                editor.commit();


            }
        });
    }
}
