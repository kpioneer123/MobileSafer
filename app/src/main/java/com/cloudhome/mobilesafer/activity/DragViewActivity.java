package com.cloudhome.mobilesafer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;

import java.lang.reflect.Field;

/**
 * Created by xionghu on 2016/7/28.
 * Email：965705418@qq.com
 */
public class DragViewActivity extends Activity {

    protected static final String TAG = "DragViewActivity";
    private ImageView iv_dragview;
    private int mWidth;
    private int mHeight;
    private TextView tv_top,tv_bottom;
    private SharedPreferences sp;
    private long[] mHits = new long[2];
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drag_view);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        Display display = getWindowManager().getDefaultDisplay();


        Point size = new Point();
        display.getSize(size);
        mWidth= size.x;
        mHeight= size.y;



        iv_dragview = (ImageView) findViewById(R.id.iv_dragview);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);


        int lastX = sp.getInt("lastX", 0);
        int lastY= sp.getInt("lastY", 0);
        Log.e(TAG, "取出保存的值("+lastX+","+lastY+")");



        if(lastY > mHeight/2){
            //当前控件在底部
            tv_top.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.INVISIBLE);
        }else{
            //当前控件在头部
            tv_top.setVisibility(View.INVISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
        }

/**
 * 一个控件或者View从创建到显示过程的主要方法
 * 1.构造方法
 * 2.测量-onMeasure(boolean,int,int,int,int);
 * 3.指定位置-onLayout();
 * 4.onDraw(canvas);
 */
        //iv_dragview.layout(lastX, lastY, lastX+iv_dragview.getWidth(), lastY+iv_dragview.getHeight());
        //布局的第一个阶段是不能用这个方法的
        Log.e(TAG, "控件的高和宽("+iv_dragview.getWidth()+","+iv_dragview.getHeight()+")");



        //使用第一个阶段就起作用的方法
        RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) iv_dragview.getLayoutParams() ;
        params.leftMargin= lastX;
        params.topMargin = lastY;
        iv_dragview.setLayoutParams(params );

        iv_dragview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				System.out.println("点击了");
                Log.e(TAG, "点击了");
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {

                    Toast.makeText(DragViewActivity.this,"点击了",Toast.LENGTH_SHORT).show();
                    //双击居中
                    iv_dragview.layout(mWidth/2-iv_dragview.getWidth()/2, iv_dragview.getTop(), mWidth/2+iv_dragview.getWidth()/2, iv_dragview.getBottom());
                    saveData();//保存坐标
                }

            }
        });


        // 设置触摸事件在activity中，只对其设置触摸事件，触摸事件必须返回true，如果有设置了点击事件，这个事件需要把触摸事件的返回值返回true
        iv_dragview.setOnTouchListener(new View.OnTouchListener() {


            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指按下屏幕
                        //1.记录手指第一次按下的坐标
                        startX = event.getRawX();
                        startY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE://手指在屏幕上移动

                        //2.来电一个新的坐标
                        float newX = event.getRawX();
                        float newY = event.getRawY();

                        //3.计算偏移量
                        int dX = (int) (newX - startX);
                        int dY = (int) (newY - startY);

                        //4.根据偏移量更新控件的位置
                        //屏蔽非法拖动
                        int newl = iv_dragview.getLeft()   + dX;
                        int newt = iv_dragview.getTop()    + dY;
                        int newr = iv_dragview.getRight()  + dX;
                        int newb = iv_dragview.getBottom() + dY;

                        if(newl <0 ||newt<0||newr>mWidth||newb>mHeight-getStatusBarHeight())
                        {
                            break;
                        }


                        if(newt > mHeight/2){
                            //当前控件在底部
                            tv_top.setVisibility(View.VISIBLE);
                            tv_bottom.setVisibility(View.INVISIBLE);
                        }else{
                            //当前控件在头部
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                        }

                        iv_dragview.layout(newl, newt, newr, newb);

                        //5.重新记录坐标
                        startX = event.getRawX();
                        startY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP://手指离开屏幕

                        saveData();
                        break;

                }

                return false;

            }

        });
    }


    /**
     * 用于获取状态栏的高度。
     * @return 返回状态栏高度的像素值。
    导入包java.lang.reflect.Field


     */
    private int statusBarHeight;
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void saveData() {
        SharedPreferences.Editor editor =sp.edit();
        editor.putInt("lastX", iv_dragview.getLeft());
        editor.putInt("lastY", iv_dragview.getTop());
        editor.commit();
    }

}
