package fr.utc.nf28.moka.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * A helper class used to determine the current network state, i.e. online or not online
 */
public class ConnectionUtils {
	public static final Style NETWORK_ERROR_STYLE = Style.ALERT;

	public static boolean isOnline(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
}
