package zhengzhiren.android.shaketoolbox;

import java.util.List;

import zhengzhiren.android.hardware.ShakeDetector;
import zhengzhiren.android.hardware.ShakeDetector.OnShakeListener;
import zhengzhiren.android.shaketoolbox.actions.Action;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ToolboxService extends Service implements OnShakeListener {

	/**
	 * 服务运行时显示图标
	 */
	private static final String PREF_SHOW_ICON = "show_icon";
	private static final String PREF_PRIORITY_MODE="priority_mode";
	/**
	 * 振动时间（毫秒）
	 */
	private static final int VIBRATE_MILLISECOND = 40;

	/**
	 * 摇晃时振动
	 */
	private static final String PREF_VIBRATE_ON_SHAKE = "vibrate_on_shake";
	private static final String PREF_POCKET_MODE = "pocket_mode";
	private static final int NOTIFICATION_ID = 0;

	private SharedPreferences mSharedPrefs;
	private List<Action> mEnabledActions;
	private ShakeDetector mShakeDetector;
	private Vibrator mVibrator;

	private boolean mVibrateOnShake;
	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 屏幕关闭时重启传感器，不可删除
			mShakeDetector.stop();
			mShakeDetector.start();
			// 启动口袋检测服务
			invokePocketDetectorService(true);
		}
	};
	private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 停止口袋检测服务
			invokePocketDetectorService(false);
		}
	};
	private final IBinder binder = new ToolBoxServiceBinder();

	public class ToolBoxServiceBinder extends Binder {
		ToolboxService getService() {
			return ToolboxService.this;
		}

	}

	/**
	 * 操作口袋检测服务。参数true-启动，false-停止
	 */
	private void invokePocketDetectorService(boolean startOrStop) {
		if (getPocketMode()) {
			if (startOrStop) {
				Intent intentpocket = new Intent();
				intentpocket.setClass(ToolboxService.this,
						PocketDetectorService.class);
				startService(intentpocket);
			} else {
				Intent intentpocket = new Intent();
				intentpocket.setClass(ToolboxService.this,
						PocketDetectorService.class);
				stopService(intentpocket);
			}
		}
	}

	/**
	 * 获取口袋模式
	 */
	private boolean getPocketMode() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean(PREF_POCKET_MODE, false);
	}
	/**
	 * 获取口袋模式
	 */
	private boolean getPriorityMode() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean(PREF_PRIORITY_MODE, false);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		if(getPriorityMode())
		{
			this.setForeground(true);
		}
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mEnabledActions = Action.getEnabledActions(this);
		mShakeDetector = new ShakeDetector(this);
		int threshold = this.mSharedPrefs.getInt(
				SeekBarPreference.PREF_SHAKE_SENSITIVITY,
				SeekBarPreference.DEFAULT_VALUE);
		mShakeDetector.setShakeThreshold(SeekBarPreference
				.getpre_ShakeThreshold_on_shake_value(threshold));
		mVibrateOnShake = mSharedPrefs.getBoolean(PREF_VIBRATE_ON_SHAKE, true);
		if (mVibrateOnShake) {
			mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		}
		// 待机时也监听传感器
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenOffReceiver, filter);
		IntentFilter filterScreenOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
		registerReceiver(mScreenOnReceiver, filterScreenOn);
	}

	/**
	 * 启动服务
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 操作为空，则结束服务
		if (mEnabledActions.isEmpty()) {
			stopSelf();
			return START_STICKY;
		}

		mShakeDetector.registerOnShakeListener(this);
		try {
			// 启动摇晃检测
			mShakeDetector.start();
			if (mSharedPrefs.getBoolean(PREF_SHOW_ICON, true)) {
				// 在通知栏显示图标
				this.showNotificationIcon();
			}
		} catch (UnsupportedOperationException e) {
			// 启动服务失败，提示用户
			Toast.makeText(this, R.string.toast_start_service_failed,
					Toast.LENGTH_LONG).show();
			stopSelf();
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void stopShakeAccelerometer() {
		mShakeDetector.stop();
	}

	public void startShakeAccelerometer() {
		mShakeDetector.start();
	}

	/**
	 * 摇晃发生时，执行操作
	 */
	@Override
	public void onShake() {
		// 振动
		if (mVibrateOnShake) {
			mVibrator.vibrate(VIBRATE_MILLISECOND);
		}
		try {
			for (Action action : mEnabledActions) {
				action.invokeAction();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 停止服务
	 */
	@Override
	public void onDestroy() {
		mShakeDetector.stop();
		mShakeDetector.unregisterOnShakeListener(this);
		unregisterReceiver(mScreenOffReceiver);
		unregisterReceiver(mScreenOnReceiver);
		if (mSharedPrefs.getBoolean(PREF_SHOW_ICON, true)) {
			// 移除通知栏图标
			this.removeNotificationIcon();
		}
	}

	/**
	 * 添加通知栏图标
	 */
	private void showNotificationIcon() {
		NotificationManager nm = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);

		CharSequence tickerText = this.getResources().getText(
				R.string.notification_ticker_text);
		CharSequence contentTitle = this.getResources().getText(
				R.string.app_name);
		CharSequence contentText = this.getResources().getText(
				R.string.notification_content_text);

		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		Notification notification = new Notification();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.icon = R.drawable.app_icon;
		notification.tickerText = tickerText;
		notification.setLatestEventInfo(this, contentTitle, contentText,
				pendingIntent);

		nm.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 移除通知栏图标
	 */
	private void removeNotificationIcon() {
		NotificationManager nm = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_ID);
	}
}
