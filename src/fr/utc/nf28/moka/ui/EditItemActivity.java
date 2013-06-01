package fr.utc.nf28.moka.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.io.receiver.LockingReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemActivity extends MokaUpActivity implements EditItemFragment.Callbacks, LockingReceiver.OnLockingListener {
	public static final String ARG_ITEM = "arg_item";
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 1;
	private static final String TAG = makeLogTag(EditItemActivity.class);
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
	private MokaItem mSelectedItem;
	/**
	 * Broadcast receiver used to catch locking callback from SMA
	 */
	private LockingReceiver mLockingReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		mLockingReceiver = new LockingReceiver(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mSelectedItem = getIntent().getExtras().getParcelable(ARG_ITEM);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.edit_item_container, EditItemFragment.newInstance(mSelectedItem))
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		LocalBroadcastManager.getInstance(this).registerReceiver(mLockingReceiver, mIntentFilter);

		//send locking request to the SMA
		if (mSelectedItem != null) {
			final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
			agent.lockItem(mSelectedItem.getId());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mLockingReceiver);
	}

	@Override
	public void onItemDeletion(MokaItem item) {
		final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
		agent.deleteItem(item.getId());
		setResult(EditItemActivity.RESULT_DELETE);
		finish();
	}

	@Override
	public void onSuccess() {
		//TODO display full fragment
		Crouton.makeText(this, "Element locké pour vous ! ",
				Style.CONFIRM).show();
	}

	@Override
	public void onAlreadyLocked(String lockerName) {
		//TODO display fragment without edition possibility
		Crouton.makeText(this, "Element déjà locké par " + lockerName,
				CroutonUtils.INFO_MOKA_STYLE).show();
	}

	@Override
	public void onError() {
		//TODO display error or return ?
		Crouton.makeText(this, "locking error ",
				Style.ALERT).show();
	}
}