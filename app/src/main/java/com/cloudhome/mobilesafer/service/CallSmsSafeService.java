package com.cloudhome.mobilesafer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.cloudhome.mobilesafer.activity.CallSmsSafeActivity;
import com.cloudhome.mobilesafer.db.BlackNumberDao;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xionghu on 2016/8/4.
 * Email：965705418@qq.com
 */
public class CallSmsSafeService extends Service {

    private InnerSMSReceiver receiver;
    private BlackNumberDao dao;
    private TelephonyManager tm;

    private MyPhoneStateListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InnerSMSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);

                String sender = sms.getOriginatingAddress();

                if (dao.query(sender)) //要拦截的电话号码
                {

                    String mode = dao.queryMode(sender);
                    if ("1".equals(mode) || "2".equals(mode)) {
                        //短信拦截
                        System.out.println("拦截到一条短信");
                        abortBroadcast();//把短信广播给终止


                    }
                }

                String body = sms.getMessageBody();

                if (body.contains("发票")) {
                    System.out.print("拦截到一条广告短信");
                    abortBroadcast();
                }

            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new BlackNumberDao(this);

        //注册监听短信
        receiver = new InnerSMSReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");//对短信感兴趣
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(receiver, filter);

        //监听来电
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {

                case TelephonyManager.CALL_STATE_RINGING://电话打进来

                    //电话挂断-电话拦截
                    if (dao.query(incomingNumber)) {
                        String mode = dao.queryMode(incomingNumber);
                        if ("0".equals(mode) || "2".equals(mode)) {
                            //把当前电话挂断
                            endCall();//把电话挂断
						//deleteCallLog(incomingNumber);
                            //观察数据变化后，再去删除
                            Uri uri = Uri.parse("content://call_log/calls");
                            //注册内容观察者
                            getContentResolver().registerContentObserver(uri,true,new MyContentObserver(new Handler(),incomingNumber));

                        }
                    }
                    break;


                default:

                    break;

            }
        }
    }

    /**
     * 删除呼叫记录
     * @param incomingNumber
     */
    public void deleteCallLog(String incomingNumber)
    {

       ContentResolver resolver =  getContentResolver();
        Uri uri =Uri.parse("content://call_log/calls");

        resolver.delete(uri,"number=?",new String[]{ incomingNumber });

    }
    private void endCall() {
          //用反射得到ServiceManager的实例
//
          // 1.得到字节码
       try {
           Class clazz =   CallSmsSafeActivity.class.getClassLoader().loadClass("android.os.ServiceManager");

           // 2.得到对应的方法getService
           Method method = clazz.getMethod("getService",String.class);

           // 3.得到实例

           // 4.执行这个方法

           //静态方法不需要传实例
           IBinder b = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
           // 5.拷贝.aidl文件
           // 6.生成Java代码
           ITelephony service = ITelephony.Stub.asInterface(b);

           // 7.执行Java中的endCall();
           service.endCall();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (RemoteException e) {
           e.printStackTrace();
       }

    }

    private class MyContentObserver extends ContentObserver{

        private String incomingNumber;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */

        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            //当观察的路径变化的时候
            deleteCallLog(incomingNumber);
              getContentResolver().unregisterContentObserver(this);

        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消注册监听短信
        unregisterReceiver(receiver);
        receiver = null;
        //取消监听电话注册
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
    }


}
