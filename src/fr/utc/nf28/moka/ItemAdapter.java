package fr.utc.nf28.moka;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import fr.utc.nf28.moka.data.BaseItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ItemAdapter extends BaseMokaAdapter implements StickyGridHeadersSimpleAdapter, Filterable {
	private static final String TAG = makeLogTag(ItemAdapter.class);
	private final SparseArray<String> mSectionToPosition;
	private List<BaseItem> mItems = Collections.emptyList();
	private List<BaseItem> mSavedItems = Collections.emptyList();

	public ItemAdapter(Context context) {
		super(context);

		mSectionToPosition = new SparseArray<String>();
	}

	public void updateItems(List<BaseItem> items) {
		updateItems(items, false);
	}

	private void updateItems(List<BaseItem> items, boolean dueToFilterOperation) {
		mSectionToPosition.clear();

		int i = 0;
		for (BaseItem item : items) {
			mSectionToPosition.append(i, item.getClassName());
			i++;
		}

		Collections.sort(items);
		mItems = items;
		notifyDataSetChanged();

		if (!dueToFilterOperation) {
			mSavedItems = new ArrayList<BaseItem>(items);
		}
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public BaseItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final BaseItem item = getItem(position);
		itemName.setText(item.getName());

		return convertView;
	}

	public boolean hasItems() {
		return mSavedItems != null && !mSavedItems.isEmpty();
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

				final List<BaseItem> foundItems = new ArrayList<BaseItem>();
				for (BaseItem item : mSavedItems) {
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
				final List<BaseItem> results = (List<BaseItem>) filterResults.values;
				updateItems(results, true);
			}
		};
	}

	public Filter getSectionFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				final FilterResults results = new FilterResults();
				if (charSequence == null || charSequence.length() == 0) {
					results.values = mSavedItems;
					results.count = mSavedItems.size();
					return results;
				}

				final List<BaseItem> foundItems = new ArrayList<BaseItem>();
				for (BaseItem item : mSavedItems) {
					if (item.getClassName().contains(charSequence)) {
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
				final List<BaseItem> results = (List<BaseItem>) filterResults.values;
				updateItems(results, true);
			}
		};
	}

	@Override
	public long getHeaderId(int position) {
		return mSectionToPosition.get(position).hashCode();
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.header_class_grid, null);
		}

		final TextView sectionName = ViewHolder.get(convertView, R.id.header_text);
		sectionName.setText(getItem(position).getClassName().toUpperCase());

		return convertView;
	}
}
