package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import fr.utc.nf28.moka.MokaApplication;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.io.agent.AndroidAgent;
import fr.utc.nf28.moka.util.ConnectionUtils;
import fr.utc.nf28.moka.util.JadeUtils;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;
import jade.android.RuntimeCallback;

import static fr.utc.nf28.moka.util.LogUtils.LOGE;
import static fr.utc.nf28.moka.util.LogUtils.LOGI;
import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class DeviceConfigurationActivity extends Activity {
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
	 * Log for Logcat
	 */
	private static final String TAG = makeLogTag(DeviceConfigurationActivity.class);
	private final IntentFilter mIntentFilter = new IntentFilter();
	/**
	 * Use to receive broadcast from wifi and network
	 */
	private final BroadcastReceiver mMokaWifiStateChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
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
					LOGI(TAG, "WIFI_STATE_ENABLED");
					mProgressConnection.setVisibility(View.GONE);
					mCheckConnexion.setVisibility(View.VISIBLE);
					mProgressIp.setVisibility(View.VISIBLE);
					//TODO remove after dev-period.
					if (ConnectionUtils.isOnline(DeviceConfigurationActivity.this)) {
						((MokaApplication) getApplication()).startJadePlatform(mMainContainerIp,
								Integer.valueOf(mMainContainerPort),
								mContainerCallback);
					} else {
						//TODO remove after dev-period. Choose WPA2 or WEP
						configureWifiWPA2();
					}
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					//wifi Enabling
					LOGI(TAG, "WIFI_STATE_ENABLING");
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					break;
			}

			final String action = intent.getAction();
			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
				final NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info != null) {
					if (NetworkInfo.State.CONNECTED.equals(info.getState())) {
						mProgressIp.setVisibility(View.GONE);
						mCheckIp.setVisibility(View.VISIBLE);
						mProgressContainer.setVisibility(View.VISIBLE);
						LOGI(TAG, "NetworkInfo.State.CONNECTED");
						((MokaApplication) getApplication()).startJadePlatform(mMainContainerIp,
								Integer.valueOf(mMainContainerPort),
								mContainerCallback);
					} else {
						LOGI(TAG, info.getState().toString());
					}
				}
			}
		}
	};
	/**
	 * WifiManager to manage network
	 */
	private WifiManager mWifiManager;
	/**
	 * wireless configuration from tag in extra
	 */
	private String mSSID;
	private String mPWD;
	/**
	 * JADE parameters from tag
	 */
	private String mMainContainerIp;
	private String mMainContainerPort;
	/**
	 * Jade callback
	 */
	private RuntimeCallback<Void> mContainerCallback;
	private RuntimeCallback<Void> mAgentCallback;
	/**
	 * layout component
	 */
	private ProgressBar mProgressConnection;
	private ProgressBar mProgressIp;
	private ProgressBar mProgressContainer;
	private ProgressBar mProgressAgent;
	private CheckBox mCheckConnexion;
	private CheckBox mCheckIp;
	private CheckBox mCheckContainer;
	private CheckBox mCheckAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_configuration_activity);

		final Intent i = getIntent();
		if (i != null && i.hasExtra(EXTRA_SSID) && i.hasExtra(EXTRA_PWD) && i.hasExtra(EXTRA_IP) && i.hasExtra(EXTRA_PORT)) {
			mSSID = i.getStringExtra(EXTRA_SSID);
			mPWD = i.getStringExtra(EXTRA_PWD);
			mMainContainerIp = i.getStringExtra(EXTRA_IP);
			// We real the real IP address for later reuse
			final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putString(SharedPreferencesUtils.KEY_SERVER_IP, mMainContainerIp);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				editor.apply();
			} else {
				editor.commit();
			}
			mMainContainerPort = i.getStringExtra(EXTRA_PORT);
		}

		//get layout element
		mProgressConnection = (ProgressBar) findViewById(R.id.progressConnexion);
		mProgressIp = (ProgressBar) findViewById(R.id.progressOptenirIp);
		mProgressContainer = (ProgressBar) findViewById(R.id.progressContainer);
		mProgressAgent = (ProgressBar) findViewById(R.id.progressAgent);
		mCheckConnexion = (CheckBox) findViewById(R.id.checkConnexion);
		mCheckIp = (CheckBox) findViewById(R.id.checkOptenirIp);
		mCheckContainer = (CheckBox) findViewById(R.id.checkContainer);
		mCheckAgent = (CheckBox) findViewById(R.id.checkAgent);

		//implement callback for Jade container creation
		mContainerCallback = new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void thisIsNull) {
				LOGI(TAG, "start agent container success");
				// Split container successfully started
				DeviceConfigurationActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressContainer.setVisibility(View.GONE);
						mCheckContainer.setVisibility(View.VISIBLE);
						mProgressAgent.setVisibility(View.VISIBLE);
					}
				});
				((MokaApplication) getApplication()).startAgent(JadeUtils.ANDROID_AGENT_NICKNAME,
						AndroidAgent.class.getName(), new Object[]{getApplicationContext()}, mAgentCallback);
			}

			@Override
			public void onFailure(Throwable throwable) {
				LOGI(TAG, "start agent container fail");
				// Split container startup error
			}
		};

		//implement callback for Jade container creation
		mAgentCallback = new

				RuntimeCallback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						//Agent successfully started
						LOGI(TAG, "start agent " + JadeUtils.ANDROID_AGENT_NICKNAME + " success");
						DeviceConfigurationActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mProgressAgent.setVisibility(View.GONE);
								mCheckAgent.setVisibility(View.VISIBLE);
							}
						});
						launchMainActivity();
					}

					@Override
					public void onFailure(Throwable throwable) {
						//Agent startup error
						LOGE(TAG, "start agent " + JadeUtils.ANDROID_AGENT_NICKNAME + " fail", throwable);
					}
				};


		LOGI(TAG, "activity start with ssid = " + mSSID + " and pwd = " + mPWD);

		mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

		enableWifi();
	}

	/**
	 * enable wifi if not and register to wifi and network lister
	 */
	private void enableWifi() {
		//TODO first progress bar not visible
		mProgressConnection.setVisibility(View.VISIBLE);

		//enable wifi cause know we can receive broadcast
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);

		findViewById(R.id.progressConnexion).setVisibility(View.GONE);
	}

	/**
	 * use to configure wireless WPA2 connection from code
	 */
	private void configureWifiWPA2() {
		LOGI(TAG, "configureWifi");
		final WifiConfiguration mWifiConfig = new WifiConfiguration();
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
		final int netId = mWifiManager.addNetwork(mWifiConfig);
		LOGI(TAG, "addNetWork return code : " + String.valueOf(netId));
		final boolean b = mWifiManager.enableNetwork(netId, true);
		LOGI(TAG, "enableNetwork return code : " + String.valueOf(b));
	}

	/**
	 * use to configure wireless WEP connection from code
	 */
	private void configureWifiWEP() {
		LOGI(TAG, "configureWifi");
		final WifiConfiguration mWifiConfig = new WifiConfiguration();
		mWifiConfig.SSID = "\"" + mSSID + "\"";
		mWifiConfig.priority = 40;
		mWifiConfig.status = WifiConfiguration.Status.ENABLED;
		mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		mWifiConfig.wepKeys[0] = "\"" + mPWD + "\"";
		mWifiConfig.wepTxKeyIndex = 0;
		final int netId = mWifiManager.addNetwork(mWifiConfig);
		LOGI(TAG, "addNetWork return code : " + String.valueOf(netId));
		final boolean b = mWifiManager.enableNetwork(netId, true);
		LOGI(TAG, "enableNetwork return code : " + String.valueOf(b));
	}

	private void launchMainActivity() {
		final Intent mainIntent = new Intent(this, MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mainIntent);
	}

	@Override
	protected void onPause() {
		super.onPause();

		unregisterReceiver(mMokaWifiStateChangedReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();

		//Register receiver
		registerReceiver(mMokaWifiStateChangedReceiver, mIntentFilter);
	}
}
