package com.cloudhome.mobilesafer;

import android.test.AndroidTestCase;

import com.cloudhome.mobilesafer.domain.AppInfo;
import com.cloudhome.mobilesafer.engine.AppInfoProvider;

import java.util.List;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class TestGetApp extends AndroidTestCase {

    public void testgetApp(){
        List<AppInfo> appInfos = AppInfoProvider.getAllAppInfos(getContext());

        for(AppInfo appinfo :appInfos)
        {
            System.out.println("appInfo == " +appinfo.toString());
        }
    }
}
