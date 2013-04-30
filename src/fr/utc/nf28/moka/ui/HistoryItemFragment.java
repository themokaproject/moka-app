package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import fr.utc.nf28.moka.HistoryItemAdapter;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.ArrayList;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class HistoryItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(HistoryItemFragment.class);
	private final List<CurrentItem> items = new ArrayList<CurrentItem>(10);

	public HistoryItemFragment() {
	}

	// Fragment lifecycle management
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);

		for (int i = 0; i < 10; i++) {
			items.add(new CurrentItem("history " + String.valueOf(10 - i)));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_history_item_list, container, false);

		final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
		final HistoryItemAdapter adapter = new HistoryItemAdapter(getSherlockActivity());
		adapter.updateHistoryItems(items);
		listView.setAdapter(adapter);

		return rootView;
	}
}
