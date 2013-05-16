package fr.utc.nf28.moka;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

import fr.utc.nf28.moka.agent.AndroidAgent;
import fr.utc.nf28.moka.util.LogUtils;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.Profile;
import jade.util.leap.Properties;

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

	/**
	 * JADE parameters from tag
	 */
	private String mMainContainerIp;
	private String mMainContainerPort;

	/**
	 * layout component
	 */
	private ProgressBar mProgressConnexion;
	private ProgressBar mProgressIp;
	private ProgressBar mProgressContainer;
	private ProgressBar mProgressAgent;
	private CheckBox mCheckConnexion;
	private CheckBox mCheckIp;
	private CheckBox mCheckContainer;
	private CheckBox mCheckAgent;

	/**
	 * JADE
	 */
	private MicroRuntimeServiceBinder mMicroRuntimeServiceBinder;
	private Properties mAgentContainerProperties;
	private ServiceConnection mServiceConnection;
	private static final String ANDROID_AGENT_NICKNAME = "AndroidAgent_" + UUID.randomUUID().toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_configuration_activity);

		Intent i = getIntent();

		if (i != null && i.hasExtra(EXTRA_SSID) && i.hasExtra(EXTRA_PWD) && i.hasExtra(EXTRA_IP) && i.hasExtra(EXTRA_PORT)) {
			mSSID = i.getStringExtra(EXTRA_SSID);
			mPWD = i.getStringExtra(EXTRA_PWD);
			mMainContainerIp = i.getStringExtra(EXTRA_IP);
			mMainContainerPort = i.getStringExtra(EXTRA_PORT);
		}

		//get layout element
		mProgressConnexion = (ProgressBar) findViewById(R.id.progressConnexion);
		mProgressIp = (ProgressBar) findViewById(R.id.progressOptenirIp);
		mProgressContainer = (ProgressBar) findViewById(R.id.progressContainer);
		mProgressAgent = (ProgressBar) findViewById(R.id.progressAgent);
		mCheckConnexion = (CheckBox) findViewById(R.id.checkConnexion);
		mCheckIp = (CheckBox) findViewById(R.id.checkOptenirIp);
		mCheckContainer = (CheckBox) findViewById(R.id.checkContainer);
		mCheckAgent = (CheckBox) findViewById(R.id.checkAgent);

		mAgentContainerProperties = new Properties();
		mAgentContainerProperties.setProperty(Profile.JVM, Profile.ANDROID);

		Log.i(TAG, "activity start with ssid = " + mSSID + " and pwd = " + mPWD);
		enableWifi();
	}

	/**
	 * enable wifi if not and register to wifi and network lister
	 */
	private void enableWifi() {
		mProgressConnexion.setVisibility(View.VISIBLE);

		//Register receiver
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		myIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(MokaWifiStateChangedReceiver, myIntentFilter);

		//enable wifi cause know we can receive broadcast
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);

		findViewById(R.id.progressConnexion).setVisibility(View.VISIBLE);
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
					mProgressConnexion.setVisibility(View.INVISIBLE);
					mCheckConnexion.setVisibility(View.VISIBLE);
					mProgressIp.setVisibility(View.VISIBLE);
					//TODO remove after dev-period. Choose WPA2 or WEP
					configureWifiWPA2();
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
					mProgressIp.setVisibility(View.INVISIBLE);
					mCheckIp.setVisibility(View.VISIBLE);
					mProgressContainer.setVisibility(View.VISIBLE);
					Log.i(TAG, "NetworkInfo.State.CONNECTED");
					//TODO start JADE container
					startJadePlatform(mMainContainerIp, Integer.valueOf(mMainContainerPort));
				} else {
					Log.i(TAG, info.getState().toString());
				}
			}
		}
	};

	/**
	 * use to configure wireless WPA2 connection from code
	 */
	private void configureWifiWPA2() {
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

	/**
	 * use to configure wireless WEP connection from code
	 */
	private void configureWifiWEP() {
		Log.i(TAG, "configureWifi");
		WifiConfiguration mWifiConfig = new WifiConfiguration();
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
		int netId = mWifiManager.addNetwork(mWifiConfig);
		Log.i(TAG, "addNetWork return code : " + String.valueOf(netId));
		boolean b = mWifiManager.enableNetwork(netId, true);
		Log.i(TAG, "enableNetwork return code : " + String.valueOf(b));

	}

	private void launchMainActivity() {
		startActivity(new Intent(DeviceConfigurationActivity.this, MainActivity.class));
		unbindService(mServiceConnection);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//TODO need to check, Activity can be pause before Receiver registering
		unregisterReceiver(MokaWifiStateChangedReceiver);
	}

	/**
	 * Start agent plateform
	 *
	 * @param host adress ip of mainContainer Machine
	 * @param port port to reach mainContainer Machine
	 */
	public void startJadePlatform(final String host, final int port) {
		Log.i(TAG, "start jade platform");
		mAgentContainerProperties.setProperty(Profile.MAIN_HOST, host);
		mAgentContainerProperties.setProperty(Profile.MAIN_PORT, String.valueOf(port));
		bindMicroRuntimeService();
	}

	/**
	 * JadeAndroid good practices for jade runtime
	 */
	private void bindMicroRuntimeService() {
		Log.i(TAG, "bind micro runtime");
		mServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className, IBinder service) {
				// Bind successful
				Log.i(TAG, "bind micro runtime success");
				mMicroRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
				startAgentContainer();
			}

			public void onServiceDisconnected(ComponentName className) {
				// Bind unsuccessful
				Log.i(TAG, "bind micro runtime fail");
				mMicroRuntimeServiceBinder = null;
			}
		};

		bindService(new Intent(getApplicationContext(), MicroRuntimeService.class),
				mServiceConnection,
				Context.BIND_AUTO_CREATE);
	}


	/**
	 * start JADE Agent container
	 */
	private void startAgentContainer() {
		Log.i(TAG, "start agent container");
		mMicroRuntimeServiceBinder.startAgentContainer(mAgentContainerProperties,
				new RuntimeCallback<Void>() {
					@Override
					public void onSuccess(Void thisIsNull) {
						Log.i(TAG, "start agent container success");
						// Split container successfully started
						DeviceConfigurationActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mProgressContainer.setVisibility(View.INVISIBLE);
								mCheckContainer.setVisibility(View.VISIBLE);
								mProgressAgent.setVisibility(View.VISIBLE);
							}
						});
						startAgent(ANDROID_AGENT_NICKNAME, AndroidAgent.class.getName(), null);
					}

					@Override
					public void onFailure(Throwable throwable) {
						Log.i(TAG, "start agent container fail");
						// Split container startup error
					}
				});
	}

	/**
	 * start a JADE Agent
	 *
	 * @param nickName  agent name, must be unique
	 * @param className agent class
	 * @param params    params which can be retrieved by the agent in setup()
	 */
	private void startAgent(final String nickName, final String className, Object[] params) {
		Log.i(TAG, "start agent " + nickName);
		mMicroRuntimeServiceBinder.startAgent(nickName, className, params,
				new RuntimeCallback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						//Agent successfully started
						Log.i(TAG, "start agent " + nickName + " success");
						//TODO change activity unbind the service which cause jade platform death
						DeviceConfigurationActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mProgressAgent.setVisibility(View.INVISIBLE);
								mCheckAgent.setVisibility(View.VISIBLE);
							}
						});
						//launchMainActivity();
					}

					@Override
					public void onFailure(Throwable throwable) {
						//Agent startup error
						Log.e(TAG, "start agent " + nickName + " fail", throwable);
					}
				});
	}
}
