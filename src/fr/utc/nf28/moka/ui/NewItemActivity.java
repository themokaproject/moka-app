package fr.utc.nf28.moka.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ComputerType;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;
import fr.utc.nf28.moka.io.receiver.CreationReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemActivity extends MokaUpActivity implements CreationReceiver.OnCreationCallbackListener, EditItemFragment.Callbacks {
	public static final String ARG_TYPE = "arg_type";
	private static final String TAG = makeLogTag(NewItemActivity.class);
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
	/**
	 * broadcast receiver use to catch agent callback
	 */
	private CreationReceiver mJadeServerReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_item_activity);

		//create new receiver
		mJadeServerReceiver = new CreationReceiver(this);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.new_item_actionbar_title));

		final Intent intent = getIntent();
		if (savedInstanceState == null && intent.hasExtra(ARG_TYPE)) {
			//retrieve MokaType from intent
			final MokaType type = intent.getExtras().getParcelable(ARG_TYPE);
			if (type == null) {
				throw new IllegalStateException("Selected type cannot be null");
			}
			final String typeName;
			if (type instanceof ComputerType.UmlType) {
				typeName = ComputerType.UmlType.KEY_TYPE;
			} else if (type instanceof TextType.PostItType) {
				typeName = TextType.PostItType.KEY_TYPE;
			} else if (type instanceof MediaType.ImageType) {
				typeName = MediaType.ImageType.KEY_TYPE;
			} else if (type instanceof MediaType.VideoType) {
				typeName = MediaType.VideoType.KEY_TYPE;
			} else {
				final Crouton crouton = Crouton.makeText(this,
						getResources().getString(R.string.item_not_supported_yet), Style.ALERT);
				crouton.setLifecycleCallback(new LifecycleCallback() {
					@Override
					public void onDisplayed() {
						findViewById(R.id.progress).setVisibility(View.GONE);
					}

					@Override
					public void onRemoved() {
					}
				});
				crouton.show();
				return;
			}

			//send creation request to the SMA
			JadeUtils.getAndroidAgentInterface().createItem(typeName);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mJadeServerReceiver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mJadeServerReceiver);
	}

	@Override
	public void onSuccess(MokaItem newItem) {
		Crouton.makeText(this, "id from server :" + String.valueOf(newItem.getId()) + ". Ready for editing.",
				Style.CONFIRM).show();
		findViewById(R.id.progress).setVisibility(View.GONE);
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slow_fade_in, R.anim.slow_fade_out)
				.replace(android.R.id.content, EditItemFragment.newInstance(newItem))
				.commit();
	}

	@Override
	public void onError() {
		Crouton.makeText(this, getResources().getString(R.string.network_error), Style.ALERT).show();
		findViewById(R.id.progress).setVisibility(View.GONE);
	}

	@Override
	public void onItemDeletion(MokaItem item) {
		JadeUtils.getAndroidAgentInterface().deleteItem(item.getId());
		setResult(EditItemActivity.RESULT_DELETE);
		finish();
	}
}
