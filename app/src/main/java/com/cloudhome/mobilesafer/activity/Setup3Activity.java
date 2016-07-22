package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/19.
 * Email：965705418@qq.com
 */
public class Setup3Activity extends BaseSetupActivity {


    public static Setup3Activity Setup3instance=null;
    private EditText et_safenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否做过设置向导，如果没有做过就跳转到设置向导页面的第一个页面，否则就加载手机防盗页面
        setContentView(R.layout.activity_setup3);

        et_safenumber = (EditText) findViewById(R.id.et_safenumber);
        et_safenumber.setText(sp.getString("safenumber",""));
        Setup3instance =this;
    }

    @Override
    public void showPre() {
        //当前页面关闭
        finish();
        overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
    }

    @Override
    public void showNext() {


        //校验是否设置过安全密码
        String number = et_safenumber.getText().toString().trim();
        if(TextUtils.isEmpty(number))
        {
            Toast.makeText(getApplicationContext(),"安全号码还没有设置",Toast.LENGTH_SHORT).show();
            return;
        }

        //保存
      SharedPreferences.Editor editor =  sp.edit();
        editor.putString("safenumber",number);
        editor.commit();

        Intent intent = new Intent(this,Setup4Activity.class);

        startActivity(intent);



        overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);
    }


    public  void selectContact(View view)
    {

        Intent intent = new Intent(this,SelectContactActivity.class);
        startActivityForResult(intent,0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null)
      return;

        String number = data.getStringExtra("number").replace("-","");
        et_safenumber.setText(number);
    }
}