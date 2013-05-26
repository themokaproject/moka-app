package fr.utc.nf28.moka.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JadeServerReceiver extends BroadcastReceiver {

	/**
	 * identify broadcast message
	 */
	public static final String INTENT_FILTER_JADE_SERVER_RECEIVER = "fr.utc.nf28.moka.agent.JadeServerReceiver";

	/**
	 * intent extra for callback content
	 */
	public static final String EXTRA_JADE_SERVER_MESSAGE = "callbackContentFromJadeServer";

	private IJadeServerReceiver mInterface;

	/**
	 * constructor
	 * @param i interface to getCallback
	 */
	public JadeServerReceiver(IJadeServerReceiver i) {
		super();
		mInterface = i;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(EXTRA_JADE_SERVER_MESSAGE)) {
			//TODO check if it's a item creation or another callback
			mInterface.onNewItem();
		}

	}
}
