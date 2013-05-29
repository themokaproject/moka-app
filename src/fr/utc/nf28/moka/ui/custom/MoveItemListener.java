package fr.utc.nf28.moka.ui.custom;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public abstract class MoveItemListener implements View.OnTouchListener {
	private static final String TAG = makeLogTag(MoveItemListener.class);
	private static final int MOVE_NOISE = 10;
	private static final int RESIZE_NOISE = 10;
	private float mLastX = -1f;
	private float mLastY = -1f;
	private float mLastXDist = -1f;
	private float mLastYDist = -1f;

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {

		if (motionEvent.getPointerCount() == 1) {
			return onePointer(motionEvent);
		} else if (motionEvent.getPointerCount() == 2) {
			return twoPointers(motionEvent);
		}

		return false;
	}

	/*
	*	RESIZE
	*	DIRECTION EXPLANATION
	*
	*	2X means DECREASE HEIGHT
	*	1X means INCREASE HEIGHT
	*	X1 means INCREASE WIDTH
	*	X2 means DECREASE WIDTH
	*
 	*/
	private boolean twoPointers(final MotionEvent motionEvent) {
		final float currentXDist = Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
		final float currentYDist = Math.abs(motionEvent.getY(0) - motionEvent.getY(1));

		if (mLastXDist == -1f && mLastYDist == -1f) {
			mLastXDist = currentXDist;
			mLastYDist = currentYDist;
			return true;
		} else {
			final float dx = currentXDist - mLastXDist;
			final float dy = currentYDist - mLastYDist;
			int direction = computeDirection(dx, dy, RESIZE_NOISE);

			if (direction != 0) {
				if (direction % 10 != 0) mLastXDist = currentXDist;
				if (direction >= 10) mLastYDist = currentYDist;
				Log.i(TAG, "resize || direction : " + String.valueOf(direction));
				resize(direction);
			}
		}

		return true;
	}

	private void reset() {
		mLastXDist = -1f;
		mLastYDist = -1f;
		mLastX = -1f;
		mLastY = -1f;
	}

	/*
	*	MOVE
	*	DIRECTION EXPLANATION
	*
	* 		22	20	21
	*
	* 		2	0	1
	*
	* 		12	10	11
	*
	*	2X means TOP
	*	1X means BOTTOM
	*	X1 means right
	*	X2 means left
	*
 	*/
	private boolean onePointer(final MotionEvent motionEvent) {
		final int action = motionEvent.getAction();
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			Log.i(TAG, "action_up reset");
			reset();
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
			int direction = computeDirection(dx, dy, MOVE_NOISE);

			if (direction != 0) {
				if (direction % 10 != 0) mLastX = currentX;
				if (direction >= 10) mLastY = currentY;
				final int pseudoVelocity = (int) Math.max(Math.abs(dx / MOVE_NOISE), Math.abs(dy / MOVE_NOISE));
				move(direction, pseudoVelocity);
			}
		}

		return true;
	}

	private int computeDirection(float dx, float dy, int noise) {
		int direction = 0;

		if (Math.abs(dx) >= noise) {
			if (dx >= 0) {
				direction += 1;
			} else {
				direction += 2;
			}
		}

		if (Math.abs(dy) >= noise) {
			if (dy >= 0) {
				direction += 10;
			} else {
				direction += 20;
			}
		}

		return direction;
	}

	public abstract void move(int direction, int velocity);
	public abstract void resize(int direction);
}
