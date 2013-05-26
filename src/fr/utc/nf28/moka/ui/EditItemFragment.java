package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.util.DateUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment implements View.OnTouchListener {
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
	private float mLastX = -1f;
	private float mLastY = -1f;
	private Callbacks mCallbacks;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		final TextView itemName = (TextView) rootView.findViewById(R.id.item_name);
		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemCategory = (TextView) rootView.findViewById(R.id.item_category);
		final TextView itemCreator = (TextView) rootView.findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);
		final SurfaceView canvasMoveItem = (SurfaceView) rootView.findViewById(R.id.canvas_move_item);

		itemName.setText(mSelectedItem.getTitle());
		itemType.setText(mSelectedItem.getType().getName());
		itemCategory.setText(mSelectedItem.getType().getCategoryName());
		itemCreator.setText("Créé par " + mSelectedItem.getCreatorName() + " le " + DateUtils.getFormattedDate(mSelectedItem.getCreationDate()));
		// TODO: fetch values from strings.xml
		itemImage.setImageResource(mSelectedItem.getType().getResId());
		canvasMoveItem.setOnTouchListener(this);

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
				final StringBuilder sb = new StringBuilder();
				sb.append(resources.getString(R.string.delete_confirmation_message));
				sb.append(" ");
				sb.append(mSelectedItem.getTitle());
				sb.append(" ?");
				// TODO: use string variables
				new AlertDialog.Builder(getSherlockActivity())
						.setTitle(resources.getString(R.string.delete_confirmation_title))
						.setMessage(sb.toString())
						.setPositiveButton(resources.getString(R.string.delete_confirmation_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								mCallbacks.onItemDeletion(mSelectedItem);
							}
						})
						.setNegativeButton(resources.getString(R.string.delete_confirmation_cancel), new DialogInterface.OnClickListener() {
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
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;

		super.onDetach();
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		final int action = motionEvent.getAction();
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			Log.i(TAG, "action_up reset");
			mLastX = -1f;
			mLastY = -1f;
			return true;
		}

		final float currentX = motionEvent.getX();
		final float currentY = motionEvent.getY();

		if (mLastX == -1f || mLastY == -1f) {
			mLastX = currentX;
			mLastY = currentY;
		} else {
			final float dx = currentX - mLastX;
			final float dy = currentY - mLastY;
			int direction = 0;

			if (Math.abs(dx) >= MOVE_NOISE) {
				mLastX = currentX;
				if (dx >= 0) {
					//RIGHT
					direction += 1;
				} else {
					//LEFT
					direction += 2;
				}
			}

			if (Math.abs(dy) >= MOVE_NOISE) {
				mLastY = currentY;
				if (dy >= 0) {
					//BOTTOM
					direction += 10;
				} else {
					//TOP
					direction += 20;
				}
			}

			if (direction != 0) {
				final int pseudoVelocity = (int) Math.max(Math.abs(dx / MOVE_NOISE), Math.abs(dy / MOVE_NOISE));
				Log.i(TAG, "direction " + String.valueOf(direction) + " velocity " + String.valueOf(pseudoVelocity));
				mAgent.moveItem(mSelectedItem.getId(), direction, pseudoVelocity);
			}
		}

		return true;
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
