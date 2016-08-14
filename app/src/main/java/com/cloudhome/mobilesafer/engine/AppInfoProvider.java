package com.cloudhome.mobilesafer.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.cloudhome.mobilesafer.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class AppInfoProvider {

    public  static List<AppInfo> getAllAppInfos(Context context){

        List<AppInfo> AppInfos = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();
       List<PackageInfo> packInfos= pm.getInstalledPackages(0);

        for(PackageInfo info : packInfos)
        {
            AppInfo appInfo = new AppInfo();
         String packName = info.packageName;
            appInfo.setPackName(packName);
           Drawable icon =  info.applicationInfo.loadIcon(pm);
            appInfo.setIcon(icon);
           String name= info.applicationInfo.loadLabel(pm).toString();
            appInfo.setName(name);

            //
            int flage =info.applicationInfo.flags;
              if((flage& ApplicationInfo.FLAG_SYSTEM)==0)
              {
                  //用户程序
                  appInfo.setUser(true);
              }else{
                  //系统程序
                  appInfo.setUser(false);
              }

            if((flage& ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0)
            {
                //内部存储
                appInfo.setRom(true);
            }else{
                //外部存储
                appInfo.setRom(false);
            }


            AppInfos.add(appInfo);
        }

        return AppInfos;
    }
}
