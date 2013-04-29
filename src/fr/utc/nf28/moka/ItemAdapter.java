package fr.utc.nf28.moka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.Collections;
import java.util.List;

public class ItemAdapter extends BaseAdapter {
	private final LayoutInflater mLayoutInflater;
	private List<CurrentItem> mCurrentItems = Collections.emptyList();

	public ItemAdapter(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void updateItems(List<CurrentItem> currentItems) {
		mCurrentItems = currentItems;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mCurrentItems.size();
	}

	@Override
	public CurrentItem getItem(int position) {
		return mCurrentItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final CurrentItem item = getItem(position);
		itemName.setText(item.getName());

		return convertView;
	}
}
