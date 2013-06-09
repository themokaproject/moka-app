package fr.utc.nf28.moka.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;

public class TutoCollectionActivity extends MokaUpActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuto_activity_collection);

		((ViewPager) findViewById(R.id.tuto_pager)).setAdapter(
				new TutoCollectionPagerAdapter(getSupportFragmentManager()));
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
	 * representing an object in the collection.
	 */
	private static class TutoCollectionPagerAdapter extends FragmentStatePagerAdapter {

		public TutoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			final Fragment fragment = new TutoCollectionFragment();
			final Bundle args = new Bundle();
			switch (position) {
				case 0:
					args.putInt(TutoCollectionFragment.ARG_DRAWABLE, R.drawable.tuto_connexion);
					break;
				case 1:
					args.putInt(TutoCollectionFragment.ARG_DRAWABLE, R.drawable.tuto_connexion_plateforme);
					break;
				case 2:
					args.putInt(TutoCollectionFragment.ARG_DRAWABLE, R.drawable.tuto_creation);
					break;
				case 3:
					args.putInt(TutoCollectionFragment.ARG_DRAWABLE, R.drawable.tuto_creation_details);
					break;
				default:
					args.putInt(TutoCollectionFragment.ARG_DRAWABLE, R.drawable.logo);
					break;
			}
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO: stringify
			switch (position) {
				case 0:
					return "Connexion";
				case 1:
					return "Apparition";
				case 2:
					return "Creation";
				case 3:
					return "Détails";
				case 4:
					return "Edition";
				case 5:
					return "Déplacement";
				default:
					return "tuto " + (position + 1);
			}
		}
	}

}
