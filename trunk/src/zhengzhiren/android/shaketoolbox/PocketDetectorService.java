package zhengzhiren.android.shaketoolbox;


import java.util.List;
import zhengzhiren.android.shaketoolbox.actions.Action;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.Toast;

public class PocketDetectorService extends Service implements SensorEventListener {

	  private boolean mIsRegistered=false;
	  private SensorManager mSensorMgr;
	public PocketDetectorService() {
		// TODO Auto-generated constructor stub
	}
	private void registerProximitySensor()
	  {
	    SensorManager localSensorManager = this.mSensorMgr;
	    Sensor localSensor = this.mSensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    localSensorManager.registerListener(this, localSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    this.mIsRegistered = true;
	  }
	private void unregisterProximitySensor()
	  {
	    this.mSensorMgr.unregisterListener(this);
	    this.mIsRegistered = false;
	  }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		List<Action> mEnabledActions= Action.getEnabledActions(this);
		if (mEnabledActions.isEmpty()) {
			stopSelf();
			return START_STICKY;
		}
		return START_STICKY;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if ((!this.mIsRegistered) || (this.mSensorMgr == null))
	    {
	      SensorManager localSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	      this.mSensorMgr = localSensorManager;
	      registerProximitySensor();
	      //this.mDelayedStartHandler.removeCallbacksAndMessages(null);
	    }
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterProximitySensor();
		super.onDestroy();
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
	        if (arg0.sensor.getType() == Sensor.TYPE_PROXIMITY)
	        {
	          float[] arrayOfFloat = arg0.values;
	          float j = arrayOfFloat[0];
	          //filelog f=new filelog("pocketlog.txt");
	          //Time t=new Time();
	          //t.setToNow();
	  		  //String log="pocket:"+String.valueOf(t.hour)+"/"+String.valueOf(t.minute)+"/"+String.valueOf(t.second)+"\n";
	  		  //log+=String.valueOf(j)+"\n";
	  		  //f.writelog(log);
	        }
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
