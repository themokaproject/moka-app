package fr.utc.nf28.moka;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ItemAdapter extends BaseMokaAdapter implements Filterable {
	private static final String TAG = makeLogTag(ItemAdapter.class);
	private List<CurrentItem> mCurrentItems = Collections.emptyList();
	private List<CurrentItem> mSavedItems = Collections.emptyList();

	public ItemAdapter(Context context) {
		super(context);
	}

	public void updateItems(List<CurrentItem> items) {
		updateItems(items, false);
	}

	public void updateItems(List<CurrentItem> items, boolean dueToFilterOperation) {
		mCurrentItems = items;
		notifyDataSetChanged();

		if (!dueToFilterOperation) {
			mSavedItems = new ArrayList<CurrentItem>(items);
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final CurrentItem item = getItem(position);
		itemName.setText(item.getName());

		return convertView;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				final FilterResults results = new FilterResults();
				if (charSequence == null || charSequence.length() == 0) {
					results.values = mSavedItems;
					results.count = mSavedItems.size();
					return results;
				}

				final List<CurrentItem> foundItems = new ArrayList<CurrentItem>();
				for (CurrentItem item : mSavedItems) {
					if (item.getName().contains(charSequence)) {
						foundItems.add(item);
					}
				}
				results.values = foundItems;
				results.count = foundItems.size();

				return results;
			}

			@Override
			@SuppressWarnings("unchecked")
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				final List<CurrentItem> results = (List<CurrentItem>) filterResults.values;
				updateItems(results, true);
			}
		};
	}
}
