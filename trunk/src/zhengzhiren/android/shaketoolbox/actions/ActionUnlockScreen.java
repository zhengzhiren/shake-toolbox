//package zhengzhiren.android.shaketoolbox.actions;
//
//import zhengzhiren.android.shaketoolbox.R;
//import android.app.KeyguardManager;
//import android.app.KeyguardManager.KeyguardLock;
//import android.content.Context;
//
//public class ActionUnlockScreen extends Action {
//
//	private static final String PREF_LOCK_SCREEN = "unlock_screen";
//
//	public ActionUnlockScreen(Context context) {
//		super(context);
//	}
//
//	@Override
//	protected String getSharedPrefsKey() {
//		return PREF_LOCK_SCREEN;
//	}
//
//	@Override
//	public int getIcon() {
//		return R.drawable.unlock_screen;
//	}
//
//	@Override
//	public int getName() {
//		return R.string.action_unlock_screen_name;
//	}
//
//	@Override
//	public int getDescription() {
//		return R.string.action_unlock_screen_desc;
//	}
//
//	@Override
//	public void invokeAction() throws Exception {
//		KeyguardManager km = (KeyguardManager) mContext
//				.getSystemService(Context.KEYGUARD_SERVICE);
//		KeyguardLock lock = km.newKeyguardLock(this.getClass().getName());
//		lock.disableKeyguard();
//	}
//
//}
