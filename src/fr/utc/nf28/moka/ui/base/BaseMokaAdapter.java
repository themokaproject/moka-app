package fr.utc.nf28.moka.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class BaseMokaAdapter extends BaseAdapter {
	protected final LayoutInflater mLayoutInflater;

	public BaseMokaAdapter(Context context) {
		super();

		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
