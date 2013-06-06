package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.io.MokaRestService;
import fr.utc.nf28.moka.io.receiver.MokaReceiver;
import fr.utc.nf28.moka.io.receiver.RefreshItemReceiver;
import fr.utc.nf28.moka.ui.base.BasePagerFragment;
import fr.utc.nf28.moka.util.HttpHelper;
import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.MokaRestHelper;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class CurrentItemListFragment extends BasePagerFragment implements AdapterView.OnItemClickListener,
		RefreshItemReceiver.OnRefreshItemListener, Callback<Response> {
	private static final String DEFAULT_REST_SERVER_IP = "192.168.1.6"; //TODO same as Jade main container ? doublon in HistoryEntryListFragment
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
	private final IntentFilter mIntentFilter = new IntentFilter(MokaReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
	private CurrentItemAdapter mAdapter;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;
	/**
	 * Receiver
	 */
	private RefreshItemReceiver mRefreshItemReceiver;
	private MokaRestService mMokaRestService;
	private ListView mListView;
	private ProgressBar mProgressBar;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_current_item_list, container, false);

		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress);

		mListView = (ListView) rootView.findViewById(android.R.id.list);
		mListView.setOnItemClickListener(this);
		mListView.setEmptyView(rootView.findViewById(android.R.id.empty));
		mAdapter = new CurrentItemAdapter(getSherlockActivity());
		mListView.setAdapter(mAdapter);

		mRefreshItemReceiver = new RefreshItemReceiver(this);

		// Launch the background task to retrieve history entries from the RESTful server
		// TODO: refact with {@link HistoryEntryListFragment}
		final String API_URL = "http://" + PreferenceManager.getDefaultSharedPreferences(getSherlockActivity())
				.getString(SharedPreferencesUtils.KEY_PREF_IP, DEFAULT_REST_SERVER_IP) + "/api";
		mMokaRestService = MokaRestHelper.getMokaRestService(API_URL);

		return rootView;
	}

	@Override
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;

		super.onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshCurrentList();
		LocalBroadcastManager.getInstance(getSherlockActivity()).registerReceiver(mRefreshItemReceiver, mIntentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getSherlockActivity()).unregisterReceiver(mRefreshItemReceiver);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		mCallbacks.onItemSelected(mAdapter.getItem(position));
	}

	@Override
	public void onRefreshRequest() {
		refreshCurrentList();
	}

	/**
	 * get the current item list from the rest server
	 */
	private void refreshCurrentList() {
		mProgressBar.setVisibility(View.VISIBLE);
		mListView.getEmptyView().setVisibility(View.GONE);
		mMokaRestService.itemsEntries(String.valueOf(System.currentTimeMillis()), this);
	}

	private void resetUi() {
		mProgressBar.setVisibility(View.GONE);
		mListView.getEmptyView().setVisibility(View.VISIBLE);
	}

	@Override
	public void success(Response res, Response response) {
		new ParseCurrentItemListTask().execute(response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		Log.d(TAG, "REST call failure === " + retrofitError.toString());
		resetUi();
		handleNetworkError();
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

	final class ParseCurrentItemListTask extends AsyncTask<Response, Void, List<MokaItem>> {
		@Override
		protected List<MokaItem> doInBackground(Response... params) {
			try {
				final List<MokaItem> currentItems = JSONParserUtils.deserializeItemEntries(
						HttpHelper.convertStreamToString(params[0].getBody().in())
				);
				return currentItems;
			} catch (IOException e) {
				return Collections.emptyList();
			}
		}

		@Override
		protected void onPostExecute(List<MokaItem> currentItems) {
			resetUi();
			mAdapter.updateCurrentItems(currentItems);
		}
	}
}
