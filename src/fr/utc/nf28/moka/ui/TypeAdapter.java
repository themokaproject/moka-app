package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.ui.base.BaseMokaAdapter;
import fr.utc.nf28.moka.ui.base.MokaFilter;
import fr.utc.nf28.moka.ui.base.ViewHolder;

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
	private final Context mContext;
	private final SparseArray<String> mSectionToPosition;
	private List<MokaType> mTypes = Collections.emptyList();
	private List<MokaType> mSavedTypes = Collections.emptyList();
	private List<MokaType> mSectionFilterTypes = Collections.emptyList();
	private List<MokaType> mItemFilterTypes = Collections.emptyList();
	private SectionFilterCallbacks mSectionFilterCallbacks = sDummySearchCallbacks;
	private TypeFilter mTypeFilter;
	private SectionFilter mSectionFilter;

	public TypeAdapter(Context context) {
		super(context);

		mContext = context;
		mSectionToPosition = new SparseArray<String>();
	}

	public void updateTypes(List<MokaType> types) {
		updateTypes(types, false, false);
	}

	private void updateTypes(List<MokaType> types, boolean dueToMainFilterOperation, boolean dueToSectionFilterOperation) {
		mSectionToPosition.clear();

		int i = 0;
		for (MokaType type : types) {
			mSectionToPosition.append(i, type.getCategoryName());
			i++;
		}

		Collections.sort(types);
		mTypes = types;
		notifyDataSetChanged();

		if (!dueToMainFilterOperation && !dueToSectionFilterOperation) {
			mSavedTypes = new ArrayList<MokaType>(types);
		}
		if (dueToMainFilterOperation) {
			mItemFilterTypes = new ArrayList<MokaType>(types);
		}
		if (dueToSectionFilterOperation) {
			mSectionFilterTypes = new ArrayList<MokaType>(types);
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
		return mTypes.size();
	}

	@Override
	public MokaType getItem(int position) {
		return mTypes.get(position);
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
		Picasso.with(mContext)
				.load(item.getResId())
				.resizeDimen(R.dimen.list_type_picture_width_height, R.dimen.list_type_picture_width_height)
				.into(itemImage);

		return convertView;
	}

	public boolean hasItems() {
		return mSavedTypes != null && !mSavedTypes.isEmpty();
	}

	@Override
	public Filter getFilter() {
		if (mTypeFilter == null) {
			mTypeFilter = new TypeFilter();
		}
		return mTypeFilter;
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
		sectionName.setText(getItem(position).getCategoryName().toUpperCase());

		return convertView;
	}

	public interface SectionFilterCallbacks {
		public void onSectionExist(List<MokaType> sections);

		public void onNoSuchSection();
	}

	private final class TypeFilter extends MokaFilter {
		@Override
		protected void onEmptyRequest() {
			if (mSectionFilter != null && mSectionFilter.isQuerying) {
				filterResults.values = mSectionFilterTypes;
				filterResults.count = mSectionFilterTypes.size();
			} else {
				filterResults.values = mSavedTypes;
				filterResults.count = mSavedTypes.size();
			}
		}

		@Override
		protected void onRequest(String query) {
			if (mSectionFilter != null && mSectionFilter.isQuerying) {
				final List<MokaType> currentTypes = mTypes;
				for (MokaType item : currentTypes) {
					if (item.getName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<MokaType> savedTypes = mSavedTypes;
				for (MokaType item : savedTypes) {
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

	private final class SectionFilter extends MokaFilter {
		@Override
		protected void onEmptyRequest() {
			if (mTypeFilter != null && mTypeFilter.isQuerying) {
				filterResults.values = mItemFilterTypes;
				filterResults.count = mItemFilterTypes.size();
			} else {
				filterResults.values = mSavedTypes;
				filterResults.count = mSavedTypes.size();
			}
		}

		@Override
		protected void onRequest(String query) {
			if (mTypeFilter != null && mTypeFilter.isQuerying) {
				final List<MokaType> currentTypes = mTypes;
				for (MokaType item : currentTypes) {
					if (item.getCategoryName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			} else {
				final List<MokaType> savedTypes = mSavedTypes;
				for (MokaType item : savedTypes) {
					if (item.getCategoryName().toLowerCase().contains(query)) {
						foundItems.add(item);
					}
				}
			}
			filterResults.values = foundItems;
			filterResults.count = foundItems.size();
		}

		@Override
		protected void onPublish(List<MokaType> results) {
			final boolean isItemFiltering = mTypeFilter != null && mTypeFilter.isQuerying;
			updateTypes(results, isItemFiltering, !isItemFiltering);
			if (results == null || results.isEmpty()) {
				mSectionFilterCallbacks.onNoSuchSection();
			} else {
				mSectionFilterCallbacks.onSectionExist(results);
			}
		}
	}
}
