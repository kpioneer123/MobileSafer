package com.cloudhome.mobilesafer.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by xionghu on 2016/7/25.
 * Email：965705418@qq.com
 */
public class GPSService extends Service {


    private LocationManager lm;
    private SharedPreferences sp;
    private MyLocationListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("config", MODE_PRIVATE);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyLocationListener();
        //设置条件
        Criteria criteria= new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置精确度为最佳

//        criteria.setAltitudeRequired(false);//不要求海拔信息
//        criteria.setBearingRequired(false);//不要求方位信息
//        criteria.setCostAllowed(true);//是否允许收费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求
        String provider = lm.getBestProvider(criteria,true);

        lm.requestLocationUpdates("gps",0,0,listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        lm.removeUpdates(listener);
        listener =null;
    }

    private class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {


            String longitude  = "j:"+location.getLongitude()+"\n";
            String latitude   = "w:"+location.getLatitude()+"\n";
            String accuracy   = "a:"+location.getAccuracy()+"\n";//精确度
            //位置变化-发短信给安全号码
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastlocation", longitude+latitude+accuracy);
            editor.commit();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
