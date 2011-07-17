package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.AppManager;
import android.content.Context;

/**
 * 操作——显示当前应用程序信息
 * 
 * @author ZZR
 * 
 */
public class ActionShowAppDetails extends Action {

	private static final String PREF_SHOW_APP_DETAILS = "show_app_details";

	public ActionShowAppDetails(Context context) {
		super(context);
	}

	@Override
	public int getIcon() {
		return R.drawable.app_detail;
	}

	@Override
	public int getName() {
		return R.string.action_show_app_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_show_app_desc;
	}

	@Override
	public void invokeAction() {
		String packageName = AppManager.getTopActivity(mContext)
				.getPackageName();
		AppManager.showInstalledAppDetails(mContext, packageName);

	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_SHOW_APP_DETAILS;
	}

}
