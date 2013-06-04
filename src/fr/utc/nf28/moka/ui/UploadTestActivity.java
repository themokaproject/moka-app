package fr.utc.nf28.moka.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.receiver.CreationReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.util.JadeUtils;

public class UploadTestActivity extends SherlockActivity implements CreationReceiver.OnCreationCallbackListener {

	private Button mUpload;
	private CreationReceiver mJadeCreationReceiver;
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_test_activity);

		mUpload = (Button) findViewById(R.id.upload);
		mUpload.setEnabled(false);

		mJadeCreationReceiver = new CreationReceiver(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mJadeCreationReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mJadeCreationReceiver, mIntentFilter);
		//send creation request to the SMA
		JadeUtils.getAndroidAgentInterface().createItem(MediaType.ImageType.KEY_TYPE);
	}

	@Override
	public void onSuccess(MokaItem newItem) {
		Crouton.makeText(this, "id from server :" + String.valueOf(newItem.getId()) + ". Ready for editing.",
				Style.CONFIRM).show();
		mUpload.setEnabled(true);
		findViewById(R.id.progressBar).setVisibility(View.GONE);
	}

	@Override
	public void onError() {

	}
}
