package fr.utc.nf28.moka.ui.custom;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public abstract class MoveItemListener implements View.OnTouchListener {
	private static final String TAG = makeLogTag(MoveItemListener.class);
	private static final int MOVE_NOISE = 10;
	private float mLastX = -1f;
	private float mLastY = -1f;

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
				move(direction, pseudoVelocity);
			}
		}

		return true;
	}

	public abstract void move(int direction, int velocity);
}
