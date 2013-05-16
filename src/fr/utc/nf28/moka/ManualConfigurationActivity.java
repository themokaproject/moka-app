package fr.utc.nf28.moka;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import fr.utc.nf28.moka.ui.ConfigurationFragment;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.NfcUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ManualConfigurationActivity extends Activity {
	/**
	 * key for connection configs in sharedPreferences
	 */
	public static final String KEY_PREF_SSID = "preference_wifi_ssid";
	public static final String KEY_PREF_PWD = "preference_wifi_password";
	public static final String KEY_PREF_IP = "preference_jade_ip";
	public static final String KEY_PREF_PORT = "preference_jade_port";
	/**
	 * Tag for log cat
	 */
	private static final String TAG = makeLogTag(ManualConfigurationActivity.class);
	/**
	 * scheme for tag
	 */
	private static final String TAG_MOKA_SCHEME = "http://moka.fr/c/?";
	/**
	 * use to available nfc
	 */
	private NfcAdapter mNfcAdapter;
	private SharedPreferences mPrefs;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_configuration_activity);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(ManualConfigurationActivity.this);

		//display fragment as main content
		getFragmentManager().beginTransaction().add(R.id.settings, new ConfigurationFragment()).commit();

		findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Intent i = new Intent(ManualConfigurationActivity.this, DeviceConfigurationActivity.class);
				i.putExtra(DeviceConfigurationActivity.EXTRA_SSID, mPrefs.getString(KEY_PREF_SSID, ""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_PWD, mPrefs.getString(KEY_PREF_PWD, ""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_IP, mPrefs.getString(KEY_PREF_IP, ""));
				i.putExtra(DeviceConfigurationActivity.EXTRA_PORT, mPrefs.getString(KEY_PREF_PORT, ""));
				startActivity(i);
			}
		});

		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if (mNfcAdapter == null) {
			findViewById(R.id.write).setClickable(false);
		} else {
			findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					enableNfcDiscovering();
					Crouton.makeText(ManualConfigurationActivity.this
							, getResources().getString(R.string.touch_a_tag)
							, CroutonUtils.INFO_MOKA_STYLE).show();
				}
			});
		}
	}

	/**
	 * let the activity to claim priority on tag_discover event when
	 * the activity is displayed
	 */
	private void enableNfcDiscovering() {
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		final IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{tagDetected}, null);
	}

	/**
	 * restore nfc priority
	 */
	private void disableNfcDiscovering() {
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		disableNfcDiscovering();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
			NfcUtils.writeMokaTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
					, TAG_MOKA_SCHEME + mPrefs.getString(KEY_PREF_SSID, "") + ","
					+ mPrefs.getString(KEY_PREF_PWD, "") + ","
					+ mPrefs.getString(KEY_PREF_IP, "") + ","
					+ mPrefs.getString(KEY_PREF_PORT, "")
					, false);
		}
	}
}

