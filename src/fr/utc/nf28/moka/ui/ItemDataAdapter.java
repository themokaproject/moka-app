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
    private final List<ItemData> mItemsData;
    private final MokaItem mCurrentItem;

    public ItemDataAdapter(Context context, List<ItemData> itemsData, MokaItem currentItem) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemsData = itemsData;
        mCurrentItem = currentItem;
    }

    public void getView(ViewGroup parent) {
        if (mItemsData == null) {
            return; // TODO: remove once all items data are implemented
        }
        for (final ItemData itemData : mItemsData) {
            final String field = itemData.getField();
            if ("title".equals(field)) {
                final View rootView = mLayoutInflater.inflate(R.layout.item_data_edit_text, parent, true);
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
            }
        }
    }
}
