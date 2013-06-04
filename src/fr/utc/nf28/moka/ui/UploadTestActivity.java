package fr.utc.nf28.moka.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import java.io.File;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.receiver.CreationReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class UploadTestActivity extends SherlockActivity implements CreationReceiver.OnCreationCallbackListener {

	private Button mUpload;
	private CreationReceiver mJadeCreationReceiver;
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);

	//Capture stuff
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private static final String TEMP_IMAGE_NAME = "uploadTemp.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_test_activity);

		mUpload = (Button) findViewById(R.id.upload);
		mUpload.setEnabled(false);
		mUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = UploadTestActivity.this.getOutputMediaFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});

		mJadeCreationReceiver = new CreationReceiver(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Crouton.makeText(this, "image saved",
						Style.CONFIRM).show();

			} else if (resultCode == RESULT_CANCELED) {
				Crouton.makeText(this, "image capture canceled",
						CroutonUtils.INFO_MOKA_STYLE).show();
			} else {
				Crouton.makeText(this, "image saving failed",
						Style.ALERT).show();
			}
		}
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

	/**
	 * Create a file Uri for saving our temp image
	 */
	private static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/**
	 * Create a File for saving our temp image
	 */
	private static File getOutputMediaFile() {

		final File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "Moka");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Moka", "failed to create directory");
				return null;
			}
		}

		// Create a temp image file name
		final File mediaFile = new File(mediaStorageDir.getPath() + File.separator + TEMP_IMAGE_NAME);
		return mediaFile;
	}
}
