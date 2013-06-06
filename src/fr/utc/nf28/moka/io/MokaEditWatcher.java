package fr.utc.nf28.moka.io;

import android.text.Editable;
import android.text.TextWatcher;

import fr.utc.nf28.moka.util.JadeUtils;

public class MokaEditWatcher implements TextWatcher {

	private int mItemId;
	private String mField;

	public MokaEditWatcher(final int itemId, final String field) {
		mItemId = itemId;
		mField = field;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
		JadeUtils.getAndroidAgentInterface().editItem(mItemId, mField, charSequence.toString());
	}

	@Override
	public void afterTextChanged(Editable editable) {

	}
}
