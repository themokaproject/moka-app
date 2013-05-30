package fr.utc.nf28.moka.io.receiver;


import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import fr.utc.nf28.moka.io.agent.A2ATransaction;
import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class RefreshHistoryReceiver extends MokaReceiver {

	 private OnRefreshHistoryListener mInterface;

	public RefreshHistoryReceiver(OnRefreshHistoryListener i ){
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
				if (type.equals(JadeUtils.TRANSACTION_TYPE_REFRESH_CURRENT_ITEMS) || type.equals(JadeUtils.TRANSACTION_TYPE_REFRESH_HISTORY)) {
					mInterface.onRefreshRequest();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public interface OnRefreshHistoryListener {
		/**
		 * call when jade server request Android device to refresh history
		 */
		public void onRefreshRequest();
	}
}
