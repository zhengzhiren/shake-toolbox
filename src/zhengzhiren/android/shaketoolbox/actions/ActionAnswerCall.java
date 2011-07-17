package zhengzhiren.android.shaketoolbox.actions;

import android.content.Context;
import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.Telephony;

/**
 * 操作——接听呼入电话
 * 
 * @author ZZR
 * 
 */
public class ActionAnswerCall extends Action {

	private static final String PREF_ANSWER_CALL = "answer_call";

	public ActionAnswerCall(Context context) {
		super(context);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_ANSWER_CALL;
	}

	@Override
	public int getIcon() {
		return R.drawable.answer_call;
	}

	@Override
	public int getName() {
		return R.string.action_answer_call_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_answer_call_desc;
	}

	@Override
	public void invokeAction() {
		try {
			Telephony.answerCall(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
