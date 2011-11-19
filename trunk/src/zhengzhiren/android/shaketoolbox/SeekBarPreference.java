package zhengzhiren.android.shaketoolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements
		OnSeekBarChangeListener {

	private SeekBar mseekBar;
	private TextView mtextView;
	public final static String pre_ShakeThreshold_on_shake_value = "ShakeThreshold_on_shake_value";
	public final static int defaultvalue = 5;
	public final static int maxvalue = 10;
	private int seekvalue;
	private SharedPreferences mSharedPrefs;

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public static int Getpre_ShakeThreshold_on_shake_value(int srcthreshold) {
		return (srcthreshold + 1) * 500;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		mSharedPrefs = this.getSharedPreferences();
		seekvalue = mSharedPrefs.getInt(pre_ShakeThreshold_on_shake_value,
				defaultvalue);
		mseekBar = (SeekBar) view.findViewById(R.id.seekBar);
		mtextView = (TextView) view.findViewById(R.id.textView);
		mseekBar.setOnSeekBarChangeListener(this);
		this.mseekBar.setProgress(this.seekvalue);
		mtextView.setText(seekvalue + "/" + mseekBar.getMax());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			SharedPreferences.Editor editor = mSharedPrefs.edit();
			editor.putInt(pre_ShakeThreshold_on_shake_value,
					mseekBar.getProgress());
			editor.commit();
		} else {
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mtextView.setText(progress + "/" + seekBar.getMax());
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
