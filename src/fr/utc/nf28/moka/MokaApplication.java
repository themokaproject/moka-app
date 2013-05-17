package fr.utc.nf28.moka;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import fr.utc.nf28.moka.agent.AndroidAgent;
import fr.utc.nf28.moka.data.ComputerType;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;
import fr.utc.nf28.moka.util.JadeUtils;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.Profile;
import jade.util.leap.Properties;

import java.util.HashMap;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class MokaApplication extends Application {
	public static HashMap<String, MokaType> MOKA_TYPES;

	/**
	 * Log for Logcat
	 */
	private static final String TAG = makeLogTag(DeviceConfigurationActivity.class);
	/**
	 * JADE
	 */
	private MicroRuntimeServiceBinder mMicroRuntimeServiceBinder;
	private Properties mAgentContainerProperties;
	private ServiceConnection mServiceConnection;
	private RuntimeCallback<Void> mContainerCallback;

	@Override
	public void onCreate() {
		super.onCreate();

		MOKA_TYPES = new HashMap<String, MokaType>() {
			{
				put(MediaType.ImageType.KEY_TYPE, new MediaType.ImageType("Image", "Description d'une image"));
				put(MediaType.VideoType.KEY_TYPE, new MediaType.VideoType("Vidéo", "Description d'une vidéo"));
				put(MediaType.WebType.KEY_TYPE, new MediaType.WebType("Page web", "Description d'une page web"));
				put(TextType.PlainTextType.KEY_TYPE, new TextType.PlainTextType("Texte", "Description d'un texte"));
				put(TextType.ListType.KEY_TYPE, new TextType.ListType("Liste", "Description d'une liste"));
				put(TextType.PostItType.KEY_TYPE, new TextType.PostItType("Post-it", "Description d'un post-it"));
				put(ComputerType.UmlType.KEY_TYPE, new ComputerType.UmlType("Diagramme UML", "Description d'un diagramme UML"));
			}
		};

		mAgentContainerProperties = new Properties();
		mAgentContainerProperties.setProperty(Profile.JVM, Profile.ANDROID);
	}

	/**
	 * * Start agent plateform
	 *
	 * @param host              adress ip of mainContainer Machine
	 * @param port              port to reach mainContainer Machine
	 * @param containerCallback callback for container connection
	 */
	public void startJadePlatform(final String host, final int port, RuntimeCallback<Void> containerCallback) {
		Log.i(TAG, "start jade platform");
		mAgentContainerProperties.setProperty(Profile.MAIN_HOST, host);
		mAgentContainerProperties.setProperty(Profile.MAIN_PORT, String.valueOf(port));
		mContainerCallback = containerCallback;
		bindMicroRuntimeService();
	}

	/**
	 * JadeAndroid good practices for jade runtime
	 */
	private void bindMicroRuntimeService() {
		Log.i(TAG, "bind micro runtime");
		mServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className, IBinder service) {
				// Bind successful
				Log.i(TAG, "bind micro runtime success");
				mMicroRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
				startAgentContainer();
			}

			public void onServiceDisconnected(ComponentName className) {
				// Bind unsuccessful
				Log.i(TAG, "bind micro runtime fail");
				mMicroRuntimeServiceBinder = null;
			}
		};

		bindService(new Intent(getApplicationContext(), MicroRuntimeService.class),
				mServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * start JADE Agent container
	 */
	private void startAgentContainer() {
		Log.i(TAG, "start agent container");
		mMicroRuntimeServiceBinder.startAgentContainer(mAgentContainerProperties,
				mContainerCallback);
	}

	/**
	 * start a JADE Agent
	 *
	 * @param nickName      agent name, must be unique
	 * @param className     agent class
	 * @param params        params which can be retrieved by the agent in setup()
	 * @param agentCallback callback for agent creation
	 */
	public void startAgent(final String nickName, final String className, Object[] params, RuntimeCallback<Void> agentCallback) {
		Log.i(TAG, "start agent " + nickName);
		mMicroRuntimeServiceBinder.startAgent(nickName, className, params,
				agentCallback);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		//TODO container and agent still visible in jade plateform
		if (mServiceConnection != null) {
			unbindService(mServiceConnection);
		}
	}
}
