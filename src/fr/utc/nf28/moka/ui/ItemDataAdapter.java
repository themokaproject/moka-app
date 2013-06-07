package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ItemData;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;
import fr.utc.nf28.moka.util.JadeUtils;

public class ItemDataAdapter {
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onTitleChanged(String field, String title) {
		}

		@Override
		public void onUploadPicture(EditText viewToUpdate) {
		}
	};
	private final LayoutInflater mLayoutInflater;
	private final MokaItem mCurrentItem;
	private final ViewGroup mParent;
	private List<ItemData> mItemsData;
	private Callbacks mCallbacks = sDummyCallbacks;

	public ItemDataAdapter(Context context, MokaItem currentItem, ViewGroup parent) {
		mLayoutInflater = LayoutInflater.from(context);
		mCurrentItem = currentItem;
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
		if (MokaType.KEY_TITLE.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_title, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_title);
			editText.setText(mCurrentItem.getTitle());
			editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					mCallbacks.onTitleChanged(field, charSequence.toString());
				}

				@Override
				public void afterTextChanged(Editable editable) {
				}
			});
			return rootView;
		}
		if (TextType.KEY_CONTENT.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_content, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_content);
			editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					JadeUtils.getAndroidAgentInterface().editItem(mCurrentItem.getId(), field, charSequence.toString());
				}

				@Override
				public void afterTextChanged(Editable editable) {
				}
			});
			return rootView;
		}
		if (MediaType.KEY_URL.equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_url, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_url);
			editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
					JadeUtils.getAndroidAgentInterface().editItem(mCurrentItem.getId(), field, charSequence.toString());
				}

				@Override
				public void afterTextChanged(Editable editable) {
				}
			});
			final ImageButton uploadButton = (ImageButton) rootView.findViewById(R.id.item_data_upload);
			uploadButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mCallbacks.onUploadPicture(editText);
				}
			});
			return rootView;
		}
		return null;
	}

	public interface Callbacks {
		public void onTitleChanged(String field, String title);

		public void onUploadPicture(EditText viewToUpdate);
	}
}