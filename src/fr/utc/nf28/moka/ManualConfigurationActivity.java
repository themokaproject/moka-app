package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import fr.utc.nf28.moka.ui.ConfigurationFragment;
import fr.utc.nf28.moka.util.LogUtils;

public class ManualConfigurationActivity extends Activity {
	/**
	 * Tag for log cat
	 */
	private static final String TAG = LogUtils.makeLogTag(ManualConfigurationActivity.class);

	/**
	 * key for connection configs in sharedPreferences
	 */
	public static final String KEY_PREF_SSID = "preference_wifi_ssid";
	public static final String KEY_PREF_PWD = "preference_wifi_password";
	public static final String KEY_PREF_IP = "preference_jade_ip";
	public static final String KEY_PREF_PORT = "preference_jade_port";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_configuration_activity);

		//display fragment as main content
		getFragmentManager().beginTransaction().add(R.id.settings, new ConfigurationFragment()).commit();

		findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ManualConfigurationActivity.this);
				Intent i = new Intent(ManualConfigurationActivity.this, DeviceConfigurationActivity.class);
				i.putExtra(DeviceConfigurationActivity.EXTRA_SSID,sharedPref.getString(KEY_PREF_SSID,""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_PWD,sharedPref.getString(KEY_PREF_PWD,""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_IP,sharedPref.getString(KEY_PREF_IP,""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_PORT,sharedPref.getString(KEY_PREF_PORT,""));
				startActivity(i);
			}
		});
	}
}
