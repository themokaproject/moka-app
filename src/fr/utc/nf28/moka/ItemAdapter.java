package fr.utc.nf28.moka;

import android.content.Context;
import android.util.Log;
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
	private static final SectionFilterCallbacks sDummySearchCallbacks = new SectionFilterCallbacks() {
		@Override
		public void onSectionExist(List<BaseItem> sectionItems) {
		}

		@Override
		public void onNoSuchSection() {
		}
	};
	private static final String TAG = makeLogTag(ItemAdapter.class);
	private final SparseArray<String> mSectionToPosition;
	private List<BaseItem> mItems = Collections.emptyList();
	private List<BaseItem> mSavedItems = Collections.emptyList();
	private List<BaseItem> mSectionFilterItems = Collections.emptyList();
	private List<BaseItem> mItemFilterItems = Collections.emptyList();
	private SectionFilterCallbacks mSectionFilterCallbacks = sDummySearchCallbacks;
	private ItemFilter mItemFilter;
	private SectionFilter mSectionFilter;

	public ItemAdapter(Context context) {
		super(context);

		mSectionToPosition = new SparseArray<String>();
	}

	public void updateItems(List<BaseItem> items) {
		updateItems(items, false);
	}

	private void updateItems(List<BaseItem> items, boolean dueToMainFilterOperation) {
		updateItems(items, dueToMainFilterOperation, false);
	}

	private void updateItems(List<BaseItem> items, boolean dueToMainFilterOperation, boolean dueToSectionFilterOperation) {
		mSectionToPosition.clear();

		int i = 0;
		for (BaseItem item : items) {
			mSectionToPosition.append(i, item.getClassName());
			i++;
		}

		Collections.sort(items);
		mItems = items;
		notifyDataSetChanged();

		if (!dueToMainFilterOperation && !dueToSectionFilterOperation) {
			mSavedItems = new ArrayList<BaseItem>(items);
		}

		if (dueToMainFilterOperation) {
			mItemFilterItems = new ArrayList<BaseItem>(items);
		}
		if (dueToSectionFilterOperation) {
			mSectionFilterItems = new ArrayList<BaseItem>(items);
		}
	}

	public void setSectionFilterCallbacks(SectionFilterCallbacks callbacks) {
		mSectionFilterCallbacks = callbacks;
	}

	public void resetSectionFilterCallbacks() {
		mSectionFilterCallbacks = sDummySearchCallbacks;
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
		if (mItemFilter == null) {
			mItemFilter = new ItemFilter();
		}
		return mItemFilter;
	}

	public Filter getSectionFilter() {
		if (mSectionFilter == null) {
			mSectionFilter = new SectionFilter();
		}
		return mSectionFilter;
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

	public interface SectionFilterCallbacks {
		public void onSectionExist(List<BaseItem> sectionItems);

		public void onNoSuchSection();
	}

	private class ItemFilter extends Filter {
		private boolean mIsQuerying;

		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {
			mIsQuerying = true;
			final FilterResults results = new FilterResults();
			if (charSequence == null || charSequence.length() == 0) {
				if (mSectionFilter != null && mSectionFilter.isQuerying()) {
					results.values = mSectionFilterItems;
					results.count = mSectionFilterItems.size();
				} else {
					results.values = mSavedItems;
					results.count = mSavedItems.size();
				}
				mIsQuerying = false;
				return results;
			}

			Log.d(TAG, "performFiltering, query == " + charSequence);
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

		public boolean isQuerying() {
			return mIsQuerying;
		}
	}

	private class SectionFilter extends Filter {
		private boolean mIsQuerying;

		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {
			mIsQuerying = true;
			final FilterResults results = new FilterResults();
			if (charSequence == null) {
				if (mItemFilter != null && mItemFilter.isQuerying()) {
					results.values = mItemFilterItems;
					results.count = mItemFilterItems.size();
				} else {
					results.values = mSavedItems;
					results.count = mSavedItems.size();
				}
				mIsQuerying = false;
				return results;
			}

			Log.d(TAG, "performFiltering2, query == " + charSequence);
			final List<BaseItem> foundItems = new ArrayList<BaseItem>();
			if (mItemFilter != null && mItemFilter.isQuerying()) {
				for (BaseItem item : mItems) {
					if (item.getClassName().contains(charSequence)) {
						foundItems.add(item);
					}
				}
			} else {
				for (BaseItem item : mSavedItems) {
					if (item.getClassName().contains(charSequence)) {
						foundItems.add(item);
					}
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
			updateItems(results, false, true);
			if (results == null || results.isEmpty()) {
				mSectionFilterCallbacks.onNoSuchSection();
			} else {
				mSectionFilterCallbacks.onSectionExist(results);
			}
		}

		public boolean isQuerying() {
			return mIsQuerying;
		}
	}
}
