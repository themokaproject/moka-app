package fr.utc.nf28.moka;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import fr.utc.nf28.moka.data.CurrentItem;

public class ItemDetailActivity extends SherlockFragmentActivity {
	public static final String ARG_ITEM = "arg_item";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detail_activity);

		final CurrentItem item = getIntent().getExtras().getParcelable(ARG_ITEM);
		getSupportActionBar().setTitle(item.getName());
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
