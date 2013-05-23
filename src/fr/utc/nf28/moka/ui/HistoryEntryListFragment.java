package fr.utc.nf28.moka.ui;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fr.utc.nf28.moka.HistoryItemAdapter;
import fr.utc.nf28.moka.MokaRestService;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.HistoryEntry;
import fr.utc.nf28.moka.data.MokaItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class HistoryEntryListFragment extends SherlockFragment {
	private static final String API_URL = "http://thomaskeunebroek.fr/moka";
	private static final String TAG = makeLogTag(HistoryEntryListFragment.class);
	private final MokaItem mSelectedItem;
	private HistoryItemAdapter mAdapter;
	private ListView mListView;
	private ProgressBar mProgressBar;

	public HistoryEntryListFragment(MokaItem selectedItem) {
		mSelectedItem = selectedItem;
	}

	public static HistoryEntryListFragment newInstance(MokaItem selectedItem) {
		return new HistoryEntryListFragment(selectedItem);
	}

	// Fragment lifecycle management

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_history_item_list, container, false);

		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress);

		mListView = (ListView) rootView.findViewById(android.R.id.list);
		mListView.setEmptyView(rootView.findViewById(android.R.id.empty));
		mAdapter = new HistoryItemAdapter(getSherlockActivity());
		mListView.setAdapter(mAdapter);

		// Launch the background task
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//new LoadItemHistoryTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			//new LoadItemHistoryTask(this).execute();
		}

		final RestAdapter restAdapter = new RestAdapter.Builder().setServer(API_URL).setDebug(true).build();
		final MokaRestService mokaRestService = restAdapter.create(MokaRestService.class);
		mokaRestService.historyEntries(mSelectedItem.getId(), new Callback<List<HistoryEntry>>() {
			@Override
			public void success(List<HistoryEntry> historyEntries, Response response) {
				Log.d(TAG, "success");
				mAdapter.updateHistoryItems(historyEntries);
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.d(TAG, "failure === " + retrofitError.toString());
			}
		});

		return rootView;
	}

	private void handleNetworkError() {
		Toast.makeText(getSherlockActivity(), "Erreur réseau", Toast.LENGTH_SHORT).show();
	}

	private static class LoadItemHistoryTask extends AsyncTask<Void, Void, List<HistoryEntry>> {
		private final WeakReference<HistoryEntryListFragment> mUiFragment;

		public LoadItemHistoryTask(HistoryEntryListFragment uiFragment) {
			super();

			mUiFragment = new WeakReference<HistoryEntryListFragment>(uiFragment);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			final HistoryEntryListFragment ui = mUiFragment.get();
			if (ui != null) {
				ui.mListView.getEmptyView().setVisibility(View.GONE);
				ui.mProgressBar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<HistoryEntry> doInBackground(Void... params) {
			final List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>(10);

			for (int i = 0; i < 10; i++) {
				//historyEntries.add(new HistoryEntry("history " + String.valueOf(10 - i)));
			}

			SystemClock.sleep(5000);
			return historyEntries;
		}

		@Override
		protected void onPostExecute(List<HistoryEntry> historyEntries) {
			super.onPostExecute(historyEntries);

			final HistoryEntryListFragment ui = mUiFragment.get();
			if (ui != null) {
				if (historyEntries == null) {
					ui.handleNetworkError();
				} else {
					ui.mAdapter.updateHistoryItems(historyEntries);
				}
				ui.mListView.getEmptyView().setVisibility(View.VISIBLE);
				ui.mProgressBar.setVisibility(View.GONE);
			}
		}
	}
}
