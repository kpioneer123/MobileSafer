package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.cloudhome.mobilesafer.R;

/**
 * Created by xionghu on 2016/7/28.
 * Email：965705418@qq.com
 */
public class DragViewActivity extends Activity {


    private ImageView iv_dragview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drag_view);

        iv_dragview = (ImageView) findViewById(R.id.iv_dragview);
    }
}
