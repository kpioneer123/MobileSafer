package com.cloudhome.mobilesafer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cloudhome.mobilesafer.db.NumberAddressQueryDao;

/**
 * Created by xionghu on 2016/7/28.
 * Email：965705418@qq.com
 */
public class OutCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number  = getResultData();//去电号码
        String address = NumberAddressQueryDao.getAddress(number);
        Toast.makeText(context,address,Toast.LENGTH_SHORT);
    }
}
