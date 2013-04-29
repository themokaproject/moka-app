package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;
import fr.utc.nf28.moka.ItemAdapter;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends SherlockFragment {
	private List<CurrentItem> items = new ArrayList<CurrentItem>(10);

	public ItemListFragment() {
	}

	// Fragment lifecycle management
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);

		for (int i = 1; i <= 10; i++) {
			items.add(new CurrentItem("item " + i));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

		final GridView gridView = (GridView) rootView.findViewById(R.id.grid);
		final ItemAdapter adapter = new ItemAdapter(getSherlockActivity());
		adapter.updateItems(items);

		gridView.setAdapter(adapter);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_item_list, menu);

		// Set the search hint text
		((SearchView) menu.findItem(R.id.menu_search).getActionView())
				.setQueryHint(getSherlockActivity().getString(R.string.search_hint));
	}
}
