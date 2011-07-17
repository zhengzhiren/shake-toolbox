package zhengzhiren.android.shaketoolbox.actions;

import zhengzhiren.android.shaketoolbox.R;
import zhengzhiren.android.utils.MediaPlaybackController;
import android.content.Context;

/**
 * 操作——切换歌曲
 * 
 * @author ZZR
 * 
 */
public class ActionNextSong extends Action {

	private static final String PREF_NEXT_SONG = "next_song";

	public ActionNextSong(Context context) {
		super(context);
	}

	@Override
	public int getIcon() {
		return R.drawable.next_song;
	}

	@Override
	public int getName() {
		return R.string.action_next_song_name;
	}

	@Override
	public int getDescription() {
		return R.string.action_next_song_desc;
	}

	@Override
	public void invokeAction() {
		MediaPlaybackController.nextSong(mContext);
	}

	@Override
	protected String getSharedPrefsKey() {
		return PREF_NEXT_SONG;
	}

}
