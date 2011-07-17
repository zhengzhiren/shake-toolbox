package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.Telephony;
import android.content.Context;

/**
 * 操作——拒接电话、结束通话
 * 
 * @author ZZR
 * 
 */
public class ActionEndCall extends Action {

	private static final String PREF_END_CALL = "end_call";

	public ActionEndCall(Context context) {
		super(context);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_END_CALL;
	}

	@Override
	public int getIcon() {
		return R.drawable.end_call;
	}

	@Override
	public int getName() {
		return R.string.action_end_call_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_end_call_desc;
	}

	@Override
	public void invokeAction() {
		try {
			Telephony.endCall(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
