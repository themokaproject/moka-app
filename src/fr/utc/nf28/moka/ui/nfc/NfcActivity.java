package fr.utc.nf28.moka.ui.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import fr.utc.nf28.moka.DeviceConfigurationActivity;
import fr.utc.nf28.moka.MainActivity;
import fr.utc.nf28.moka.SettingsActivity;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.NfcUtils;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NfcActivity extends SherlockActivity {
	private static final String TAG = makeLogTag(NfcActivity.class);
	private NfcAdapter mNfcAdapter;
	private EditText mFirstName;
	private EditText mLastName;
	private SharedPreferences mPrefs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_activity);

		mFirstName = (EditText) findViewById(R.id.firstnamefield);
		mLastName = (EditText) findViewById(R.id.lastnamefield);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		mFirstName.setText(mPrefs.getString(SharedPreferencesUtils.KEY_PREF_FIRST_NAME, ""));
		mLastName.setText(mPrefs.getString(SharedPreferencesUtils.KEY_PREF_LAST_NAME, ""));

		findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(NfcActivity.this, MainActivity.class));
				finish();
			}
		});

		findViewById(R.id.manuel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final CharSequence firstName = mFirstName.getText();
				final CharSequence lastName = mLastName.getText();
				if (firstName != null && lastName != null) {
					final SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString(SharedPreferencesUtils.KEY_PREF_FIRST_NAME, firstName.toString());
					editor.putString(SharedPreferencesUtils.KEY_PREF_LAST_NAME, lastName.toString());
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
						editor.apply();
					} else {
						editor.commit();
					}
				}
				if(checkPreferences()){
					final Intent i = new Intent(NfcActivity.this, DeviceConfigurationActivity.class);
					i.putExtra(DeviceConfigurationActivity.EXTRA_SSID, mPrefs.getString(SharedPreferencesUtils.KEY_PREF_SSID, ""));
					i.putExtra(DeviceConfigurationActivity.EXTRA_PWD, mPrefs.getString(SharedPreferencesUtils.KEY_PREF_PWD, ""));
					i.putExtra(DeviceConfigurationActivity.EXTRA_IP, mPrefs.getString(SharedPreferencesUtils.KEY_PREF_IP, ""));
					i.putExtra(DeviceConfigurationActivity.EXTRA_PORT, mPrefs.getString(SharedPreferencesUtils.KEY_PREF_PORT, ""));
					startActivity(i);
				}
			}
		});

		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
			((TextView) findViewById(R.id.info)).setText(R.string.info_no_nfc_text);
		}
	}

	public boolean checkPreferences(){
		final String ssid = mPrefs.getString(SharedPreferencesUtils.KEY_PREF_SSID, "");
		if(ssid.equals("")){
			Crouton.makeText(this
					, getResources().getString(R.string.no_ssid)
					, CroutonUtils.INFO_MOKA_STYLE).show();
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_nfc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		enableNfcDiscovering();
		if (getIntent().hasExtra(NfcAdapter.EXTRA_TAG)) {
			processTag((Tag) getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG));
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
			processTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
		}
	}

	/**
	 * let the activity to claim priority on tag_discover event when
	 * the activity is displayed
	 */
	private void enableNfcDiscovering() {
		if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
			final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			final IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
			mNfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{tagDetected}, null);
		}
	}

	/**
	 * restore nfc priority
	 */
	private void disableNfcDiscovering() {
		if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}

	/**
	 * process tag which has launched this activity or which has been caught;
	 *
	 * @param tag tag from intent
	 */
	private void processTag(Tag tag) {
		final String result = NfcUtils.readTag(tag);
		final String query = Uri.parse(result).getQuery();
		Log.i(TAG, "query : " + String.valueOf(query));
		if (query != null) {
			final String[] tagsParams = query.split(",");
			if (tagsParams.length == 4) {
				final Intent i = new Intent(this, DeviceConfigurationActivity.class);
				i.putExtra(DeviceConfigurationActivity.EXTRA_SSID, tagsParams[0]);
				i.putExtra(DeviceConfigurationActivity.EXTRA_PWD, tagsParams[1]);
				i.putExtra(DeviceConfigurationActivity.EXTRA_IP, tagsParams[2]);
				i.putExtra(DeviceConfigurationActivity.EXTRA_PORT, tagsParams[3]);
				startActivity(i);
				finish();
			} else {
				Log.i(TAG, "Wrong tag");
			}
		}
	}
}