package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * 操作——点亮屏幕
 * 
 * @author ZZR
 * 
 */
public class ActionScreenOn extends Action {

	private static String PREF_SCREEN_ON = "screen_on";

	private static String TAG = "ActionScreenOn";

	private static long TIME_OUT = 5000;

	public ActionScreenOn(Context context) {
		super(context);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_SCREEN_ON;
	}

	@Override
	public int getIcon() {
		return R.drawable.screen_on;
	}

	@Override
	public int getName() {
		return R.string.action_screen_on_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_screen_on_desc;
	}

	@Override
	public void invokeAction() {
		PowerManager pm = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
		wl.acquire(TIME_OUT);
	}
}
