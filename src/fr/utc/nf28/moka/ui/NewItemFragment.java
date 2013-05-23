package fr.utc.nf28.moka.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(NewItemFragment.class);
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onValidate(MokaItem newItem) {
		}
	};
	private final MokaType mSelectedType;
	private Callbacks mCallbacks = sDummyCallbacks;

	public NewItemFragment(MokaType selectedType) {
		mSelectedType = selectedType;
	}

	public static NewItemFragment newInstance(MokaType selectedType) {
		return new NewItemFragment(selectedType);
	}

	// Fragment lifecycle management
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
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
		typeCategory.setText(mSelectedType.getCategoryName());
		typeImage.setImageResource(mSelectedType.getResId());

		return rootView;
	}

	@Override
	public void onDetach() {
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;

		super.onDetach();
	}

	public interface Callbacks {
		public void onValidate(MokaItem newItem);
	}
}
