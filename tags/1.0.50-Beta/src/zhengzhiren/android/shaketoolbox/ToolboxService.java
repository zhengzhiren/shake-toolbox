package zhengzhiren.android.shaketoolbox;

import java.util.List;

import zhengzhiren.android.hardware.ShakeDetector;
import zhengzhiren.android.hardware.ShakeDetector.OnShakeListener;
import zhengzhiren.android.shaketoolbox.actions.Action;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ToolboxService extends Service implements OnShakeListener {

	/**
	 * 服务运行时显示图标
	 */
	private static final String PREF_SHOW_ICON = "show_icon";

	/**
	 * 振动时间（毫秒）
	 */
	private static final int VIBRATE_MILLISECOND = 40;

	/**
	 * 摇晃时振动
	 */
	private static final String PREF_VIBRATE_ON_SHAKE = "vibrate_on_shake";
	private static final int NOTIFICATION_ID = 0;

	private SharedPreferences mSharedPrefs;
	private List<Action> mEnabledActions;
	private ShakeDetector mShakeDetector;
	private Vibrator mVibrator;

	private boolean mVibrateOnShake;

	@Override
	public void onCreate() {
		super.onCreate();

		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mEnabledActions = Action.getEnabledActions(this);
		mShakeDetector = new ShakeDetector(this);
		mVibrateOnShake = mSharedPrefs.getBoolean(PREF_VIBRATE_ON_SHAKE, true);
		if (mVibrateOnShake) {
			mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		}
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
		return null;
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