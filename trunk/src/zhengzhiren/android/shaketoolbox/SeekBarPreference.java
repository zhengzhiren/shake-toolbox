package zhengzhiren.android.shaketoolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author wjx
 * 
 */
public class SeekBarPreference extends DialogPreference implements
		OnSeekBarChangeListener {
	private SeekBar mSeekBar;
	private TextView mTextView;
	public final static String PREF_SHAKE_SENSITIVITY = "shake_sensitivity";
	public final static int DEFAULT_VALUE = 5;
	public final static int MAX_VALUE = 10;
	private SharedPreferences mSharedPrefs;

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public static int getpre_ShakeThreshold_on_shake_value(int srcthreshold) {
		return (srcthreshold + 1) * 500;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		mSharedPrefs = this.getSharedPreferences();
		int seekvalue = mSharedPrefs.getInt(PREF_SHAKE_SENSITIVITY,
				DEFAULT_VALUE);
		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
		mTextView = (TextView) view.findViewById(R.id.textView);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setProgress(seekvalue);
		mTextView.setText(seekvalue + "/" + mSeekBar.getMax());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			SharedPreferences.Editor editor = mSharedPrefs.edit();
			editor.putInt(PREF_SHAKE_SENSITIVITY, mSeekBar.getProgress());
			editor.commit();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mTextView.setText(progress + "/" + seekBar.getMax());
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
