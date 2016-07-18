package com.cloudhome.mobilesafer.utils;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class GetVersion {

	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
        int verCode = -1;
        try {
        	//注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    "com.cloudhome.mobilesafer", 0).versionCode;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verCode;
    }
	
   /**
    * 获取版本名称
    * @param context
    * @return
    */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.cloudhome.mobilesafer", 0).versionName;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verName;   
}	
    

}