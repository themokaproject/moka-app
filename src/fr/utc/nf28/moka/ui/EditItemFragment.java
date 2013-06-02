package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.io.receiver.LockingReceiver;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.ui.custom.MoveItemListener;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment implements LockingReceiver.OnLockingListener {
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemDeletion(MokaItem item) {
		}
	};
	private static final String TAG = makeLogTag(EditItemFragment.class);
	private static final int MOVE_NOISE = 10;
	private final MokaItem mSelectedItem;
	private final IAndroidAgent mAgent = JadeUtils.getAndroidAgentInterface();
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
	private float mLastX = -1f;
	private float mLastY = -1f;
	private Callbacks mCallbacks;
	private View mCanvasMoveItem;
	/**
	 * Broadcast receiver used to catch locking callback from SMA
	 */
	private LockingReceiver mLockingReceiver;

	public EditItemFragment(MokaItem selectedItem) {
		if (selectedItem == null) {
			throw new IllegalStateException("Selected item cannot be null");
		}

		mSelectedItem = selectedItem;
	}

	public static EditItemFragment newInstance(MokaItem selectedItem) {
		return new EditItemFragment(selectedItem);
	}

	// Fragment lifecycle management

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);

		mLockingReceiver = new LockingReceiver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		final TextView itemName = (TextView) rootView.findViewById(R.id.history_name);
		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemCategory = (TextView) rootView.findViewById(R.id.item_category);
		final TextView itemCreator = (TextView) rootView.findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);
		mCanvasMoveItem = rootView.findViewById(R.id.canvas_move_item);

		itemName.setText(mSelectedItem.getTitle());
		itemType.setText(mSelectedItem.getType().getName());
		itemCategory.setText(mSelectedItem.getType().getCategoryName());
		itemCreator.setText(String.format(getResources().getString(R.string.item_info_creation),
				mSelectedItem.getCreatorName(), mSelectedItem.getCreationDate()));
		itemImage.setImageResource(mSelectedItem.getType().getResId());
		mCanvasMoveItem.setOnTouchListener(new MoveItemListener() {
			@Override
			public void move(int direction, int velocity) {
				mAgent.moveItem(mSelectedItem.getId(), direction, velocity);
			}

			@Override
			public void resize(int direction) {
				mAgent.resizeItem(mSelectedItem.getId(), direction);
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().getSupportActionBar().setTitle(mSelectedItem.getTitle());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_edit_item, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete:
				final Resources resources = getResources();
				new AlertDialog.Builder(getSherlockActivity())
						.setTitle(resources.getString(R.string.delete_confirmation_title))
						.setMessage(String.format(resources.getString(R.string.delete_confirmation_message),
								mSelectedItem.getTitle()))
						.setPositiveButton(resources.getString(R.string.delete_confirmation_ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int which) {
										mCallbacks.onItemDeletion(mSelectedItem);
									}
								})
						.setNegativeButton(resources.getString(R.string.delete_confirmation_cancel),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int which) {
									}
								})
						.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mAgent.unlockItem(mSelectedItem.getId());
		LocalBroadcastManager.getInstance(getSherlockActivity()).unregisterReceiver(mLockingReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(getSherlockActivity()).registerReceiver(mLockingReceiver, mIntentFilter);
	}

	@Override
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;

		super.onDetach();
	}

	@Override
	public void onSuccess() {
		//TODO display full fragment
		Crouton.makeText(getSherlockActivity(), "Element locké pour vous ! ",
				Style.CONFIRM).show();
	}

	@Override
	public void onAlreadyLocked(String lockerName) {
		//TODO display fragment without edition possibility
		Crouton.makeText(getSherlockActivity(), "Element déjà locké par " + lockerName,
				CroutonUtils.INFO_MOKA_STYLE).show();
	}

	@Override
	public void onError() {
		//TODO display error or return ?
		Crouton.makeText(getSherlockActivity(), "locking error ",
				Style.ALERT).show();
	}


	/**
	 * A callback interface that all activities containing this fragment must
	 * implement.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been deleted.
		 */
		public void onItemDeletion(MokaItem item);
	}
}
