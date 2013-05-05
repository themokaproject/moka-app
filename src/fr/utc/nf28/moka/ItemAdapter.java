package fr.utc.nf28.moka;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
		updateItems(items, false, false);
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
		final ImageView itemImage = ViewHolder.get(convertView, R.id.item_image);
		final BaseItem item = getItem(position);
		itemName.setText(item.getName());
		itemImage.setImageResource(item.getResId());

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

	private final class ItemFilter extends MokaItemsFilter {
		@Override
		protected void onEmptyRequest() {
			if (mSectionFilter != null && mSectionFilter.isQuerying) {
				filterResults.values = mSectionFilterItems;
				filterResults.count = mSectionFilterItems.size();
			} else {
				filterResults.values = mSavedItems;
				filterResults.count = mSavedItems.size();
			}
		}

		@Override
		protected void onRequest(String query) {
			if (mSectionFilter != null && mSectionFilter.isQuerying) {
				final List<BaseItem> currentItems = mItems;
				for (BaseItem item : currentItems) {
					if (item.getName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<BaseItem> savedItems = mSavedItems;
				for (BaseItem item : savedItems) {
					if (item.getName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			}
			filterResults.values = foundItems;
			filterResults.count = foundItems.size();
		}

		@Override
		protected void onPublish(List<BaseItem> results) {
			final boolean isSectionFiltering = mSectionFilter != null && mSectionFilter.isQuerying;
			updateItems(results, isSectionFiltering, !isSectionFiltering);
		}
	}

	private final class SectionFilter extends MokaItemsFilter {
		@Override
		protected void onEmptyRequest() {
			if (mItemFilter != null && mItemFilter.isQuerying) {
				filterResults.values = mItemFilterItems;
				filterResults.count = mItemFilterItems.size();
			} else {
				filterResults.values = mSavedItems;
				filterResults.count = mSavedItems.size();
			}
		}

		@Override
		protected void onRequest(String query) {
			if (mItemFilter != null && mItemFilter.isQuerying) {
				final List<BaseItem> currentItems = mItems;
				for (BaseItem item : currentItems) {
					if (item.getClassName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<BaseItem> savedItems = mSavedItems;
				for (BaseItem item : savedItems) {
					if (item.getClassName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			}
			filterResults.values = foundItems;
			filterResults.count = foundItems.size();
		}

		@Override
		protected void onPublish(List<BaseItem> results) {
			final boolean isItemFiltering = mItemFilter != null && mItemFilter.isQuerying;
			updateItems(results, isItemFiltering, !isItemFiltering);
			if (results == null || results.isEmpty()) {
				mSectionFilterCallbacks.onNoSuchSection();
			} else {
				mSectionFilterCallbacks.onSectionExist(results);
			}
		}
	}
}
