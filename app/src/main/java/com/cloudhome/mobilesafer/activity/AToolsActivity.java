package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/26.
 * Email：965705418@qq.com
 */
public class AToolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

setContentView(R.layout.activity_atools);
    }

    /**
     * 点击事件-进入号码归属地查询
     * @param view
     */
    public   void numberAddressQuery(View view)
    {
        Intent intent = new Intent(this,NumberAddressQueryActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
