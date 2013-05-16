package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
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

	/**
	 * wireless configuration from tag in extra
	 */
	private String mSSID;
	private String mPWD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_configuration_activity);

		Intent i = getIntent();

		if (i != null && i.hasExtra(EXTRA_SSID) && i.hasExtra(EXTRA_PWD) && i.hasExtra(EXTRA_IP) && i.hasExtra(EXTRA_PORT)) {
			mSSID = i.getStringExtra(EXTRA_SSID);
			mPWD = i.getStringExtra(EXTRA_PWD);
			((TextView) findViewById(R.id.textSSID)).setText(mSSID);
			((TextView) findViewById(R.id.textPWD)).setText(mPWD);
			((TextView) findViewById(R.id.textIP)).setText(i.getStringExtra(EXTRA_IP));
			((TextView) findViewById(R.id.textPort)).setText(i.getStringExtra(EXTRA_PORT));
		}
		Log.i(TAG, "activity start with ssid = " + mSSID + " and pwd = " + mPWD);
		enableWifi();

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

	/**
	 * Use to receive broadcast from wifi and network
	 */
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
					Log.i(TAG, "WIFI_STATE_ENABLED");
					configureWifi();
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					//wifi Enabling
					Log.i(TAG, "WIFI_STATE_ENABLING");
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
			}

			final String action = intent.getAction();
			if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					Log.i(TAG, "NetworkInfo.State.CONNECTED");
					launchMainActivity();
				} else {
					Log.i(TAG, info.getState().toString());
				}
			}
		}
	};

	/**
	 * use to configure wireless connection from code
	 */
	private void configureWifi() {
		Log.i(TAG, "configureWifi");
		WifiConfiguration mWifiConfig = new WifiConfiguration();
		mWifiConfig.SSID = "\"" + mSSID + "\"";
		mWifiConfig.priority = 40;
		mWifiConfig.preSharedKey = "\"" + mPWD + "\"";
		mWifiConfig.status = WifiConfiguration.Status.ENABLED;
		mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
		mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		int netId = mWifiManager.addNetwork(mWifiConfig);
		Log.i(TAG, "addNetWork return code : " + String.valueOf(netId));
		boolean b = mWifiManager.enableNetwork(netId, true);
		Log.i(TAG, "enableNetwork return code : " + String.valueOf(b));

	}

	private void launchMainActivity() {
		startActivity(new Intent(DeviceConfigurationActivity.this, MainActivity.class));
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(MokaWifiStateChangedReceiver);
	}
}
