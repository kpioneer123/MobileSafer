package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.db.NumberAddressQueryDao;

/**
 * Created by xionghu on 2016/7/26.
 * Email：965705418@qq.com
 */
public class NumberAddressQueryActivity extends Activity {


    private EditText et_number;
    private TextView tv_result;
    //震动服务
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_address_query);


        et_number = (EditText) findViewById(R.id.et_number);
        tv_result = (TextView) findViewById(R.id.tv_result);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        et_number.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                if(s!=null&s.length()>=3)
                {
                    String address =  NumberAddressQueryDao.getAddress(s.toString());
                    tv_result.setText(address);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //查询电话号码的归属地
    public void query(View view)
    {

        String number = et_number.getText().toString().trim();

        if(TextUtils.isEmpty(number))
        {
            Toast.makeText(getApplicationContext(),"电话号码不能为空",Toast.LENGTH_SHORT).show();

            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
            et_number.startAnimation(shake);


//            shake.setInterpolator(new Interpolator() {
//                @Override
//                public float getInterpolation(float x) {
//                    //方程式 = y 笛卡尔
//
//                    return y;
//                }
//            });

            //震动效果
            vibrator.vibrate(2000);

//            long[] pattern ={500,500,1000,1000,2000,2000};
//            //repeate -1不重复 ，0重复
//            vibrator.vibrate(pattern,-1);
        }else{

String address = NumberAddressQueryDao.getAddress(number);

            tv_result.setText(address);
          //  System.out.println("您要查询的电话号码："+address);
        }



    }
}
