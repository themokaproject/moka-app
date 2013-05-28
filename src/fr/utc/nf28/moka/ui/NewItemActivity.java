package fr.utc.nf28.moka.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemActivity extends MokaUpActivity {
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

			final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
			//TODO dynamic get the right item type
			agent.createItem("umlClass");
		}
	}
}
