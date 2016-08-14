package com.cloudhome.mobilesafer.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by xionghu on 2016/8/10.
 * Email：965705418@qq.com
 */
public class SystemInfoUtils  {

    /**
     *得到当前手机运行进程的总数
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);



        return am.getRunningAppProcesses().size();



    }

    /**
     * 得到可用内存
     * @param context
     * @return
     */
    public static long getAvailRam(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();

        am.getMemoryInfo(outInfo);

        return outInfo.availMem;
    }





    /**
     * 得到总的内存
     * @param context
     * @return
     */
    public static long getTotalRam(Context context)
    {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();

            am.getMemoryInfo(outInfo);


            return outInfo.totalMem;
        }else{
            try {
                File file = new File("/proc/meminfo");
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                //MemTotal:         516452 kB
                String result = reader.readLine();
                StringBuffer buffer = new StringBuffer();
                //字符串-字符数组-字符
                for(char c : result.toCharArray()){
                    if(c >= '0'&& c <= '9'){
                        buffer.append(c);
                    }
                }
                return Integer.valueOf(buffer.toString())*1024;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }
        }
    }



}
