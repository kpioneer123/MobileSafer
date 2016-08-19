package com.cloudhome.mobilesafer.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.receiver.MyAppWidget;
import com.cloudhome.mobilesafer.utils.SystemInfoUtils;


/**
 * 在服务中把widget更新
 */
public class UpdateAppWidgetService extends Service {

	private AppWidgetManager awm;

	private Timer timer;// 定时器
	private TimerTask task;
	private ScreenReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// 监听锁屏事件
		receiver = new ScreenReceiver();
		IntentFilter filter = new IntentFilter();
		// 设置监听锁屏
		filter.addAction(Intent.ACTION_SCREEN_OFF);//锁屏的事件
		filter.addAction(Intent.ACTION_SCREEN_ON);//屏幕打开的事件
		registerReceiver(receiver, filter);
		startUpdate();
	}

	private void startUpdate() {
		awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {

				System.out.println("开始耗电了....");

				// 激活的组件-通讯 打开MyAppWidget.class
				ComponentName provider = new ComponentName(
						UpdateAppWidgetService.this, MyAppWidget.class);
				// 远程View
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.mywidget);
				views.setTextViewText(
						R.id.process_count,
						"正在运行的进程:"
								+ SystemInfoUtils
								.getRunningProcessCount(UpdateAppWidgetService.this));
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ Formatter
								.formatFileSize(
										UpdateAppWidgetService.this,
										SystemInfoUtils
												.getAvailRam(UpdateAppWidgetService.this)));
				// 动作
				Intent intent = new Intent();
				intent.setAction("com.cloudhome.mobilesafer.service.killprocess");
				// 延期意图
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						UpdateAppWidgetService.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);

			}
		};
		timer.schedule(task, 0, 4000);
	}

	private class ScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()==Intent.ACTION_SCREEN_OFF){
				//锁屏
				if(timer != null&& task != null){
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;

				}
			}else if(intent.getAction()==Intent.ACTION_SCREEN_ON){
				//屏幕打开
				if(timer == null&&task == null){
					startUpdate();
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消监听锁屏和开屏
		unregisterReceiver(receiver);
		receiver = null;
		// 取消定时器工作
		if(timer != null&& task != null){
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;

		}
	}

}
