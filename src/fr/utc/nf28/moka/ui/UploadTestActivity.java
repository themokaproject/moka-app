package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import fr.utc.nf28.moka.R;

public class UploadTestActivity extends SherlockActivity {

	private Button mUpload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_test_activity);

		mUpload = (Button)findViewById(R.id.upload);
		mUpload.setEnabled(false);

	}
}
