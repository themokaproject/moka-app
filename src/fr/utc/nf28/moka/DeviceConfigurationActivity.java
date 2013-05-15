package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
	 * port tag for EXTRA in	 intent
	 */
	public static final String EXTRA_PORT = "portFromNfc";

	/**
	 * WifiManager to manage network
	 */
	private WifiManager mWifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_configuration_activity);

		Intent i = getIntent();

		if (i != null && i.hasExtra(EXTRA_SSID) && i.hasExtra(EXTRA_PWD) && i.hasExtra(EXTRA_IP) && i.hasExtra(EXTRA_PORT)) {
			((TextView) findViewById(R.id.textSSID)).setText(i.getStringExtra(EXTRA_SSID));
			((TextView) findViewById(R.id.textPWD)).setText(i.getStringExtra(EXTRA_PWD));
			((TextView) findViewById(R.id.textIP)).setText(i.getStringExtra(EXTRA_IP));
			((TextView) findViewById(R.id.textPort)).setText(i.getStringExtra(EXTRA_PORT));
		}


	}

	/**
	 * enable wifi if not and register to wifi and network lister
	 */
	private void enableWifi() {
		//Register receiver
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		myIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(MokaWifiStateChangedReceiver, myIntentFilter);

		//enable wifi cause know we can receive broadcast
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);
	}

	private BroadcastReceiver MokaWifiStateChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_UNKNOWN);

			switch (extraWifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
					//wifi Disabled
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					//wifi Disabling
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					//wifi Enable
					//TODO configure Wifi here
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					//wifi Enabling
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
			}

			final String action = intent.getAction();
			if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					//TODO start activity caus know, this device is connected
				}
			}
		}
	};

}
