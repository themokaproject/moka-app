package fr.utc.nf28.moka.io.receiver;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import fr.utc.nf28.moka.io.agent.A2ATransaction;
import fr.utc.nf28.moka.util.JSONParserUtils;

public class LockingReceiver extends MokaReceiver {

	private OnLockingListener mInterface;

	public LockingReceiver(OnLockingListener i) {
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
//				if (JadeUtils.TRANSACTION_TYPE_ITEM_CREATION_SUCCESS.equals(type)) {
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * interface
	 */
	public interface OnLockingListener {
		/**
		 * called when locking succeeded
		 */
		public void onSuccess();

		/**
		 * called when item is already locked
		 *
		 * @param lockerName name of current locker
		 */
		public void onAlreadyLocked(String lockerName);

		/**
		 * called when locking failed
		 */
		public void onError();
	}
}
