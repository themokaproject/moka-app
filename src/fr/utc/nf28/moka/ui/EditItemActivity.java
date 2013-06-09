package fr.utc.nf28.moka.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ProgressBar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.receiver.LockingReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemActivity extends MokaUpActivity implements EditItemFragment.Callbacks,
		LockingReceiver.OnLockingListener {
	public static final String ARG_ITEM = "arg_item";
	private static final String TAG = makeLogTag(EditItemActivity.class);
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
	private Button mButtonRetry;
	private MokaItem mSelectedItem;
	private ProgressBar mProgressBar;
	/**
	 * Broadcast receiver used to catch locking callback from SMA
	 */
	private LockingReceiver mLockingReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		mProgressBar = (ProgressBar) findViewById(R.id.progress);

		mLockingReceiver = new LockingReceiver(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final Intent intent = getIntent();
		if (intent.hasExtra(ARG_ITEM)) {
			mSelectedItem = intent.getExtras().getParcelable(ARG_ITEM);
			if (mSelectedItem == null) {
				throw new IllegalStateException("Selected item cannot be null");
			}

			//send locking request to the SMA
			JadeUtils.getAndroidAgentInterface().lockItem(mSelectedItem.getId());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mLockingReceiver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mLockingReceiver);
	}

	@Override
	public void onItemDeletion(MokaItem item) {
		JadeUtils.getAndroidAgentInterface().deleteItem(item.getId());
		setResult(EditItemFragment.RESULT_DELETE);
		finish();
	}

	@Override
	public void onSuccess() {
		Crouton.makeText(this, getResources().getString(R.string.crouton_lock_success), Style.INFO).show();
		resetUi();
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slow_fade_in, R.anim.slow_fade_out)
				.replace(android.R.id.content, EditItemFragment.newInstance(mSelectedItem))
				.commit();
	}

	@Override
	public void onAlreadyLocked(String lockerName) {
		Crouton.makeText(this, String.format(getResources().getString(R.string.crouton_lock_failed_format), lockerName),
				CroutonUtils.INFO_MOKA_STYLE).show();
		mProgressBar.setVisibility(View.GONE);
		showRetryButton();
	}

	@Override
	public void onError() {
		Crouton.makeText(this, getResources().getString(R.string.crouton_lock_error), Style.ALERT).show();
		mProgressBar.setVisibility(View.GONE);
		showRetryButton();
	}

	private void resetUi() {
		mProgressBar.setVisibility(View.GONE);
		mProgressBar = null;
		if (mButtonRetry != null) {
			mButtonRetry.setVisibility(View.GONE);
			mButtonRetry = null;
		}
	}

	private void showRetryButton() {
		if (mButtonRetry == null) {
			final ViewStub retryStub = (ViewStub) findViewById(R.id.retry);
			retryStub.setOnInflateListener(new ViewStub.OnInflateListener() {
				@Override
				public void onInflate(ViewStub viewStub, View view) {
					mButtonRetry = (Button) view;
				}
			});
			retryStub.inflate();
		} else {
			mButtonRetry.setVisibility(View.VISIBLE);
		}
	}

	public void retry(View v) {
		mButtonRetry.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		JadeUtils.getAndroidAgentInterface().lockItem(mSelectedItem.getId());
	}
}