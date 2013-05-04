package fr.utc.nf28.moka.ui.nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fr.utc.nf28.moka.MainActivity;
import fr.utc.nf28.moka.R;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NfcActivity extends Activity {
	private static final String TAG = makeLogTag(NfcActivity.class);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_activity);

		findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(NfcActivity.this, MainActivity.class));
				finish();
			}
		});
	}
}