package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import fr.utc.nf28.moka.ItemAdapter;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.BaseItem;

import java.util.ArrayList;
import java.util.List;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ItemListFragment extends SherlockFragment implements AdapterView.OnItemClickListener,
		SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener,
		AdapterView.OnItemLongClickListener, ItemAdapter.SectionFilterCallbacks {
	private static final String TAG = makeLogTag(ItemListFragment.class);
	private static final String PERSISTENT_LAST_CLASS = "Moka_LastClass";
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(BaseItem item) {
		}

		@Override
		public void onItemLongClicked(BaseItem item) {
		}
	};
	private final List<BaseItem> items = new ArrayList<BaseItem>(10);
	private ItemAdapter mAdapter;
	private Callbacks mCallbacks = sDummyCallbacks;
	private SharedPreferences mPrefs;
	private Spinner mSpinner;
	private StickyGridHeadersGridView mGridView;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

		mSpinner = (Spinner) rootView.findViewById(R.id.spinner);
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getSherlockActivity(),
				R.array.item_classes, R.layout.simple_spinner_item_bigger);
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_bigger);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(this);

		mGridView = (StickyGridHeadersGridView) rootView.findViewById(R.id.grid);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		mGridView.setAreHeadersSticky(false);
		mGridView.setEmptyView(rootView.findViewById(android.R.id.empty));
		mGridView.getEmptyView().setVisibility(View.GONE);
		mAdapter = new ItemAdapter(getSherlockActivity());

		final int resources[] = new int[]{
				R.drawable.logo,
				R.drawable.logo_item_web,
				R.drawable.ic_launcher,
				R.drawable.ic_launcher,
				R.drawable.logo_item_text,
				R.drawable.logo_item_picture,
				R.drawable.logo_item_list,
				R.drawable.logo_item_picture,
				R.drawable.logo,
				R.drawable.logo_item_video
		};

		final BaseItem[] sampleItems = new BaseItem[]{
				new BaseItem("Logo"),
				new BaseItem("Web"),
				new BaseItem("Icône"),
				new BaseItem("Icône"),
				new BaseItem("Texte"),
				new BaseItem("Image"),
				new BaseItem("Liste"),
				new BaseItem("Image"),
				new BaseItem("Logo"),
				new BaseItem("Vidéo")
		};

		final int nbSections = mSpinner.getCount() - 1;
		for (int i = 0; i <= 9; i++) {
			sampleItems[i].setClassName(adapter.getItem(i % nbSections).toString());
			sampleItems[i].setDescription("Une description");
			sampleItems[i].setResId(resources[i]);
			items.add(sampleItems[i]);
		}
		mAdapter.setSectionFilterCallbacks(this);
		mAdapter.updateItems(items);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		mSpinner.setSelection(loadLastClassPreference());
	}

	@Override
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		mAdapter.resetSectionFilterCallbacks();

		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		mCallbacks.onItemSelected(mAdapter.getItem(position));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
		mCallbacks.onItemLongClicked(mAdapter.getItem(position));
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_item_list, menu);

		// Setup searchMenuItem and searchView
		final MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
		searchMenuItem.setOnActionExpandListener(this);
		final SearchView searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setQueryHint(getSherlockActivity().getString(R.string.search_hint));
		searchView.setOnQueryTextListener(this);

		// Workaround for the bug that occurs when changing tab while in search mode.
		// There might be a better solution.
		mAdapter.getFilter().filter(null);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getSherlockActivity().getMenuInflater();
		inflater.inflate(R.menu.activity_item_detail, menu);
	}

	private void saveLastClassPreference(int position) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mPrefs.edit().putInt(PERSISTENT_LAST_CLASS, position).apply();
		} else {
			mPrefs.edit().putInt(PERSISTENT_LAST_CLASS, position).commit();
		}
	}

	private int loadLastClassPreference() {
		try {
			return mPrefs.getInt(PERSISTENT_LAST_CLASS, mSpinner.getCount() - 1);
		} catch (IllegalArgumentException e) {
			return 0;
		}
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (position == mSpinner.getCount() - 1) {
			mAdapter.getSectionFilter().filter(null);
		} else {
			mAdapter.getSectionFilter().filter(parent.getItemAtPosition(position).toString());
		}
		saveLastClassPreference(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onSectionExist(List<BaseItem> sectionItems) {
		if (mGridView.getAdapter() == null) {
			mGridView.setAdapter(mAdapter);
			mGridView.getEmptyView().setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onNoSuchSection() {
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
		public void onItemSelected(BaseItem item);

		/**
		 * Callback for when an item has been long clicked.
		 */
		public void onItemLongClicked(BaseItem item);
	}
}
