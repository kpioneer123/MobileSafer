package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.adapter.CommonNumberQueryAdapter;

/**
 * Created by xionhgu on 2016/8/21.
 * Email：965705418@qq.com
 */
public class CommonNumberQueryActivity extends Activity {
    private  static String path = "/data/data/com.cloudhome.mobilesafer/files/commonnum.db";
    private ExpandableListView elv;
    private CommonNumberQueryAdapter mapater;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commonnumber_query);

        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        mapater=new CommonNumberQueryAdapter(CommonNumberQueryActivity.this,db );
        elv = (ExpandableListView) findViewById(R.id.elv);
        elv.setAdapter(mapater);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(db != null){
            db.close();
            db = null;
        }

    }
}
