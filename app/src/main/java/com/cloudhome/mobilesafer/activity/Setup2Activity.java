package com.cloudhome.mobilesafer.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.view.SettingItemView;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class Setup2Activity extends BaseSetupActivity {


    public static Setup2Activity Setup2instance=null;
    private SettingItemView siv_bind_sim;
    /**
     * 电话服务-读取sim信息，监听来电和挂断来电
     */
    private TelephonyManager tm ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //调用父类的onCreate方法

        //判断是否做过设置向导，如果没有做过就跳转到设置向导页面的第一个页面，否则就加载手机防盗页面
        setContentView(R.layout.activity_setup2);

        Setup2instance=this;

        init();
    }


    private void init() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
      siv_bind_sim = (SettingItemView)findViewById(R.id.siv_bind_sim);

        String sim = sp.getString("sim","") ;

        if(TextUtils.isEmpty(sim)){
            siv_bind_sim.setChecked(false);
        }else{
            siv_bind_sim.setChecked(true);
        }
        siv_bind_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sp.edit();

                if(siv_bind_sim.isChecked())
                {
                    siv_bind_sim.setChecked(false);

                    editor.putString("sim", null);
                }else {

                    siv_bind_sim.setChecked(true);


                    String sim = tm.getSimSerialNumber();

                    editor.putString("sim", sim +"444");

                }

                editor.commit();
            }
        });
    }



    @Override
    public void showPre() {

        //当前页面关闭
        finish();
        overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
    }

    @Override
    public void showNext() {

        String sim = sp.getString("sim","");
        if(TextUtils.isEmpty(sim))
        {
            Toast.makeText(getApplicationContext(),"你还没有绑定sim卡",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this,Setup3Activity.class);

        startActivity(intent);



        overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);

    }


}