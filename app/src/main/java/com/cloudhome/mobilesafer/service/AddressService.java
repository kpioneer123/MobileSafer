package com.cloudhome.mobilesafer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.db.NumberAddressQueryDao;

/**
 * Created by xionghu on 2016/7/27.
 * Email：965705418@qq.com
 */
public class AddressService extends Service {

    /**
     * 电话服务
     */
    private TelephonyManager tm;

    private MyPhoneStateListener listener;

    private OutCallReceiver receiver;

    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private View view;
    private SharedPreferences sp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("config",MODE_PRIVATE);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);





        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        receiver = new OutCallReceiver();
        //注册监听去电-广播接收者的代码注册
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");//监听去电的功能
        registerReceiver(receiver,filter);
    }


//服务里面
    private class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();//去电号码
            String address = NumberAddressQueryDao.getAddress(number);
            myToast( address);


        }
    }

    private void myToast(String address) {
//         view =new TextView(this);
//        view.setTextSize(18);
//        view.setTextColor(Color.RED);
//        view.setText(address);

          view  = View.inflate(this, R.layout.show_address,null);
        int which = sp.getInt("which",0);
        int ids[] = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};

        int LastX= sp.getInt("lastX",0);
        int LastY= sp.getInt("lastY",0);

        view.setBackgroundResource(ids[which]);//根据设置中心的设置值，动态设置背景

        view.setOnTouchListener(new View.OnTouchListener() {

            int startX = 0;
            int startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {


switch (event.getAction()){

    case MotionEvent.ACTION_DOWN:
        //1.记录第一次按下
        startX = (int) event.getRawX();
        startY = (int) event.getRawY();

        break;
    case MotionEvent.ACTION_MOVE:

        //2.来电新的坐标

        int newX = (int) event.getRawX();
        int newY = (int) event.getRawY();

        //3.计算偏移量
        int dX = newX - startX;
        int dY = newY - startY;

        //4.根据偏移量跟新控件的位置
        params.x+=dX;
        params.y+=dY;


        //屏蔽非法拖动
        if(params.x < 0 )
        {
            params.x = 0;
        }

        if(params.y < 0)
        {
            params.y = 0;
        }

        if(params.x   > wm.getDefaultDisplay().getWidth() - view.getWidth())
        {
            params.x  = wm.getDefaultDisplay().getWidth() - view.getWidth();
        }

        if(params.y   > wm.getDefaultDisplay().getHeight() - view.getHeight())
        {
            params.y  = wm.getDefaultDisplay().getHeight() - view.getHeight();
        }


        wm.updateViewLayout(view,params);
        //5.重新记录我们的坐标

        startX = (int) event.getRawX();
        startY = (int) event.getRawY();

        break;
    case MotionEvent.ACTION_UP:
        //保存坐标
        SharedPreferences.Editor editor =sp.edit();
        editor.putInt("lastX",params.x);
        editor.putInt("lastY",params.y);
        editor.commit();
        break;

}

                return true;
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.tv_address);
        tv.setText(address);
         params = new WindowManager.LayoutParams();
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x=LastX;
        params.y=LastY;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

        wm.addView(view,params);
    }

    /**
     * 呼叫状态
     * 来电号码
     */
    private  class MyPhoneStateListener extends PhoneStateListener{


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state)
            {
                case TelephonyManager.CALL_STATE_RINGING://来电-铃声响起
                    String address = NumberAddressQueryDao.getAddress(incomingNumber);
                    Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();

                    myToast(address);

                    case TelephonyManager.CALL_STATE_IDLE://电话挂断

                        if(view!=null)
                        {
                            wm.removeView(view);
                            view =null;
                        }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        tm.listen(listener,PhoneStateListener.LISTEN_NONE);

        listener = null;

        //取消注册监听去电

        unregisterReceiver(receiver);
        receiver = null;

    }
}
