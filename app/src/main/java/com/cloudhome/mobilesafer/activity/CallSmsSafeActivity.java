package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.adapter.CallSmsSafeAdapter;
import com.cloudhome.mobilesafer.adapter.HomeAdapter;
import com.cloudhome.mobilesafer.db.BlackNumberDao;
import com.cloudhome.mobilesafer.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionghu on 2016/8/3.
 * Email：965705418@qq.com
 */
public class CallSmsSafeActivity extends Activity{

    private ListView lv_blacknumber;
    private BlackNumberDao dao;
    private CallSmsSafeAdapter madapter;
    private LinearLayout ll_loading;

    /**
     * 从那个地方开始加载下一个20条
     */
    private int index = 0;

    /**
     * 数据库的总条数
     */
    private int dbCount;

    /**
     * 所有的黑名单号码
     */
    private List<BlackNumberInfo> infos =new ArrayList<BlackNumberInfo>();

    private boolean isLoading = false;
    private int pagenum = 0;
    private boolean AllLoaded=false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {


            Log.d("545555",infos.size()+"");
            if (madapter==null) {

                madapter = new CallSmsSafeAdapter(CallSmsSafeActivity.this);
                madapter.setData(infos);
                lv_blacknumber.setAdapter(madapter);

            } else {
                // 刷新页面
                madapter.notifyDataSetChanged();
            }
            isLoading = false;
            ll_loading.setVisibility(View.GONE);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_sms_safe);

        initView();

        fillData();
        initEvent();


    }

    private void initEvent() {
        //设置滑动到底部的监听
        lv_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {

            //当状态发生变化的是时候回调
            //静止--->滑动
            //滑动--->静止
            //手指滑动--->惯性滚动

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 静止状态-空闲


                        if(isLoading){
                            Toast.makeText(getApplicationContext(),"数据正在加载...",Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        if(AllLoaded)
//                        {
//                            Toast.makeText(getApplicationContext(), "没有数据了...", 0)
//                                    .show();
//                            return;
//                        }

                        int lastPosition = lv_blacknumber.getLastVisiblePosition();
                        int currenttotalSize =infos.size();//20

                        if (index >= dbCount) {
                            Toast.makeText(getApplicationContext(), "没有数据了...", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
//
//                        if(pagenum>0&&infos.size()<20)
//                        {
//                            AllLoaded =true;
//                        }

                        if(lastPosition == (currenttotalSize - 1 ))
                        {
                            pagenum++;
                            isLoading =true;
                            Toast.makeText(getApplicationContext(),"加载更多数据",Toast.LENGTH_SHORT).show();
                            index +=20;

                            fillData();
                        }
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态



                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸滑动状态

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initView() {
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);

        ll_loading= (LinearLayout) findViewById(R.id.ll_loading);

    }


    /**
     * 加载数据
     */
    private void fillData() {


        ll_loading.setVisibility(View.VISIBLE);
        dao  = new BlackNumberDao(this);

        new Thread() {

            public void run() {
                if (infos == null) {

                  //  infos =dao.queryAll();
                    infos = dao.queryPart(index);// 联网的操作或者耗时的操作

                } else {

                    infos.addAll(dao.queryPart(index));
                }
                dbCount = dao.queryCount();
                handler.sendEmptyMessage(0);
            };
        }.start();

    }


    private AlertDialog dialog;

   public void addBlackNumber(View view)
   {

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       dialog =builder.create();
       View contentview = View.inflate(this,R.layout.dialog_addblacknumber,null);
       final EditText et_number = (EditText) contentview.findViewById(R.id.et_number);

       final RadioGroup rg_mode = (RadioGroup) contentview.findViewById(R.id.rg_mode);
       Button cancel = (Button) contentview.findViewById(R.id.cancel);
       Button ok = (Button) contentview.findViewById(R.id.ok);
       ok.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

            //1.得到电话号码，拦截模式
               String number = et_number.getText().toString().trim();

              int checkedid =  rg_mode.getCheckedRadioButtonId();

               String mode = "2";
               switch (checkedid)
               {
                   case R.id.rb_number:  //电话
                       mode="0";
                       break;

                   case R.id.rb_sms:  //短信
                       mode="1";
                       break;

                   case R.id.rb_all:  //全部
                       mode="0";
                       break;


               }
            //2.判断是否为空

               if(TextUtils.isEmpty(number))
               {
                   Toast.makeText(getApplicationContext(),"电话号码不能为空",Toast.LENGTH_SHORT).show();
                   return;
               }


            //3.保存到数据库里面
               dao.add(number,mode);


            //4.保存到当前表

               BlackNumberInfo object = new BlackNumberInfo();
               object.setNumber(number);
               object.setMode(mode);
               infos.add(0,object);
            //5.对话框消掉,刷新UI
               dialog.dismiss();

                madapter.notifyDataSetChanged();

           }
       });
       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
       dialog.setView(contentview,0,0,0,0);
       dialog.show();

   }

}
