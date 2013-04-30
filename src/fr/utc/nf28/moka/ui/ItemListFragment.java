package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import fr.utc.nf28.moka.ItemAdapter;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.CurrentItem;

import java.util.ArrayList;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ItemListFragment extends SherlockFragment implements AdapterView.OnItemClickListener,
		SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
	private static final String TAG = makeLogTag(ItemListFragment.class);
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(CurrentItem item) {
		}
	};
	private List<CurrentItem> items = new ArrayList<CurrentItem>(10);
	private ItemAdapter mAdapter;
	private Callbacks mCallbacks = sDummyCallbacks;

	public ItemListFragment() {
	}

	// Fragment lifecycle management
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

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
		gridView.setOnItemClickListener(this);
		mAdapter = new ItemAdapter(getSherlockActivity());
		mAdapter.updateItems(items);

		gridView.setAdapter(mAdapter);

		return rootView;
	}

	@Override
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;

		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		mCallbacks.onItemSelected(mAdapter.getItem(position));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_item_list, menu);

		// Set the search hint text
		final MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
		searchMenuItem.setOnActionExpandListener(this);
		final SearchView searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setQueryHint(getSherlockActivity().getString(R.string.search_hint));
		searchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		mAdapter.getFilter().filter(newText);
		return true;
	}

	@Override
	public boolean onMenuItemActionExpand(MenuItem item) {
		return true;
	}

	@Override
	public boolean onMenuItemActionCollapse(MenuItem item) {
		mAdapter.getFilter().filter(null);
		return true;
	}

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(CurrentItem item);
	}
}
