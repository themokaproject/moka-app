package fr.utc.nf28.moka.util;

import android.content.Context;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class HttpHelper {
	private static String sApiUrl;

	public static String convertStreamToString(InputStream is) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static String getMokaApiUrl(Context context) {
		if (sApiUrl == null) {
			sApiUrl = new StringBuilder()
					.append("http://")
					.append(PreferenceManager.getDefaultSharedPreferences(context)
							.getString(SharedPreferencesUtils.KEY_PREF_IP, ""))
					.append("/api")
					.toString();
		}
		return sApiUrl;
		// TODO: use real ip adress
	}
}
