package fr.utc.nf28.moka.util;

import android.util.Log;
import jade.android.MicroRuntimeService;
import jade.android.RuntimeCallback;

import java.util.UUID;

/**
 * all utils for managing jade runtime and agents life cycle
 */
public class JadeUtils {

	/**
	 * Log TAG
	 */
	private static final String TAG = LogUtils.makeLogTag(JadeUtils.class);

	/**
	 * Default port
	 */
	public static final int DEFAULT_PORT = 1099;

	/**
	 * runtime use for jade
	 */
	private static MicroRuntimeService mRuntime = null;

	/**
	 *  check if runtime has successfully started
	 */
	private static boolean mRuntimeStarted = false;

	/**
	 * create jade container and connect to the main container
	 *
	 * @param ip   ip address of computer which host the main container
	 * @param port port for network communication
	 */
	public static void createContainer(String ip, int port) {
		if (mRuntime == null) {
			mRuntime = new MicroRuntimeService();
		}
		mRuntime.startAgentContainer(ip, port,
				new RuntimeCallback<Void>() {
					@Override
					public void onFailure(Throwable arg0) {
						// Connection error
					}

					@Override
					public void onSuccess(Void arg0) {
						// Connection success
						Log.i(TAG, "container created ! ");
						mRuntimeStarted = true;
						//start the unique agent of android device
						startAgent(UUID.randomUUID().toString(), "AndroidAgent.class",null);
					}
				}
		);
	}

	/**
	 * start a new agent,container has to be started
	 *
	 * @param name       name to identify agent which has to be unique
	 * @param agentClass class of your agent
	 */
	private static void startAgent(final String name, final String agentClass) {
		if(!mRuntimeStarted){
			//connection to the main container failed
			return;
		}
		mRuntime.startAgent(name, agentClass, null,
				new RuntimeCallback<Void>() {
					@Override
					public void onFailure(Throwable arg0) {
					}

					@Override
					public void onSuccess(Void arg0) {
						Log.i(TAG, "agent " + name + " : " + agentClass + " started ! ");
					}
				}
		);
	}

	/**
	 * start a new agent,container has to be started
	 *
	 * @param name       name to identify agent which has to be unique
	 * @param agentClass class of your agent
	 * @param params     Array of object which will be retrieved by your agent
	 * @return true onSuccess, false onError see log for error type
	 */
	private static void startAgent(final String name, final String agentClass, final Object[] params) {
		mRuntime.startAgent(name, agentClass, params,
				new RuntimeCallback<Void>() {
					@Override
					public void onFailure(Throwable arg0) {
					}

					@Override
					public void onSuccess(Void arg0) {
						Log.i(TAG, "agent " + name + " : " + agentClass + " started ! ");
					}
				}
		);
	}

	/**
	 * end the whole jade session
	 * Stop agent container
	 */
	public static void close() {

	}
}
