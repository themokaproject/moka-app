package fr.utc.nf28.moka.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.utc.nf28.moka.R;

public class TutoCollectionFragment extends SherlockFragment {

	public static final String ARG_DRAWABLE = "src";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tuto_fragment_collection_object, container, false);
		Bundle args = getArguments();
		((ImageView) rootView.findViewById(R.id.tuto_image)).setImageResource(args.getInt(ARG_DRAWABLE));
		return rootView;
	}
}
