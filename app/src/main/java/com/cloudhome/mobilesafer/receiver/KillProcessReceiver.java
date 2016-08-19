package com.cloudhome.mobilesafer.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningAppProcessInfo procerssInfo :am.getRunningAppProcesses()){
			am.killBackgroundProcesses(procerssInfo.processName);
		}

	}

}
