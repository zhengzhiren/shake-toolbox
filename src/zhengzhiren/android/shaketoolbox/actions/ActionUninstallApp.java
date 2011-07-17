package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.AppManager;
import android.content.Context;

/**
 * 操作——卸载当前程序
 * 
 * @author ZZR
 * 
 */
public class ActionUninstallApp extends Action {

	private static final String PREF_UNINSTALL_APP = "uninstall_app";

	public ActionUninstallApp(Context context) {
		super(context);
	}

	@Override
	public int getIcon() {
		return R.drawable.uninstall;
	}

	@Override
	public int getName() {
		return R.string.action_uninstall_app_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_uninstall_app_desc;
	}

	@Override
	public void invokeAction() {
		String packageName = AppManager.getTopActivity(mContext)
				.getPackageName();
		AppManager.uninstall(mContext, packageName);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_UNINSTALL_APP;
	}

}
