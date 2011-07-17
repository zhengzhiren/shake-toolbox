package zhengzhiren.android.shaketoolbox;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 首选项
 * 
 * @author ZZR
 * 
 */
public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.activity_title_preferences);
		addPreferencesFromResource(R.xml.preferences);
	}
}
