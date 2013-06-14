package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.io.File;
import java.lang.ref.WeakReference;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.ui.custom.MoveItemListener;
import fr.utc.nf28.moka.util.HttpHelper;
import fr.utc.nf28.moka.util.ImageUtils;
import fr.utc.nf28.moka.util.JadeUtils;
import fr.utc.nf28.moka.util.MokaRestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment implements ItemDataAdapter.Callbacks {
	public static final int RESULT_DELETE = Activity.RESULT_FIRST_USER + 1;
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
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST = RESULT_DELETE + 1;
	private final MokaItem mSelectedItem;
	private final IAndroidAgent mAgent = JadeUtils.getAndroidAgentInterface();
	private Callbacks mCallbacks;
	private ItemDataAdapter mItemDataAdapter;
	private EditText mViewToUpdate;

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

		final MokaType type = mSelectedItem.getType();

		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemCategory = (TextView) rootView.findViewById(R.id.item_category);
		final TextView itemCreationDate = (TextView) rootView.findViewById(R.id.item_creation_date);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);
		final EditText itemTitle = (EditText) rootView.findViewById(R.id.item_data_title);
		final View canvasMoveItem = rootView.findViewById(R.id.canvas_move_item);

		itemType.setText(type.getName());
		itemCategory.setText(type.getCategoryName());
		itemCreationDate.setText(mSelectedItem.getCreationDate());
		itemImage.setImageResource(type.getResId());
		itemTitle.setText(mSelectedItem.getTitle());
		itemTitle.addTextChangedListener(new ItemDataAdapter.MokaTextWatcher() {
			@Override
			public void onTextChanged(String newText) {
				getSherlockActivity().getSupportActionBar().setTitle(newText);
				mSelectedItem.setTitle(newText);
				mAgent.editItem(mSelectedItem.getId(), MokaType.KEY_TITLE, newText);
			}
		});
		canvasMoveItem.setOnTouchListener(new MoveItemListener() {
			@Override()
			public void move(int direction, int velocity) {
				mAgent.moveItem(mSelectedItem.getId(), direction, velocity);
			}

			@Override
			public void resize(int direction) {
				mAgent.resizeItem(mSelectedItem.getId(), direction);
			}

			@Override
			public void rotate(int direction) {
				mAgent.rotateItem(mSelectedItem.getId(), direction);
			}
		});

		mItemDataAdapter = new ItemDataAdapter(getSherlockActivity(),
				(ViewGroup) rootView.findViewById(R.id.item_data_fields));
		mItemDataAdapter.setCallbacks(this);
		mItemDataAdapter.updateItemsData(type.getItemData());

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
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		mItemDataAdapter.resetCallbacks();
		mAgent.unlockItem(mSelectedItem.getId());

		super.onDetach();
	}

	@Override
	public void onContentChanged(String field, String content) {
		mSelectedItem.update(field, content);
		mAgent.editItem(mSelectedItem.getId(), field, content);
	}

	@Override
	public void onUrlChanged(String field, String url) {
		mSelectedItem.update(field, url);
		mAgent.editItem(mSelectedItem.getId(), field, url);
	}

	@Override
	public void onUploadRequested(String field, EditText viewToUpdate) {
		mViewToUpdate = viewToUpdate;
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(ImageUtils.getTempPictureFile()));
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {

				final String apiUrl = HttpHelper.getMokaApiUrl(getSherlockActivity());

				final int itemId = mSelectedItem.getId();
				new UploadImageTask(apiUrl, itemId, new Callback<Response>() {
					@Override
					public void success(Response response, Response response2) {
						final String generatedUri = apiUrl + "/item/" + itemId + "/image/";
						mSelectedItem.update(MediaType.KEY_URL, generatedUri);
						mAgent.editItem(itemId, MediaType.KEY_URL, generatedUri);
						if (mViewToUpdate != null) {
							mViewToUpdate.setText(generatedUri);
							mViewToUpdate = null;
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
					}
				}, this).execute(ImageUtils.getTempPictureFile());
			} else if (resultCode != Activity.RESULT_CANCELED) {
				Crouton.makeText(getSherlockActivity(), getResources().getString(R.string.network_error),
						Style.ALERT).show();
				mViewToUpdate = null;
			}
		}
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

	private static class UploadImageTask extends AsyncTask<File, Void, File> {
		private final String mApiUrl;
		private final int mItemId;
		private final Callback<Response> mRetrofitCallbacks;
		private final WeakReference<EditItemFragment> mUi;
		private WeakReference<ProgressDialog> mProgressDialog;

		public UploadImageTask(String apiUrl, int itemId, Callback<Response> retrofitCallbacks, EditItemFragment ui) {
			mApiUrl = apiUrl;
			mItemId = itemId;
			mRetrofitCallbacks = retrofitCallbacks;
			mUi = new WeakReference<EditItemFragment>(ui);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			final EditItemFragment ui = mUi.get();
			if (ui != null) {
				final Resources resources = ui.getResources();
				mProgressDialog = new WeakReference<ProgressDialog>(
						ProgressDialog.show(
								ui.getSherlockActivity(),
								resources.getString(R.string.progress_dialog_upload_image_title),
								resources.getString(R.string.progress_dialog_upload_image_message),
								true,
								false)
				);
			}
		}

		@Override
		protected File doInBackground(File... params) {
			final File image = params[0];
			ImageUtils.resize(image, ImageUtils.PICTURE_SIZE_LR);
			return image;
		}

		@Override
		protected void onPostExecute(File result) {
			super.onPostExecute(result);

			MokaRestHelper.getMokaRestService(mApiUrl).uploadPicture(mItemId,
					new TypedFile("image/jpeg", result), mRetrofitCallbacks);

			final EditItemFragment ui = mUi.get();
			if (ui != null) {
				final ProgressDialog progressDialog = mProgressDialog.get();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		}
	}
}
