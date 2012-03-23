package zhengzhiren.android.shaketoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * 首选项
 * 
 * @author ZZR
 * 
 */
public class Preferences extends PreferenceActivity {
	private static final String PRE_START_APP_KEY = "start_app";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.activity_title_preferences);
		addPreferencesFromResource(R.xml.preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onPreferenceTreeClick(android.
	 * preference.PreferenceScreen, android.preference.Preference)
	 */
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(PRE_START_APP_KEY)) {
			Intent intent = new Intent(this, StartAppPreference.class);
			startActivity(intent);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
