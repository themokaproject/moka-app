package fr.utc.nf28.moka;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;

import java.util.HashMap;

import fr.utc.nf28.moka.data.ComputerType;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.Profile;
import jade.util.leap.Properties;

import static fr.utc.nf28.moka.util.LogUtils.LOGE;
import static fr.utc.nf28.moka.util.LogUtils.LOGI;
import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class MokaApplication extends Application {
	/**
	 * Log for Logcat
	 */
	private static final String TAG = makeLogTag(MokaApplication.class);
	public static HashMap<String, MokaType> MOKA_TYPES;
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

		final Resources resources = getResources();

		MediaType.setTypeName(resources.getString(R.string.type_category_media));
		TextType.setTypeName(resources.getString(R.string.type_category_text));
		ComputerType.setTypeName(resources.getString(R.string.type_category_computer));

		MOKA_TYPES = new HashMap<String, MokaType>() {
			{
				put(MediaType.ImageType.KEY_TYPE, new MediaType.ImageType(
						resources.getString(R.string.image_type_title),
						resources.getString(R.string.image_type_description))
				);
				put(MediaType.VideoType.KEY_TYPE, new MediaType.VideoType(
						resources.getString(R.string.video_type_title),
						resources.getString(R.string.video_type_description))
				);
				put(MediaType.WebType.KEY_TYPE, new MediaType.WebType(
						resources.getString(R.string.web_page_type_title),
						resources.getString(R.string.web_page_type_description))
				);
				put(TextType.PlainTextType.KEY_TYPE, new TextType.PlainTextType(
						resources.getString(R.string.plain_text_type_title),
						resources.getString(R.string.plain_text_type_description))
				);
				put(TextType.ListType.KEY_TYPE, new TextType.ListType(
						resources.getString(R.string.list_type_title),
						resources.getString(R.string.list_type_description))
				);
				put(TextType.PostItType.KEY_TYPE, new TextType.PostItType(
						resources.getString(R.string.post_it_type_title),
						resources.getString(R.string.post_it_type_description))
				);
				put(ComputerType.UmlType.KEY_TYPE, new ComputerType.UmlType(
						resources.getString(R.string.uml_type_title),
						resources.getString(R.string.uml_type_description))
				);
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
		LOGI(TAG, "start jade platform");
		mAgentContainerProperties.setProperty(Profile.MAIN_HOST, host);
		mAgentContainerProperties.setProperty(Profile.MAIN_PORT, String.valueOf(port));
		mContainerCallback = containerCallback;
		bindMicroRuntimeService();
	}

	/**
	 * JadeAndroid good practices for jade runtime
	 */
	private void bindMicroRuntimeService() {
		LOGI(TAG, "bind micro runtime");
		mServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className, IBinder service) {
				// Bind successful
				LOGI(TAG, "bind micro runtime success");
				mMicroRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
				startAgentContainer();
			}

			public void onServiceDisconnected(ComponentName className) {
				// Bind unsuccessful
				LOGE(TAG, "bind micro runtime fail");
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
		LOGI(TAG, "start agent container");
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
		LOGI(TAG, "start agent " + nickName);
		mMicroRuntimeServiceBinder.startAgent(nickName, className, params,
				agentCallback);
	}

	private void cleanup() {
		// TODO: find a way to call cleanup() (normally in the Service#onDestroy())
		if (mMicroRuntimeServiceBinder != null) {
			mMicroRuntimeServiceBinder.stopAgentContainer(mContainerCallback);
		}
		if (mServiceConnection != null) {
			unbindService(mServiceConnection);
		}
	}
}
