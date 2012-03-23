package zhengzhiren.android.shaketoolbox;

import java.util.List;

import zhengzhiren.android.shaketoolbox.actions.Action;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * @author wjx
 * 
 */
public class PocketDetectorService extends Service implements
		SensorEventListener {

	private boolean mIsRegistered = false;
	private SensorManager mSensorMgr;
	private boolean mIsPocket;
	private static final String pocket_pattern = "pocket_pattern";
	private final int defaultdistance = 5;
	private SharedPreferences mSharedPrefs;
	private ToolboxService mtoolboxservice;
	private ServiceConnection sc = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mtoolboxservice = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mtoolboxservice = ((ToolboxService.ToolBoxServiceBinder) (service))
					.getService();
		}

	};

	public PocketDetectorService() {
		// TODO Auto-generated constructor stub
	}

	private void registerProximitySensor() {
		SensorManager localSensorManager = this.mSensorMgr;
		Sensor localSensor = this.mSensorMgr
				.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		localSensorManager.registerListener(this, localSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		this.mIsRegistered = true;
	}

	private void unregisterProximitySensor() {
		this.mSensorMgr.unregisterListener(this);
		this.mIsRegistered = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		List<Action> mEnabledActions = Action.getEnabledActions(this);
		if (mEnabledActions.isEmpty()) {
			stopSelf();
			return START_STICKY;
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.mIsPocket = mSharedPrefs.getBoolean(pocket_pattern, true);
		this.bindService();
		if ((!this.mIsRegistered) || (this.mSensorMgr == null)) {
			SensorManager localSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			this.mSensorMgr = localSensorManager;
			registerProximitySensor();
		}
		super.onCreate();
	}

	private void bindService() {
		Intent intent = new Intent(PocketDetectorService.this,
				ToolboxService.class);
		bindService(intent, sc, Context.BIND_AUTO_CREATE);
	}

	private void unBind() {
		if (sc != null) {
			unbindService(sc);
		}
	}

	@Override
	public void onDestroy() {
		unregisterProximitySensor();
		this.unBind();
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			float[] arrayOfFloat = event.values;
			float j = arrayOfFloat[0];
			if (this.mIsPocket) {
				if (j == 0 || j < this.defaultdistance) {
					if (mtoolboxservice != null) {
						this.mtoolboxservice.stopShakeAccelerometer();
					}
				} else {
					if (mtoolboxservice != null) {
						this.mtoolboxservice.startShakeAccelerometer();
					}
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
