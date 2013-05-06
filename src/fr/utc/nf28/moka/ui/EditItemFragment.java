package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(EditItemFragment.class);
	private final MokaItem mSelectedItem;

	public EditItemFragment(MokaItem selectedItem) {
		mSelectedItem = selectedItem;
	}

	public static EditItemFragment newInstance(MokaItem selectedItem) {
		return new EditItemFragment(selectedItem);
	}

	// Fragment lifecycle management
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);

		Toast.makeText(getSherlockActivity(), "hey dude, I'm a " + mSelectedItem.getType().getName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		final TextView itemName = (TextView) rootView.findViewById(R.id.item_name);
		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemSource = (TextView) rootView.findViewById(R.id.item_source);
		final TextView itemCreator = (TextView) rootView.findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);

		itemName.setText(mSelectedItem.getTitle());
		itemType.setText(mSelectedItem.getTitle());
		itemSource.setText(mSelectedItem.getTitle());
		itemCreator.setText(mSelectedItem.getTitle());
		itemImage.setImageResource(R.drawable.logo);

		return rootView;
	}
}
