package fr.utc.nf28.moka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import fr.utc.nf28.moka.data.CurrentItem;
import fr.utc.nf28.moka.ui.CurrentElementListFragment;
import fr.utc.nf28.moka.ui.ElementListFragment;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener, CurrentElementListFragment.Callbacks {
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

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
	public void onItemSelected(CurrentItem currentItem) {
		final Intent detailIntent = new Intent(this, ItemDetailActivity.class);
		detailIntent.putExtra(ItemDetailActivity.ARG_ITEM, currentItem);
		startActivity(detailIntent);
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
			switch (position) {
				case 0:
					return new ElementListFragment();
				default:
					return new CurrentElementListFragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
