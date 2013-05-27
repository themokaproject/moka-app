package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ComputerItem;
import fr.utc.nf28.moka.data.MediaItem;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.TextItem;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class CurrentItemListFragment extends SherlockFragment implements AdapterView.OnItemClickListener {
	private static final String TAG = makeLogTag(CurrentItemListFragment.class);
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(MokaItem item) {
		}
	};
	private final List<MokaItem> mItems = new ArrayList<MokaItem>(10);
	private CurrentItemAdapter mAdapter;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	public CurrentItemListFragment() {
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

		mItems.add(new MediaItem.ImageItem("Image"));
		mItems.add(new MediaItem.VideoItem("Vidéo"));
		mItems.add(new MediaItem.WebItem("Web"));
		mItems.add(new ComputerItem.UmlItem("Diagramme UML"));
		mItems.add(new TextItem.PlainTextItem("Texte"));
		mItems.add(new TextItem.ListItem("Liste"));
		mItems.add(new TextItem.PostItItem("Post-it"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_current_item_list, container, false);

		final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
		listView.setOnItemClickListener(this);
		listView.setEmptyView(rootView.findViewById(android.R.id.empty));
		mAdapter = new CurrentItemAdapter(getSherlockActivity());
		mAdapter.updateCurrentItems(mItems);
		listView.setAdapter(mAdapter);

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

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(MokaItem item);
	}
}
