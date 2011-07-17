package zhengzhiren.android.shaketoolbox.receivers;

import zhengzhiren.android.shaketoolbox.ToolboxService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 系统启动时，启动服务
 * 
 * @author ZZR
 * 
 */
public class BootReceivers extends BroadcastReceiver {

	private static final String PREFS_START_ON_BOOT = "start_on_boot";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean startOnBoot = sp.getBoolean(PREFS_START_ON_BOOT, false);
		if (startOnBoot) {
			Intent i = new Intent();
			i.setClass(context, ToolboxService.class);
			context.startService(i);
		}
	}
}
