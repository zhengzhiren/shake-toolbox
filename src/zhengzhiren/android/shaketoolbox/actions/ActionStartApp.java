/**
 * 
 */
package zhengzhiren.android.shaketoolbox.actions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.shaketoolbox.StartAppPreference;

/**
 * @author wjx
 * 
 */
public class ActionStartApp extends Action {

	private static final String PREF_START_APP = "start_app";

	/**
	 * @param context
	 */
	public ActionStartApp(Context context) {
		super(context);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_START_APP;
	}

	@Override
	public int getIcon() {
		return R.drawable.start_app;
	}

	@Override
	public int getName() {
		return R.string.action_start_app_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_start_app_desc;
	}

	@Override
	public void invokeAction() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		String packgename=sp.getString(StartAppPreference.APP_LAUNCH_INTENT_NAME,null);
		if(packgename==null||packgename.isEmpty())
		{
			return;
		} 
		PackageManager pm= mContext.getApplicationContext().getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(packgename);
		mContext.startActivity(intent); 
	}

}
