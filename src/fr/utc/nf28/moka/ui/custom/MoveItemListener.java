package fr.utc.nf28.moka.ui.custom;

import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public abstract class MoveItemListener implements View.OnTouchListener, SensorEventListener {
	private static final String TAG = makeLogTag(MoveItemListener.class);
	private static final int MOVE_NOISE = 20;
	private static final int RESIZE_NOISE = 20;
	private static final double ROTATE_DETECTION = 0.0015d;
	private static final double ROTATE_NOISE = 0.35d;
	private float mLastX = -1f;
	private float mLastY = -1f;
	private float mLastXDist = -1f;
	private float mLastYDist = -1f;
	private double mLastAngle = 999999d;
	private double mLastComputedAngle = 999999d;

	private final SensorManager mSensorManager;
	private final Sensor mAccelerometer;
	private float mLastXAxis = 99999f;
	private float mLastYAxis = 99999f;
	private static final float SENSOR_NOISE = 2.0f;
	private boolean isFirst = true;

	public MoveItemListener(SensorManager sm) {
		super();
		mSensorManager = sm;
		mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}


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

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if(mLastXAxis == 99999f || mLastYAxis == 99999f) {
			mLastXAxis = sensorEvent.values[0];
			mLastYAxis = sensorEvent.values[1];
		}else if(isFirst){
			mLastXAxis = sensorEvent.values[0];
			mLastYAxis = sensorEvent.values[1];
			isFirst = false;
		}else{
			final float dx = sensorEvent.values[0] - mLastXAxis;
			final float dy = sensorEvent.values[1] - mLastYAxis;
			int direction = computeDirection(dx, dy, SENSOR_NOISE);

			if (direction != 0) {
				if (direction % 10 != 0) mLastXAxis = sensorEvent.values[0];
				if (direction >= 10) mLastYAxis = sensorEvent.values[1];
				rotate(direction);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}


	private boolean twoPointers(final MotionEvent motionEvent) {
		resetSensor();
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
				mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
		resetSensor();
	}

	public void resetSensor(){
		isFirst = true;
		mLastXAxis = 99999f;
		mLastYAxis = 99999f;
		mSensorManager.unregisterListener(this);
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
		PointF point2 = makePointF(motionEvent, 1);
		PointF middle = computeMiddle(point1, point2);
		point1 = changeOrigin(point1, middle);
		point2 = changeOrigin(point2, middle);
		double angle = computeAngle(point1);
		boolean handle = false;

		//rotation
		if (mLastAngle > Math.PI || mLastComputedAngle > Math.PI || !(isAngleSwitchValid(mLastAngle, angle))) {
			mLastAngle = angle;
			mLastComputedAngle = angle;
		} else if (ROTATE_DETECTION < Math.abs(angle - mLastAngle)) {
			if (ROTATE_NOISE < Math.abs(angle - mLastComputedAngle)) {
				int direction = 0;
				if (angle < mLastAngle) {
					direction = 100;
				} else {
					direction = 200;
				}
				mLastComputedAngle = angle;
				rotate(direction);
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
			int direction = computeDirection(dx, dy, MOVE_NOISE);

			if (direction != 0) {
				resetSensor();
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
		if (((Math.abs(angle1) > 3) && (Math.abs(angle2) < 1))
				|| ((Math.abs(angle2) > 3) && (Math.abs(angle1) < 1))) {
			return false;
		} else {
			return true;
		}
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
		PointF middle = new PointF();
		middle.x = (point1.x + point2.x) / 2;
		middle.y = (point1.y + point2.y) / 2;
		return middle;
	}

	private PointF changeOrigin(final PointF oldCoordinates, final PointF newOrigin) {
		PointF newCoordinates = new PointF();
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
