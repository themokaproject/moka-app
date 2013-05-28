package fr.utc.nf28.moka.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.ComputerItem;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import fr.utc.nf28.moka.ui.custom.MoveItemListener;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(NewItemFragment.class);
	private final MokaType mSelectedType;
	private final IAndroidAgent mAgent = JadeUtils.getAndroidAgentInterface();
	private MokaItem mNewItem;

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
		final SurfaceView canvasMoveItem = (SurfaceView) rootView.findViewById(R.id.canvas_move_item);

		typeName.setText(mSelectedType.getName());
		typeDescription.setText(mSelectedType.getDescription()); // TODO: remove description
		typeCategory.setText(mSelectedType.getCategoryName());
		typeImage.setImageResource(mSelectedType.getResId());
		canvasMoveItem.setOnTouchListener(new MoveItemListener() {
			@Override
			public void move(int direction, int velocity) {
				mAgent.moveItem(mNewItem.getId(), direction, velocity);
			}
		});

		mNewItem = new ComputerItem.UmlItem("Diagramme UML"); // TODO: create accordingly to selected type

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_new_item, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete:
				final Resources resources = getResources();
				final StringBuilder sb = new StringBuilder();
				sb.append(resources.getString(R.string.delete_confirmation_message));
				sb.append(" ");
				sb.append(mNewItem.getTitle());
				sb.append(" ?");
				// TODO: use string variables
				new AlertDialog.Builder(getSherlockActivity())
						.setTitle(resources.getString(R.string.delete_confirmation_title))
						.setMessage(sb.toString())
						.setPositiveButton(resources.getString(R.string.delete_confirmation_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO: call the DeleteAgent
								getSherlockActivity().finish();
							}
						})
						.setNegativeButton(resources.getString(R.string.delete_confirmation_cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
							}
						})
						.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
