package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.utc.nf28.moka.data.ConfigParcelable;
import fr.utc.nf28.moka.util.LogUtils;

public class DeviceConfigurationActivity extends Activity {

	/**
	 * Log for Logcat
	 */
	private static final String TAG = LogUtils.makeLogTag(DeviceConfigurationActivity.class);

	/**
	 * tag for EXTRA in intent
	 */
	public static final String EXTRA_CONFIG = "configurationFromNfc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();

		if(i!=null & i.hasExtra(EXTRA_CONFIG)){
			ConfigParcelable config = (ConfigParcelable) i.getParcelableExtra(EXTRA_CONFIG);
			Log.i(TAG, "ssid from extra"+config.getSSID());
			Log.i(TAG, "pwd from extra"+config.getPWD());
			Log.i(TAG, "ip from extra"+config.getIpAgent());
			Log.i(TAG, "port from extra"+config.getPortAgent());
		}


	}
}
