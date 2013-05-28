package fr.utc.nf28.moka.io.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class JadeServerReceiver extends BroadcastReceiver {

	/**
	 * identify broadcast message
	 */
	public static final String INTENT_FILTER_JADE_SERVER_RECEIVER = "fr.utc.nf28.moka.io.agent.JadeServerReceiver";
	/**
	 * intent extra for callback content
	 */
	public static final String EXTRA_JADE_REQUEST = "requestFromSma";

	private final IJadeServerReceiver mInterface;

	/**
	 * constructor
	 *
	 * @param i interface to getCallback
	 */
	public JadeServerReceiver(IJadeServerReceiver i) {
		super();
		mInterface = i;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(EXTRA_JADE_REQUEST)) {
			try {
				final A2ATransaction request =
						JSONParserUtils.deserializeA2ATransaction(intent.getStringExtra(EXTRA_JADE_REQUEST));
				final String type = request.getType();
				if (type.equals(JadeUtils.TRANSACTION_TYPE_ITEM_CREATION_SUCCESS)) {
					mInterface.onItemCreationSuccess((Integer)request.getContent());
				} else if (type.equals(JadeUtils.TRANSACTION_TYPE_REFRESH_CURRENT_ITEMS)) {
					mInterface.onNewItemCreated();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
