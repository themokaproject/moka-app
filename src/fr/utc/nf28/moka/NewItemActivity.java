package fr.utc.nf28.moka;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import fr.utc.nf28.moka.data.BaseItem;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemActivity extends SherlockFragmentActivity {
	public static final String ARG_ITEM = "arg_item";
	private static final String TAG = makeLogTag(NewItemActivity.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_item_activity);

		final BaseItem baseItem = getIntent().getExtras().getParcelable(ARG_ITEM);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.new_item_actionbar_title));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
