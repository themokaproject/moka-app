package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaType;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(NewItemFragment.class);
	private final MokaType mSelectedType;

	public NewItemFragment(MokaType selectedType) {
		mSelectedType = selectedType;
	}

	public static NewItemFragment newInstance(MokaType selectedType) {
		return new NewItemFragment(selectedType);
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
		final View rootView = inflater.inflate(R.layout.fragment_new_item, container, false);

		final TextView typeName = (TextView) rootView.findViewById(R.id.type_name);
		final TextView typeDescription = (TextView) rootView.findViewById(R.id.type_description);
		final TextView typeCategory = (TextView) rootView.findViewById(R.id.type_category);
		final ImageView typeImage = (ImageView) rootView.findViewById(R.id.type_image);

		typeName.setText(mSelectedType.getName());
		typeDescription.setText(mSelectedType.getDescription());
		typeCategory.setText(mSelectedType.getTypeName());
		typeImage.setImageResource(mSelectedType.getResId());

		return rootView;
	}
}
