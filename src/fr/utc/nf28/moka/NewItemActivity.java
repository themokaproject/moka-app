package fr.utc.nf28.moka;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.ui.NewItemFragment;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemActivity extends SherlockFragmentActivity {
	public static final String ARG_TYPE = "arg_type";
	private static final String TAG = makeLogTag(NewItemActivity.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_item_activity);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.new_item_actionbar_title));

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.new_item_container, NewItemFragment.newInstance((MokaType) getIntent().getExtras().getParcelable(ARG_TYPE)))
					.commit();
		}

		// test dynamic communication
		final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
		if (agent != null) {
			agent.createItem();
		}
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
