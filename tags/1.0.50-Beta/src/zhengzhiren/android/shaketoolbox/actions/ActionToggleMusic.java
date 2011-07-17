package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.MediaPlaybackController;
import android.content.Context;

/**
 * 操作——播放、暂停音乐
 * 
 * @author ZZR
 * 
 */
public class ActionToggleMusic extends Action {

	private static final String PREF_TOGGLE_MUSIC = "toggle_music";

	public ActionToggleMusic(Context context) {
		super(context);
	}

	@Override
	public int getIcon() {
		return R.drawable.toggle_music;
	}

	@Override
	public int getName() {
		return R.string.action_toggle_music_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_toggle_music_desc;
	}

	@Override
	public void invokeAction() {
		MediaPlaybackController.togglePlay(mContext);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_TOGGLE_MUSIC;
	}

}
