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
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class SettingsActivity extends Activity {
	/**
	 * Tag for log cat
	 */
	private static final String TAG = makeLogTag(SettingsActivity.class);
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
		setContentView(R.layout.settings_activity);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

		//display fragment as main content
		getFragmentManager().beginTransaction().add(R.id.settings, new ConfigurationFragment()).commit();

		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
			findViewById(R.id.write).setClickable(false);
		} else {
			findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					enableNfcDiscovering();
					Crouton.makeText(SettingsActivity.this
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
					, TAG_MOKA_SCHEME + mPrefs.getString(SharedPreferencesUtils.KEY_PREF_SSID, "") + ","
					+ mPrefs.getString(SharedPreferencesUtils.KEY_PREF_PWD, "") + ","
					+ mPrefs.getString(SharedPreferencesUtils.KEY_PREF_IP, "") + ","
					+ mPrefs.getString(SharedPreferencesUtils.KEY_PREF_PORT, "")
					, false);
		}
	}
}

