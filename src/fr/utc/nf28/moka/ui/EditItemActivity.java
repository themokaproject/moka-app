package fr.utc.nf28.moka.ui;

import android.os.Bundle;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemActivity extends MokaUpActivity implements EditItemFragment.Callbacks {
	public static final String ARG_ITEM = "arg_item";
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 1;
	private static final String TAG = makeLogTag(EditItemActivity.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.edit_item_container, EditItemFragment.newInstance((MokaItem) getIntent().getExtras().getParcelable(ARG_ITEM)))
					.commit();
		}
	}

	@Override
	public void onItemDeletion(MokaItem item) {
		// TODO: call the DeleteAgent
		final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
		agent.deleteItem(item.getId());
		setResult(EditItemActivity.RESULT_DELETE);
		finish();
	}
}