package fr.utc.nf28.moka.io.receiver;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import fr.utc.nf28.moka.io.agent.A2ATransaction;
import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class RefreshItemReceiver extends MokaReceiver {
	private final OnRefreshItemListener mInterface;

	public RefreshItemReceiver(OnRefreshItemListener i) {
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
				if (JadeUtils.TRANSACTION_TYPE_REFRESH_CURRENT_ITEMS.equals(type)) {
					mInterface.onRefreshRequest();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public interface OnRefreshItemListener {
		/**
		 * call when jade server request Android device to refresh
		 * current items list due to a new item creation
		 */
		public void onRefreshRequest();
	}
}
