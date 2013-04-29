package fr.utc.nf28.moka;

import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import fr.utc.nf28.moka.data.CurrentItem;

public class ItemDetailActivity extends SherlockFragmentActivity {
	public static final String ARG_ITEM = "arg_item";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detail_activity);

		final CurrentItem item = getIntent().getExtras().getParcelable(ARG_ITEM);
		Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
	}
}
