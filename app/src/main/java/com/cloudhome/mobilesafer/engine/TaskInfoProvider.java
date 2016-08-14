package com.cloudhome.mobilesafer.engine;

import android.app.ActivityManager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionghu on 2016/8/11.
 * Email：965705418@qq.com
 */
public class TaskInfoProvider   {

    /**
     * 得到手机所有的运行的进程信息
     * @param context
     * @return
     */
    public static List<TaskInfo>  getAllTaskInfos(Context context){

        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();


        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos =am.getRunningAppProcesses();
        PackageManager pm  = context.getPackageManager();
        for(ActivityManager.RunningAppProcessInfo processInfo : processInfos)
        {

            TaskInfo taskInfo = new TaskInfo();
            //包名

            String packName = processInfo.processName;

            taskInfo.setPackName(packName);

            Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[] {processInfo.pid})[0];

            long meminfosize = memoryInfo.getTotalPrivateDirty()*1024;

            taskInfo.setMeminfosize(meminfosize);

            try {
                //图标
                Drawable icon = pm.getPackageInfo(packName,0).applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);

                //软件名称
                String   name = pm.getPackageInfo(packName,0).applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);


                int flag = pm.getPackageInfo(packName,0).applicationInfo.flags;
                if((flag& ApplicationInfo.FLAG_SYSTEM)==0)
                {
                    //用户进程
                    taskInfo.setUser(true);

                }else{

                    //系统进程
                    taskInfo.setUser(false);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.setName(packName);
                taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));

            }


            taskInfos.add(taskInfo);
        }

        return taskInfos;
    }

}
