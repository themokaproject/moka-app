package fr.utc.nf28.moka;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.agent.IJadeServerReceiver;
import fr.utc.nf28.moka.agent.JadeServerReceiver;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.ui.CurrentItemListFragment;
import fr.utc.nf28.moka.ui.TypeListFragment;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener,
		TypeListFragment.Callbacks, CurrentItemListFragment.Callbacks, IJadeServerReceiver {
	private static final String TAG = makeLogTag(MainActivity.class);
	private static final int EDIT_ITEM_REQUEST = 0;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	/**
	 * broadcast receiver use to catch agent callback
	 */
	private JadeServerReceiver mJadeServerReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		//create new receiver
		mJadeServerReceiver = new JadeServerReceiver(this);

		// ActionBar setup
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);

		// ViewPager setup
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

		// We add our tabs
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_title_create))
				.setTabListener(this));

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_title_current))
				.setTabListener(this));

		new RetrieveIpTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		Crouton.cancelAllCroutons();

		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mJadeServerReceiver,
				new IntentFilter(JadeServerReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER));
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mJadeServerReceiver);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_licenses:
				startActivity(new Intent(this, LicensesActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTypeSelected(MokaType type) {
		final Intent detailIntent = new Intent(this, NewItemActivity.class);
		detailIntent.putExtra(NewItemActivity.ARG_TYPE, type);
		startActivity(detailIntent);
	}

	@Override
	public void onTypeLongClicked(MokaType type) {
		new AlertDialog.Builder(this)
				.setTitle(type.getName())
				.setMessage(type.getDescription())
				.show();
	}

	@Override
	public void onItemSelected(MokaItem item) {
		final Intent detailIntent = new Intent(this, ItemDetailActivity.class);
		detailIntent.putExtra(ItemDetailActivity.ARG_ITEM, item);
		startActivityForResult(detailIntent, EDIT_ITEM_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_ITEM_REQUEST) {
			if (resultCode == ItemDetailActivity.RESULT_DELETE) {
				Crouton.makeText(this, "L'élément a été correctement supprimé", CroutonUtils.INFO_MOKA_STYLE).show(); // TODO: fetch from strings
			}
		}
	}

	@Override
	public void onNewItem(String content) {
		Crouton.makeText(this, "From server : " + content, CroutonUtils.INFO_MOKA_STYLE).show();
	}

	/**
	 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private static class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			return position == 0 ? new TypeListFragment() : new CurrentItemListFragment();
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	private class RetrieveIpTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... voids) {
			final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			final int ip = wifiManager.getConnectionInfo().getIpAddress();

			return String.format(
					"%d.%d.%d.%d",
					(ip & 0xff),
					(ip >> 8 & 0xff),
					(ip >> 16 & 0xff),
					(ip >> 24 & 0xff)
			);
		}

		protected void onPostExecute(String ipString) {
			super.onPostExecute(ipString);

			final IAndroidAgent interfaceAgent = JadeUtils.getAndroidAgentInterface();
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			final String firstName = prefs.getString(SharedPreferencesUtils.KEY_PREF_FIRST_NAME, getString(R.string.unknown_firstname));
			final String lastName = prefs.getString(SharedPreferencesUtils.KEY_PREF_LAST_NAME, getString(R.string.unknown_lastname));
			interfaceAgent.connectPlatform(firstName, lastName, ipString);
		}
	}
}
