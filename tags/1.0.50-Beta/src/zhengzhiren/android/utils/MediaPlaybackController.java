package zhengzhiren.android.utils;

import android.content.Context;
import android.content.Intent;

/**
 * 通过广播Intent控制系统自带的音乐播放器的播放状态（上一首、下一首、播放/暂停……）
 * 
 * @author ZZR
 * 
 */
public class MediaPlaybackController {

	// static final String SERVICECMD = "com.android.music.musicservicecommand";
	static final String CMDNAME = "command";
	static final String CMDTOGGLEPAUSE = "togglepause";
	// static final String CMDSTOP = "stop";
	// static final String CMDPAUSE = "pause";
	// static final String CMDPREVIOUS = "previous";
	// static final String CMDNEXT = "next";

	private static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
	private static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
	private static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";
	private static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";

	/**
	 * 系统音乐播放器的包名
	 */
	private static final String PACKAGE_NAME = "com.android.music";

	/**
	 * 播放服务的类名
	 */
	private static final String SERVICE_CLASS_NAME = "com.android.music.MediaPlaybackService";

	// /**
	// * 启动系统音乐播放器服务
	// *
	// * @param context
	// */
	// public static void startService(Context context) {
	// Intent i = new Intent();
	// i.setClassName(PACKAGE_NAME, SERVICE_CLASS_NAME);
	// context.startService(i);
	// }

	/**
	 * 播放/暂停
	 * 
	 * @param context
	 */
	public static void togglePlay(Context context) {
		Intent i = new Intent();
		i.setClassName(PACKAGE_NAME, SERVICE_CLASS_NAME);
		i.putExtra(CMDNAME, CMDTOGGLEPAUSE);
		context.startService(i);
	}

	/**
	 * 下一首歌
	 */
	public static void nextSong(Context context) {
		Intent intent = new Intent();
		intent.setAction(NEXT_ACTION);
		context.sendBroadcast(intent);
	}

	// /**
	// * 上一首歌
	// *
	// * @param context
	// */
	// public static void previousSong(Context context) {
	// Intent intent = new Intent();
	// intent.setAction(PREVIOUS_ACTION);
	// context.sendBroadcast(intent);
	// }

	// /**
	// * 暂停播放
	// *
	// * @param context
	// */
	// public static void pause(Context context) {
	// Intent intent = new Intent();
	// intent.setAction(PAUSE_ACTION);
	// context.sendBroadcast(intent);
	// }

}
