package fr.utc.nf28.moka.io;

import android.text.Editable;
import android.text.TextWatcher;

public class MokaEditWatcher implements TextWatcher {

	private int mItemId;
	private String mField;
	private String mContent;

	public MokaEditWatcher(final int itemId, final String field, final String content) {
		mItemId = itemId;
		mField = field;
		mContent = content;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

	}

	@Override
	public void afterTextChanged(Editable editable) {

	}
}
