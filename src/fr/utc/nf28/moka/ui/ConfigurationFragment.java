package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import fr.utc.nf28.moka.R;

public class ConfigurationFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//load preferences from an XML ressource here
		addPreferencesFromResource(R.xml.configuration_preferences);
	}
}
