package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ItemData;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.util.JadeUtils;

import java.util.List;

public class ItemDataAdapter {
	private final LayoutInflater mLayoutInflater;
	private final MokaItem mCurrentItem;
	private final ViewGroup mParent;
	private List<ItemData> mItemsData;

	public ItemDataAdapter(Context context, MokaItem currentItem, ViewGroup parent) {
		mLayoutInflater = LayoutInflater.from(context);
		mCurrentItem = currentItem;
		mParent = parent;
	}

	public void updateItemsData(List<ItemData> itemDatas) {
		mItemsData = itemDatas;
		notifyDataSetChanged();
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
		if ("title".equals(field)) {
			final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_title, mParent, true);
			final EditText editText = (EditText) rootView.findViewById(R.id.item_data_title);
			editText.setText(mCurrentItem.getTitle());
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
		if ("content".equals(field)) {
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
		}
		return null;
	}
}