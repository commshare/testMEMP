package net.cybisoft.looplayer;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
	private String TAG="BootReceiver";
	// 系统启动完成
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {

			final Context ctx = context;
		// 当收听到的事件是“BOOT_COMPLETED”时，就创建并启动相应的Activity和Service
		if (intent.getAction().equals(ACTION)) {
/*			// Shows a welcome toast.
		*//*通过context.getString可以获取到字符串
		* *//*
			Toast.makeText(context, context.getString(R.string.app_name) + context.getString(R.string.welcome), Toast.LENGTH_LONG).show();

			// Waits a bit then the main activity.
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					// Auto start main activity when device starts-up.
					Intent myIntent = new Intent(ctx, MainActivity.class);
					myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ctx.startActivity(myIntent);

				}
			}, 5000);*/

			//立即启动
			Intent myIntent = new Intent(ctx, MainActivity.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(myIntent);
		}else
			Log.e(TAG, "intent.getAction() ["+intent.getAction()+"]");
	}

}
