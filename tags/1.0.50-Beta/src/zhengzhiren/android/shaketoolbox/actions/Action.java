package zhengzhiren.android.shaketoolbox.actions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 表示一个操作
 * 
 * @author ZZR
 * 
 */
public abstract class Action {

	protected final Context mContext;

	public Action(Context context) {
		mContext = context;
	}

	/**
	 * 返回所有的Action
	 * 
	 * @return
	 */
	public static List<Action> getAllActions(Context context) {
		ArrayList<Action> list = new ArrayList<Action>();
		list.add(new ActionCaptureScreen(context));
		list.add(new ActionShowAppDetails(context));
		list.add(new ActionUninstallApp(context));
		list.add(new ActionToggleMusic(context));
		list.add(new ActionNextSong(context));
		list.add(new ActionAnswerCall(context));
		list.add(new ActionEndCall(context));
		// list.add(new ActionUnlockScreen(context));
		return list;
	}

	/**
	 * 返回所有启用的Action
	 * 
	 * @param context
	 * @return
	 */
	public static List<Action> getEnabledActions(Context context) {
		ArrayList<Action> enabledActions = new ArrayList<Action>();
		List<Action> allActions = getAllActions(context);
		for (Action action : allActions) {
			if (action.isEnabled()) {
				enabledActions.add(action);
			}
		}
		return enabledActions;
	}

	/**
	 * 用于保存状态到SharedPreferences中的key
	 * 
	 * @return
	 */
	protected abstract String getSharedPrefsKey();

	/**
	 * 此操作是否启用
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		return sp.getBoolean(getSharedPrefsKey(), false);
	}

	/**
	 * 启用/禁用此操作（保存状态到配置文件）
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor editor = sp.edit();
		editor.putBoolean(getSharedPrefsKey(), enabled);
		editor.commit();
	}

	/**
	 * 获取操作所显示的图标
	 * 
	 * @return 图标的资源id
	 */
	public abstract int getIcon();

	/**
	 * 获取操作的名称
	 * 
	 * @return 操作名称的资源id
	 */
	public abstract int getName();

	/**
	 * 获取操作的描述信息
	 * 
	 * @return 描述信息的资源id
	 */
	public abstract int getDescription();

	/**
	 * 调用操作，执行具体的功能。
	 */
	public abstract void invokeAction();

}
