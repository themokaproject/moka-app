package fr.utc.nf28.moka.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ComputerType;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.io.agent.IJadeServerReceiver;
import fr.utc.nf28.moka.ui.base.MokaUpActivity;
import fr.utc.nf28.moka.util.CroutonUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemActivity extends MokaUpActivity implements IJadeServerReceiver{
	public static final String ARG_TYPE = "arg_type";
	private static final String TAG = makeLogTag(NewItemActivity.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_item_activity);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.new_item_actionbar_title));

		if (savedInstanceState == null & getIntent().hasExtra(ARG_TYPE)) {

			//retrieve MokaType from intent
			final MokaType type = (MokaType) getIntent().getExtras().getParcelable(ARG_TYPE);

			//add fragment
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.new_item_container, NewItemFragment.newInstance(type))
					.commit();

			//send creation request to the SMA
			final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
			if (type instanceof ComputerType.UmlType) {
				agent.createItem(ComputerType.UmlType.KEY_TYPE);
			} else if (type instanceof TextType.PostItType) {
				agent.createItem(TextType.PostItType.KEY_TYPE);
			} else {
				Crouton.makeText(this, "implémenter création pour " + type.getClass().toString(),
						CroutonUtils.INFO_MOKA_STYLE).show();
			}
		}
	}


	@Override
	public void onItemCreationSuccess(int id) {
		Crouton.makeText(this, "id from server :" + String.valueOf(id)+". Ready for edition.",
				Style.CONFIRM).show();
		//TODO enable edition
	}

	@Override
	public void onNewItemCreated() {

	}
}
