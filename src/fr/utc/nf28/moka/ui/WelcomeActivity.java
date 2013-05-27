package fr.utc.nf28.moka.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.ui.nfc.NfcActivity;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

public class WelcomeActivity extends SherlockActivity {

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		final EditText fName = (EditText) findViewById(R.id.welcome_first_name);
		final EditText lName = (EditText) findViewById(R.id.welcome_last_name);

		fName.setHint(R.string.first_name);
		lName.setHint(R.string.name);

		findViewById(R.id.welcome_start_moka).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString(SharedPreferencesUtils.KEY_PREF_LAST_NAME, lName.getText().toString());
				editor.putString(SharedPreferencesUtils.KEY_PREF_FIRST_NAME, fName.getText().toString());
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
					editor.apply();
				} else {
					editor.commit();
				}
				startActivity(new Intent(WelcomeActivity.this, NfcActivity.class));
				finish();
			}
		});
	}
}
