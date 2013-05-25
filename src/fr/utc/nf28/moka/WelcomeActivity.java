package fr.utc.nf28.moka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;

import fr.utc.nf28.moka.ui.nfc.NfcActivity;

public class WelcomeActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);

		findViewById(R.id.welcome_start_moka).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(WelcomeActivity.this, NfcActivity.class));
			}
		});
	}
}
