package fr.utc.nf28.moka;

import android.widget.Filter;
import fr.utc.nf28.moka.data.BaseItem;

import java.util.ArrayList;
import java.util.List;

public abstract class MokaItemsFilter extends Filter {
	protected final FilterResults filterResults;
	protected final List<BaseItem> foundItems;
	protected boolean isQuerying = false;

	public MokaItemsFilter() {
		filterResults = new FilterResults();
		foundItems = new ArrayList<BaseItem>();
	}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence) {
		if (charSequence == null || charSequence.length() == 0) {
			isQuerying = false;
			onEmptyRequest();
			return filterResults;
		}

		foundItems.clear();
		isQuerying = true;

		onRequest(charSequence.toString().toLowerCase());
		return filterResults;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
		onPublish((List<BaseItem>) filterResults.values);
	}

	protected abstract void onEmptyRequest();

	protected abstract void onRequest(String query);

	protected abstract void onPublish(List<BaseItem> results);
}
