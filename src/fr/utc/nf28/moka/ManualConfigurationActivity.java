package fr.utc.nf28.moka;

import android.*;
import android.R;
import android.app.Activity;
import android.os.Bundle;
import fr.utc.nf28.moka.ui.ConfigurationFragment;
import fr.utc.nf28.moka.util.LogUtils;

public class ManualConfigurationActivity extends Activity {
	/**
	 * Tag for log cat
	 */
	private static final String TAG = LogUtils.makeLogTag(ManualConfigurationActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//display fragment as main content
		getFragmentManager().beginTransaction().replace(R.id.content,new ConfigurationFragment()).commit();
	}
}
