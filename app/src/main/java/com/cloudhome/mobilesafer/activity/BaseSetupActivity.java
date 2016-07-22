package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 公共类，基类，父类
 * Created by xionghu on 2016/7/21.
 * Email：965705418@qq.com
 */
public abstract class BaseSetupActivity extends Activity {

    /**
     * 1.定义一个手势识别器
     */
    private GestureDetector detector;
    protected SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp =getSharedPreferences("config",MODE_PRIVATE);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            /**
             * @param e1
             * @param e2
             * @param velocityX x轴速度
             * @param velocityY y轴速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //屏蔽Y轴方向斜滑

                if(Math.abs(e2.getY() -e1.getY())>100)
                {
                    return true;
                }

                //屏蔽滑动慢：速度单位:像素每秒

                if(Math.abs(velocityX)<100)
                {
                    return true;
                }
                if(e2.getX() - e1.getX() >200)
                {

                    showPre();
                    return true;
                }

                if(e1.getX() - e2.getX() >200)
                {
                    showNext();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     *显示上一个页面的抽象方法
     */
    public abstract void showPre();

    /**
     * 显示下一个页面的抽象方法
     */
    public abstract void showNext();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void next(View view)
    {
        showNext();
    }


    public void pre(View view)
    {

        showPre();
//        //当前页面关闭
//        finish();
//        overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
    }


}
