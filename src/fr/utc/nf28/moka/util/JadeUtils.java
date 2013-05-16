package fr.utc.nf28.moka.util;


import android.util.Log;

import java.util.UUID;

import jade.android.MicroRuntimeService;
import jade.android.RuntimeCallback;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

/**
 * all utils for managing jade runtime and agents life cycle
 */
public class JadeUtils {
	/**
	 * Default port
	 */
	public static final int DEFAULT_PORT = 1099;
	/**
	 * Log TAG
	 */
	private static final String TAG = makeLogTag(JadeUtils.class);
	/**
	 * runtime use for jade
	 */
	private static MicroRuntimeService mRuntime = null;

	/**
	 * create jade container and connect to the main container
	 *
	 * @param ip   ip address of computer which host the main container
	 * @param port port for network communication, could use DEFAULT_PORT
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
						//start the unique agent of android device
						startAgent(UUID.randomUUID().toString(), "AndroidAgent.class", null);
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
		if (mRuntime != null)
			mRuntime.stopAgentContainer(new RuntimeCallback<Void>() {
				@Override
				public void onFailure(Throwable arg0) {
				}

				@Override
				public void onSuccess(Void arg0) {
					mRuntime = null;
				}
			});
	}
}
