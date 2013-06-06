package fr.utc.nf28.moka.ui.custom;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public abstract class MoveItemListener implements View.OnTouchListener {
	private static final String TAG = makeLogTag(MoveItemListener.class);
	private static final int MOVE_NOISE = 20;
	private static final int RESIZE_NOISE = 25;
	private static final double ROTATE_DETECTION = 0.0010d;
	private static final double ROTATE_NOISE = 0.25d;
	private float mLastX = -1f;
	private float mLastY = -1f;
	private float mLastXDist = -1f;
	private float mLastYDist = -1f;
	private double mLastAngle = 999999d;
	private double mLastComputedAngle = 999999d;
	private View mCanvas;
	private boolean animate = true;

	public MoveItemListener() {
		super();
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		view.getParent().requestDisallowInterceptTouchEvent(true);
		mCanvas = view;
		final int pointerCount = motionEvent.getPointerCount();
		switch (pointerCount) {
			case 1:
				ObjectAnimator.ofFloat(view, "alpha", 1f).start();
				animate = false;
				return onePointer(motionEvent);
			case 2:
				if (animate) {
					ViewHelper.setAlpha(view, 1f);
				}
				return twoPointers(motionEvent);
			default:
				return false;
		}
	}

	private boolean twoPointers(final MotionEvent motionEvent) {
		final int action = motionEvent.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_MOVE:
				if (!handleRotation(motionEvent)) {
					handleResizing(motionEvent);
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				reset();
				break;
		}
		return true;
	}

	private boolean onePointer(final MotionEvent motionEvent) {
		final int action = motionEvent.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				handleMovement(motionEvent);
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
		mLastAngle = 99999d;
		mLastComputedAngle = 99999d;
		ObjectAnimator.ofFloat(mCanvas, "alpha", 0.6f).start();
		mCanvas = null;
		animate = true;
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
	private boolean handleResizing(final MotionEvent motionEvent) {
		final float currentXDist = computeDistance(motionEvent.getX(0), motionEvent.getX(1));
		final float currentYDist = computeDistance(motionEvent.getY(0), motionEvent.getY(1));
		boolean handle = false;
		if (mLastXDist == -1f || mLastYDist == -1f) {
			mLastXDist = currentXDist;
			mLastYDist = currentYDist;
		} else {
			final float dx = currentXDist - mLastXDist;
			final float dy = currentYDist - mLastYDist;
			int direction = computeDirection(dx, dy, RESIZE_NOISE);

			if (direction != 0) {
				if (direction % 10 != 0) mLastXDist = currentXDist;
				if (direction >= 10) mLastYDist = currentYDist;
				resize(direction);
				handle = true;
			}
		}
		return handle;
	}

	private boolean handleRotation(final MotionEvent motionEvent) {
		PointF point1 = makePointF(motionEvent, 0);
		final PointF point2 = makePointF(motionEvent, 1);
		final PointF middle = computeMiddle(point1, point2);
		point1 = changeOrigin(point1, middle);
		double angle = computeAngle(point1);
		boolean handle = false;

		//rotation
		if (mLastAngle > Math.PI || mLastComputedAngle > Math.PI || !(isAngleSwitchValid(mLastAngle, angle))) {
			mLastAngle = angle;
			mLastComputedAngle = angle;
		} else if (ROTATE_DETECTION < Math.abs(angle - mLastAngle)) {
			if (ROTATE_NOISE < Math.abs(angle - mLastComputedAngle)) {
				int direction;
				if (angle < mLastAngle) {
					direction = 100;
				} else {
					direction = 200;
				}
				mLastComputedAngle = angle;
				rotate(direction);
				mLastXDist = computeDistance(motionEvent.getX(0), motionEvent.getX(1));
				mLastYDist = computeDistance(motionEvent.getY(0), motionEvent.getY(1));
			}

			mLastAngle = angle;
			handle = true;
		}

		return handle;
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
	private boolean handleMovement(final MotionEvent motionEvent) {
		boolean handle = false;
		if (mLastX == -1f || mLastY == -1f) {
			mLastX = motionEvent.getX();
			mLastY = motionEvent.getY();
		} else {
			final float dx = motionEvent.getX() - mLastX;
			final float dy = motionEvent.getY() - mLastY;
			final int direction = computeDirection(dx, dy, MOVE_NOISE);

			if (direction != 0) {
				if (direction % 10 != 0) mLastX = motionEvent.getX();
				if (direction >= 10) mLastY = motionEvent.getY();
				final int pseudoVelocity = (int) Math.max(Math.abs(dx / MOVE_NOISE), Math.abs(dy / MOVE_NOISE));
				move(direction, pseudoVelocity);
				handle = true;
			}
		}
		return handle;
	}

	private boolean isAngleSwitchValid(double angle1, double angle2) {
		return !(((Math.abs(angle1) > 3) && (Math.abs(angle2) < 1))
				|| ((Math.abs(angle2) > 3) && (Math.abs(angle1) < 1)));
	}

	private PointF makePointF(final MotionEvent motionEvent, int pointerId) {
		return new PointF(motionEvent.getX(pointerId), motionEvent.getY(pointerId));
	}

	private double computeAngle(final PointF point) {
		double angle = 0;
		if (point.x == 0 && point.y > 0) {
			angle = Math.PI / 2;
		} else if (point.x == 0 && point.y < 0) {
			angle = -Math.PI / 2;
		} else if (point.x > 0) {
			angle = Math.atan(point.y / point.x);
		} else if (point.x < 0 && point.y < 0) {
			angle = Math.atan(point.y / point.x) - Math.PI;
		} else if (point.x < 0 && point.y >= 0) {
			angle = Math.atan(point.y / point.x) + Math.PI;
		}
		return angle;
	}

	private PointF computeMiddle(final PointF point1, final PointF point2) {
		final PointF middle = new PointF();
		middle.x = (point1.x + point2.x) / 2;
		middle.y = (point1.y + point2.y) / 2;
		return middle;
	}

	private PointF changeOrigin(final PointF oldCoordinates, final PointF newOrigin) {
		final PointF newCoordinates = new PointF();
		newCoordinates.x = oldCoordinates.x - newOrigin.x;
		newCoordinates.y = oldCoordinates.y - newOrigin.y;
		return newCoordinates;
	}

	private float computeDistance(float p1, float p2) {
		return Math.abs(p1 - p2);
	}

	private int computeDirection(float dx, float dy, float noise) {
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

	public abstract void rotate(int direction);
}
