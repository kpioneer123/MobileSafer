package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.utils.SmsBackupUtils;

import java.io.File;
import java.io.IOException;

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
     *
     * @param view
     */
    public void numberAddressQuery(View view) {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
    }

    public void smsBackup(View view) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
          final  File file = new File(Environment.getExternalStorageDirectory(), "smsBackup.xml");

            final   ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("正在短信备份中...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();

            try {
                SmsBackupUtils.smsBackup(AToolsActivity.this, file.getAbsolutePath(), new SmsBackupUtils.SmsBackupCallBack() {
                    @Override
                    public void smsBackupBefore(int total) {

                        dialog.setMax(total);

                    }

                    @Override
                    public void smsBackupProgress(int progress) {
                        dialog.setProgress(progress);
                    }
                });
             //   Toast.makeText(this, "短信备份成功", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            } catch (IOException e) {

         //       Toast.makeText(this, "短信备份失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                dialog.dismiss();
            }
        } else {

            Toast.makeText(this, "sd卡不可用", Toast.LENGTH_SHORT).show();
        }
    }

    public void commonNumberQuery(View view)
    {
        Intent intent = new Intent(this,CommonNumberQueryActivity.class);
        startActivity(intent);
    }
}
