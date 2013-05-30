package fr.utc.nf28.moka.io.receiver;


import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import fr.utc.nf28.moka.io.agent.A2ATransaction;
import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class CreationReceiver extends MokaReceiver {

	private OnCreationCallbackListener mInterface;

	/**
	 * constructor
	 *
	 * @param i listener
	 */
	public CreationReceiver(OnCreationCallbackListener i) {
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
					mInterface.onSuccess((Integer) request.getContent());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * interface
	 */
	public interface OnCreationCallbackListener {
		/**
		 * call when jade server inform Android device that a new item
		 * has been successfully created
		 *
		 * @param id item server id
		 */
		public void onSuccess(int id);

		/**
		 * call when creation on server side failed
		 */
		public void onError();
	}
}
