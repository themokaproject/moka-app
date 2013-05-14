package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.utc.nf28.moka.util.LogUtils;

public class DeviceConfigurationActivity extends Activity {

	/**
	 * Log for Logcat
	 */
	private static final String TAG = LogUtils.makeLogTag(DeviceConfigurationActivity.class);

	/**
	 * ssid tag for EXTRA in intent
	 */
	public static final String EXTRA_SSID = "ssidFromNfc";
	/**
	 * pwd tag for EXTRA in intent
	 */
	public static final String EXTRA_PWD = "pwdFromNfc";
	/**
	 * ip tag for EXTRA in intent
	 */
	public static final String EXTRA_IP = "ipFromNfc";
	/**
	 * port tag for EXTRA in intent
	 */
	public static final String EXTRA_PORT= "portFromNfc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();

		if(i!=null && i.hasExtra(EXTRA_SSID) && i.hasExtra(EXTRA_PWD) && i.hasExtra(EXTRA_IP) && i.hasExtra(EXTRA_PORT)){
			Log.i(TAG,"ssid from tag :"+i.getStringExtra(EXTRA_SSID));
			Log.i(TAG,"pwd from tag :"+i.getStringExtra(EXTRA_PWD));
			Log.i(TAG,"ip from tag :"+i.getStringExtra(EXTRA_IP));
			Log.i(TAG, "port from tag :" + i.getStringExtra(EXTRA_PORT));
		}


	}
}
