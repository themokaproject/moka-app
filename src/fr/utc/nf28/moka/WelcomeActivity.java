package fr.utc.nf28.moka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import fr.utc.nf28.moka.ui.nfc.NfcActivity;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

public class WelcomeActivity extends SherlockActivity {

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		findViewById(R.id.welcome_start_moka).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPrefs.edit().putString(SharedPreferencesUtils.KEY_PREF_FIRST_NAME,
						((EditText)findViewById(R.id.welcome_first_name)).getText().toString()).commit();
				startActivity(new Intent(WelcomeActivity.this, NfcActivity.class));
			}
		});
	}
}
