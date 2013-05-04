package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.CurrentItem;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(EditItemFragment.class);
	private final CurrentItem mCurrentItem;

	public EditItemFragment(CurrentItem item) {
		mCurrentItem = item;
	}

	public static EditItemFragment newInstance(CurrentItem item) {
		return new EditItemFragment(item);
	}

	// Fragment lifecycle management
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fragment configuration
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		final TextView itemName = (TextView) rootView.findViewById(R.id.item_name);
		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemSource = (TextView) rootView.findViewById(R.id.item_source);
		final TextView itemCreator = (TextView) rootView.findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);

		itemName.setText(mCurrentItem.getName());
		itemType.setText(mCurrentItem.getName());
		itemSource.setText(mCurrentItem.getName());
		itemCreator.setText(mCurrentItem.getName());
		itemImage.setImageResource(R.drawable.logo);

		return rootView;
	}
}
