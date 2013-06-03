package fr.utc.nf28.moka.ui.custom;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public abstract class MoveItemListener implements View.OnTouchListener {
	private static final String TAG = makeLogTag(MoveItemListener.class);
	private static final int MOVE_NOISE = 10;
	private static final int RESIZE_NOISE = 20;
	private float mLastX = -1f;
	private float mLastY = -1f;
	private float mLastXDist = -1f;
	private float mLastYDist = -1f;

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		final ViewParent parent = view.getParent();
		if (parent != null) {
			parent.requestDisallowInterceptTouchEvent(true);
		}
		final int pointerCount = motionEvent.getPointerCount();
		switch (pointerCount) {
			case 1:
				return onePointer(motionEvent);
			case 2:
				return twoPointers(motionEvent);
			default:
				return false;
		}
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
		final int action = motionEvent.getActionMasked();

		switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
				mLastXDist = Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
				mLastYDist = Math.abs(motionEvent.getY(0) - motionEvent.getY(1));
				break;

			case MotionEvent.ACTION_MOVE:
				if (mLastXDist == -1f || mLastYDist == -1f) {
					mLastXDist = Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
					mLastYDist = Math.abs(motionEvent.getY(0) - motionEvent.getY(1));
				} else {
					final float currentXDist = Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
					final float currentYDist = Math.abs(motionEvent.getY(0) - motionEvent.getY(1));
					final float dx = currentXDist - mLastXDist;
					final float dy = currentYDist - mLastYDist;
					int direction = computeDirection(dx, dy, RESIZE_NOISE);

					if (direction != 0) {
						if (direction % 10 != 0) mLastXDist = currentXDist;
						if (direction >= 10) mLastYDist = currentYDist;
						resize(direction);
					}
				}
				break;

			case MotionEvent.ACTION_POINTER_UP:
				reset();
				break;
		}

		return true;
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

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastX = motionEvent.getX();
				mLastY = motionEvent.getY();
				break;

			case MotionEvent.ACTION_MOVE:
				if (mLastX == -1f || mLastY == -1f) {
					mLastX = motionEvent.getX();
					mLastY = motionEvent.getY();
				} else {
					final float dx = motionEvent.getX() - mLastX;
					final float dy = motionEvent.getY() - mLastY;
					int direction = computeDirection(dx, dy, MOVE_NOISE);

					if (direction != 0) {
						if (direction % 10 != 0) mLastX = motionEvent.getX();
						if (direction >= 10) mLastY = motionEvent.getY();
						final int pseudoVelocity = (int) Math.max(Math.abs(dx / MOVE_NOISE), Math.abs(dy / MOVE_NOISE));
						move(direction, pseudoVelocity);
					}
				}
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				reset();
				break;
		}

		return true;
	}

	private void reset() {
		mLastXDist = -1f;
		mLastYDist = -1f;
		mLastX = -1f;
		mLastY = -1f;
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
