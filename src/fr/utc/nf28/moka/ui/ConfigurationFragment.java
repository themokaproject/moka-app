package fr.utc.nf28.moka.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import fr.utc.nf28.moka.R;

// TODO: make this class Gingerbread & Froyo compliant, or use PreferenceActivity
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ConfigurationFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//load preferences from an XML ressource here
		addPreferencesFromResource(R.xml.configuration_preferences);
	}
}
