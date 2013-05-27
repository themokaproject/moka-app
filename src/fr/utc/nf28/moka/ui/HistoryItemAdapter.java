package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.HistoryEntry;
import fr.utc.nf28.moka.ui.base.BaseMokaAdapter;
import fr.utc.nf28.moka.ui.base.ViewHolder;

public class HistoryItemAdapter extends BaseMokaAdapter {
	private List<HistoryEntry> mHistoryEntries = Collections.emptyList();

	public HistoryItemAdapter(Context context) {
		super(context);
	}

	public void updateHistoryItems(List<HistoryEntry> historyEntries) {
		mHistoryEntries = historyEntries;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mHistoryEntries.size();
	}

	@Override
	public HistoryEntry getItem(int position) {
		return mHistoryEntries.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.history_item, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final HistoryEntry historyEntry = getItem(position);
		itemName.setText(historyEntry.getAction());

		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
