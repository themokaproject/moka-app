package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import fr.utc.nf28.moka.CurrentItemAdapter;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.ArrayList;
import java.util.List;

public class CurrentElementListFragment extends SherlockFragment {
	private List<CurrentItem> items = new ArrayList<CurrentItem>(10);

	public CurrentElementListFragment() {
	}

	// Fragment lifecycle management
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);

		for (int i = 0; i < 10; i++) {
			items.add(new CurrentItem("item " + i));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_current_element_list, container, false);

		final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
		final CurrentItemAdapter adapter = new CurrentItemAdapter(getSherlockActivity());
		adapter.updateCurrentItems(items);
		listView.setAdapter(adapter);

		return rootView;
	}
}
