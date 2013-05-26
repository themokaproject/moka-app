package fr.utc.nf28.moka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.utc.nf28.moka.agent.IAndroidAgent;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.util.DateUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class EditItemActivity extends SherlockFragmentActivity {
	public static final String ARG_ITEM = "arg_item";
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 1;
	private static final String TAG = makeLogTag(ItemDetailActivity.class);
	private static final int MOVE_NOISE = 10;
	private MokaItem mSelectedItem;
	private float mLastX = -1f;
	private float mLastY = -1f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_edit_item);

		mSelectedItem = getIntent().getExtras().getParcelable(ARG_ITEM);

		if (mSelectedItem == null) {
			throw new IllegalStateException("Selected item cannot be null");
		}

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(mSelectedItem.getTitle());

		final TextView itemName = (TextView) findViewById(R.id.item_name);
		final TextView itemType = (TextView) findViewById(R.id.item_type);
		final TextView itemCategory = (TextView) findViewById(R.id.item_category);
		final TextView itemCreator = (TextView) findViewById(R.id.item_creator);
		final ImageView itemImage = (ImageView) findViewById(R.id.item_image);
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
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


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_delete:
				final Resources resources = getResources();
				final StringBuilder sb = new StringBuilder();
				sb.append(resources.getString(R.string.delete_confirmation_message));
				sb.append(" ");
				sb.append(mSelectedItem.getTitle());
				sb.append(" ?");
				// TODO: use string variables
				new AlertDialog.Builder(this)
						.setTitle(resources.getString(R.string.delete_confirmation_title))
						.setMessage(sb.toString())
						.setPositiveButton(resources.getString(R.string.delete_confirmation_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO: call the DeleteAgent
								final IAndroidAgent agent = JadeUtils.getAndroidAgentInterface();
								agent.deleteItem(mSelectedItem.getId());
								setResult(RESULT_DELETE);
								finish();
							}
						})
						.setNegativeButton(resources.getString(R.string.delete_confirmation_cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
							}
						})
						.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
