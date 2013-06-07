package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ItemData;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.TextType;

import java.util.List;

public class ItemDataAdapter {
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onContentChanged(String field, String content) {
		}

		@Override
		public void onUrlChanged(String field, String url) {
		}

		@Override
		public void onUploadPicture(String field, EditText viewToUpdate) {
		}
	};
	private final LayoutInflater mLayoutInflater;
	private final ViewGroup mParent;
	private List<ItemData> mItemsData;
	private Callbacks mCallbacks = sDummyCallbacks;

	public ItemDataAdapter(Context context, ViewGroup parent) {
		mLayoutInflater = LayoutInflater.from(context);
		mParent = parent;
	}

	public void updateItemsData(List<ItemData> itemDatas) {
		mItemsData = itemDatas;
		notifyDataSetChanged();
	}

	public void setCallbacks(Callbacks callbacks) {
		mCallbacks = callbacks;
	}

	public void resetCallbacks() {
		mCallbacks = sDummyCallbacks;
	}

	private void notifyDataSetChanged() {
		if (mItemsData == null) {
			return;
		}
		for (ItemData itemData : mItemsData) {
			getView(itemData.getField());
		}
	}

	private View getView(final String field) {
		if (TextType.KEY_CONTENT.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_content, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_content);
			editText.addTextChangedListener(new MokaTextWatcher() {
				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					mCallbacks.onContentChanged(field, charSequence.toString());
				}
			});
			return rootView;
		}
		if (MediaType.KEY_URL.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_url, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_url);
			editText.addTextChangedListener(new MokaTextWatcher() {
				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					mCallbacks.onUrlChanged(field, charSequence.toString());
				}
			});
			return rootView;
		}
		if (MediaType.ImageType.KEY_URL_UPLOAD.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_url_upload, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_url);
			editText.addTextChangedListener(new MokaTextWatcher() {
				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					mCallbacks.onUrlChanged(MediaType.KEY_URL, charSequence.toString());
				}
			});
			final ImageButton uploadButton = (ImageButton) rootView.findViewById(R.id.item_data_upload);
			uploadButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mCallbacks.onUploadPicture(field, editText);
				}
			});
			return rootView;
		}
		return null;
	}

	public interface Callbacks {
		public void onContentChanged(String field, String content);

		public void onUrlChanged(String field, String url);

		public void onUploadPicture(String field, EditText viewToUpdate);
	}

	static abstract class MokaTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
		}

		@Override
		public abstract void onTextChanged(CharSequence charSequence, int start, int before, int count);

		@Override
		public void afterTextChanged(Editable editable) {
		}
	}
}