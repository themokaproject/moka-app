package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.ComputerItem;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class NewItemFragment extends SherlockFragment implements View.OnTouchListener {
	private static final String TAG = makeLogTag(NewItemFragment.class);
	private static final int MOVE_NOISE = 10;
	private final MokaType mSelectedType;
	private final IAndroidAgent mAgent = JadeUtils.getAndroidAgentInterface();
	private float mLastX = -1f;
	private float mLastY = -1f;
	private MokaItem mNewItem;

	public NewItemFragment(MokaType selectedType) {
		mSelectedType = selectedType;
	}

	public static NewItemFragment newInstance(MokaType selectedType) {
		return new NewItemFragment(selectedType);
	}

	// Fragment lifecycle management
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_new_item, container, false);

		final TextView typeName = (TextView) rootView.findViewById(R.id.type_name);
		final TextView typeDescription = (TextView) rootView.findViewById(R.id.type_description);
		final TextView typeCategory = (TextView) rootView.findViewById(R.id.type_category);
		final ImageView typeImage = (ImageView) rootView.findViewById(R.id.type_image);
		final SurfaceView canvasMoveItem = (SurfaceView) rootView.findViewById(R.id.canvas_move_item);

		typeName.setText(mSelectedType.getName());
		typeDescription.setText(mSelectedType.getDescription());
		typeCategory.setText(mSelectedType.getCategoryName());
		typeImage.setImageResource(mSelectedType.getResId());
		canvasMoveItem.setOnTouchListener(this);

		mNewItem = new ComputerItem.UmlItem("Diagramme UML"); // TODO: create accordingly to selected type

		return rootView;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		final int action = motionEvent.getAction();
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			Log.i(TAG, "action_up reset");
			mLastX = -1f;
			mLastY = -1f;
			return true;
		}

		final float currentX = motionEvent.getX();
		final float currentY = motionEvent.getY();

		if (mLastX == -1f || mLastY == -1f) {
			mLastX = currentX;
			mLastY = currentY;
		} else {
			final float dx = currentX - mLastX;
			final float dy = currentY - mLastY;
			int direction = 0;

			if (Math.abs(dx) >= MOVE_NOISE) {
				mLastX = currentX;
				if (dx >= 0) {
					//RIGHT
					direction += 1;
				} else {
					//LEFT
					direction += 2;
				}
			}

			if (Math.abs(dy) >= MOVE_NOISE) {
				mLastY = currentY;
				if (dy >= 0) {
					//BOTTOM
					direction += 10;
				} else {
					//TOP
					direction += 20;
				}
			}

			if (direction != 0) {
				final int pseudoVelocity = (int) Math.max(Math.abs(dx / MOVE_NOISE), Math.abs(dy / MOVE_NOISE));
				Log.i(TAG, "direction " + String.valueOf(direction) + " velocity " + String.valueOf(pseudoVelocity));
				mAgent.moveItem(mNewItem.getId(), direction, pseudoVelocity);
			}
		}

		return true;
	}
}
