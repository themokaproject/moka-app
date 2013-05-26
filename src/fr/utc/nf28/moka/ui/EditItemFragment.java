package fr.utc.nf28.moka.ui;

import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.util.DateUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemFragment extends SherlockFragment {
	private static final String TAG = makeLogTag(EditItemFragment.class);
	private static final int MOVE_NOISE = 10;
	private final MokaItem mSelectedItem;
	private float mLastX = -1f;
	private float mLastY = -1f;

	public EditItemFragment(MokaItem selectedItem) {
		mSelectedItem = selectedItem;
	}

	public static EditItemFragment newInstance(MokaItem selectedItem) {
		return new EditItemFragment(selectedItem);
	}

	// Fragment lifecycle management

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		final TextView itemName = (TextView) rootView.findViewById(R.id.item_name);
		final TextView itemType = (TextView) rootView.findViewById(R.id.item_type);
		final TextView itemCategory = (TextView) rootView.findViewById(R.id.item_category);
		final TextView itemCreator = (TextView) rootView.findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) rootView.findViewById(R.id.item_image);
		final SurfaceView surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView);
		surfaceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final float currentX = event.getX();
				final float currentY = event.getY();

				if(mLastX == -1 || mLastY == -1){
					mLastX = currentX;
					mLastY = currentY;
				}else{
					final float dx = currentX - mLastX;
					final float dy = currentY - mLastY;
					int direction = 0;

					if(Math.abs(dx) >= MOVE_NOISE){
						mLastX = currentX;
						if(dx >=0){
							//RIGHT
							direction += 1;
						}else{
							//LEFT
							direction += 2;
						}
					}

					if(Math.abs(dy) >= MOVE_NOISE){
						mLastY = currentY;
						if(dy >=0){
							//BOTTOM
							direction += 10;
						}else{
							//TOP
							direction += 20;
						}
					}

					if(direction != 0) {
						final int pseudoVelocity = (int)Math.max(Math.abs(dx/MOVE_NOISE), Math.abs(dy/MOVE_NOISE));
						Log.i(TAG, "direction " + String.valueOf(direction) + " velocity " + String.valueOf(pseudoVelocity));
						final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
						agent.moveItem(mSelectedItem.getId(), direction, pseudoVelocity);

					}
				}

				if(event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
					Log.i(TAG, "action_up reset");
					mLastX = -1;
					mLastY = -1;
				}

				return true;
			}
		});

		itemName.setText(mSelectedItem.getTitle());
		itemType.setText(mSelectedItem.getType().getName());
		itemCategory.setText(mSelectedItem.getType().getCategoryName());
		itemCreator.setText("Créé par " + mSelectedItem.getCreatorName() + " le " + DateUtils.getFormattedDate(mSelectedItem.getCreationDate()));
		// TODO: fetch values from strings.xml
		itemImage.setImageResource(mSelectedItem.getType().getResId());

		return rootView;
	}
}
