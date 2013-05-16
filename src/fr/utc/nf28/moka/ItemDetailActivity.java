package fr.utc.nf28.moka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.ui.EditItemFragment;
import fr.utc.nf28.moka.ui.HistoryEntryListFragment;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ItemDetailActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
	public static final String ARG_ITEM = "arg_item";
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 1;
	private static final String TAG = makeLogTag(ItemDetailActivity.class);
	private ViewPager mViewPager;
	private MokaItem mSelectedItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detail_activity);

		mSelectedItem = getIntent().getExtras().getParcelable(ARG_ITEM);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(mSelectedItem.getTitle());

		// ViewPager setup
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mSelectedItem));

		// We add our tabs
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_title_edit))
				.setTabListener(this));

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_title_history))
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_delete:
				final Resources resources = getResources();
				final StringBuilder sb = new StringBuilder();
				sb.append(resources.getString(R.string.delete_confirmation_message));
				sb.append(" ");
				sb.append(mSelectedItem.getTitle());
				sb.append(" ?");
				// TODO: use string variables
				new AlertDialog.Builder(this)
						.setTitle(resources.getString(R.string.delete_confirmation_title))
						.setMessage(sb.toString())
						.setPositiveButton(resources.getString(R.string.delete_confirmation_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO: call the DeleteAgent
								setResult(RESULT_DELETE);
								finish();
							}
						})
						.setNegativeButton(resources.getString(R.string.delete_confirmation_cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
							}
						})
						.show();
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

	/**
	 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private static class SectionsPagerAdapter extends FragmentPagerAdapter {
		private final MokaItem mSelectedItem;

		public SectionsPagerAdapter(FragmentManager fm, MokaItem selectedItem) {
			super(fm);

			mSelectedItem = selectedItem;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			switch (position) {
				case 0:
					return new EditItemFragment(mSelectedItem);
				default:
					return new HistoryEntryListFragment(mSelectedItem);
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}