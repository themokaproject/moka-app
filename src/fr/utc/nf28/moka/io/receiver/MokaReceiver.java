package fr.utc.nf28.moka.io.receiver;

import android.content.BroadcastReceiver;

public abstract class MokaReceiver extends BroadcastReceiver {
	/**
	 * identify broadcast message
	 */
	public static final String INTENT_FILTER_JADE_SERVER_RECEIVER = "fr.utc.nf28.moka.io.agent.MokaReceiver";
	/**
	 * intent extra for callback content
	 */
	public static final String EXTRA_JADE_REQUEST = "requestFromSma";
}
