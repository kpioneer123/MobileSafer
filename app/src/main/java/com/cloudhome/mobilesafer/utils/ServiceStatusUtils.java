package com.cloudhome.mobilesafer.utils;


import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by xionghu on 2016/7/28.
 * Email：965705418@qq.com
 */
public class ServiceStatusUtils {

    /**
     * 检验某个服务是否运行中
     * @param context
     * @param serviceName 要校验服务的全类名
     * @return 如果运行着就返回true，否则就返回false
     */


    public static  boolean isRunningService(Context context ,String serviceName ){

        //ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo>  serviceInfos = am.getRunningServices(100);

for(ActivityManager.RunningServiceInfo service : serviceInfos)
{
    String name = service.service.getClassName();

    if(serviceName.equals(name)){
       return true;
    }

}

     return false;
    }
}
