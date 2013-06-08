package fr.utc.nf28.moka.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.utc.nf28.moka.R;

public class TutoCollectionActivity extends SherlockFragmentActivity {

	private TutoCollectionPagerAdapter mTutoCollectionPagerAdapter;

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuto_activity_collection);

		mTutoCollectionPagerAdapter = new TutoCollectionPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager)findViewById(R.id.tuto_pager);
		mViewPager.setAdapter(mTutoCollectionPagerAdapter);
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
	 * representing an object in the collection.
	 */
	public static class TutoCollectionPagerAdapter extends FragmentStatePagerAdapter {

		public TutoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new TutoCollectionFragment();
			Bundle args = new Bundle();
			args.putInt(TutoCollectionFragment.ARG_IMG, i + 1); // Our object is just an integer :-P
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "OBJECT " + (position + 1);
		}
	}

}
