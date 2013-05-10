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
import fr.utc.nf28.moka.data.MokaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class TypeAdapter extends BaseMokaAdapter implements StickyGridHeadersSimpleAdapter, Filterable {
	private static final SectionFilterCallbacks sDummySearchCallbacks = new SectionFilterCallbacks() {
		@Override
		public void onSectionExist(List<MokaType> sections) {
		}

		@Override
		public void onNoSuchSection() {
		}
	};
	private static final String TAG = makeLogTag(TypeAdapter.class);
	private final SparseArray<String> mSectionToPosition;
	private List<MokaType> mItems = Collections.emptyList();
	private List<MokaType> mSavedItems = Collections.emptyList();
	private List<MokaType> mSectionFilterItems = Collections.emptyList();
	private List<MokaType> mItemFilterItems = Collections.emptyList();
	private SectionFilterCallbacks mSectionFilterCallbacks = sDummySearchCallbacks;
	private ItemFilter mItemFilter;
	private SectionFilter mSectionFilter;

	public TypeAdapter(Context context) {
		super(context);

		mSectionToPosition = new SparseArray<String>();
	}

	public void updateTypes(List<MokaType> types) {
		updateTypes(types, false, false);
	}

	private void updateTypes(List<MokaType> types, boolean dueToMainFilterOperation, boolean dueToSectionFilterOperation) {
		mSectionToPosition.clear();

		int i = 0;
		for (MokaType type : types) {
			mSectionToPosition.append(i, type.getTypeName());
			i++;
		}

		Collections.sort(types);
		mItems = types;
		notifyDataSetChanged();

		if (!dueToMainFilterOperation && !dueToSectionFilterOperation) {
			mSavedItems = new ArrayList<MokaType>(types);
		}
		if (dueToMainFilterOperation) {
			mItemFilterItems = new ArrayList<MokaType>(types);
		}
		if (dueToSectionFilterOperation) {
			mSectionFilterItems = new ArrayList<MokaType>(types);
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
	public MokaType getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.type, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final ImageView itemImage = ViewHolder.get(convertView, R.id.item_image);
		final MokaType item = getItem(position);
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
		sectionName.setText(getItem(position).getTypeName().toUpperCase());

		return convertView;
	}

	public interface SectionFilterCallbacks {
		public void onSectionExist(List<MokaType> sections);

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
				final List<MokaType> currentItems = mItems;
				for (MokaType item : currentItems) {
					if (item.getName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<MokaType> savedItems = mSavedItems;
				for (MokaType item : savedItems) {
					if (item.getName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			}
			filterResults.values = foundItems;
			filterResults.count = foundItems.size();
		}

		@Override
		protected void onPublish(List<MokaType> results) {
			final boolean isSectionFiltering = mSectionFilter != null && mSectionFilter.isQuerying;
			updateTypes(results, isSectionFiltering, !isSectionFiltering);
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
				final List<MokaType> currentItems = mItems;
				for (MokaType item : currentItems) {
					if (item.getTypeName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<MokaType> savedItems = mSavedItems;
				for (MokaType item : savedItems) {
					if (item.getTypeName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			}
			filterResults.values = foundItems;
			filterResults.count = foundItems.size();
		}

		@Override
		protected void onPublish(List<MokaType> results) {
			final boolean isItemFiltering = mItemFilter != null && mItemFilter.isQuerying;
			updateTypes(results, isItemFiltering, !isItemFiltering);
			if (results == null || results.isEmpty()) {
				mSectionFilterCallbacks.onNoSuchSection();
			} else {
				mSectionFilterCallbacks.onSectionExist(results);
			}
		}
	}
}
