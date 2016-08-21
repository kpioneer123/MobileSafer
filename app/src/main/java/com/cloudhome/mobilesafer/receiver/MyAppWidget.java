package com.cloudhome.mobilesafer.receiver;
import com.cloudhome.mobilesafer.service.UpdateAppWidgetService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by xionghu on 2016/8/19.
 * Email：965705418@qq.com
 */
public class MyAppWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive");
		super.onReceive(context, intent);
	}

	//如果更新数据UpdateAppWidgetService 服务被杀死，则在onUpdate 中重新激活进程
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println("onUpdate");
		Intent intent = new Intent(context,UpdateAppWidgetService.class);
		context.startService(intent);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		System.out.println("onAppWidgetOptionsChanged");
		// TODO Auto-generated method stub
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println("onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	//在onEnabled中启动跟新数据服务
	@Override
	public void onEnabled(Context context) {
		System.out.println("onEnabled");
		Intent intent = new Intent(context,UpdateAppWidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}

	//把UpdateAppWidgetService服务给停掉
	@Override
	public void onDisabled(Context context) {
		System.out.println("onDisabled");
		Intent intent = new Intent(context,UpdateAppWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	}

//  widget的生命周期
//  1.第一个 widget被创建的时候
//	onReceive                        每个方法都用到onReceive，通讯就用广播
//  onEnabled                        当第一个widget被创建的时候执行，系统初始化资源，比如创建服务
//  onReceive
//  onUpdate                         每新创建一个widget的时候都被执行，系统默认更新时间30分，时间到了也会之星
//  onAppWidgetOptionsChanged	     每新创建一个widget的时候执行
//	2.创建第二个
//	onReceive
//	onUpdate
//	onReceive
//	onAppWidgetOptionsChanged
//
//	3.删除第一个widget
//	onReceive
//	onDeleted                         只要删除任意一个widget就会执行
//
//	4.删除最后一个widget时候的日志
//	onReceive
//	onDeleted
//	onReceive
//	onDisabled		                    删除最后一个widget的时候执行，适合释放资源，比如停止服务。
	
	
	
}
