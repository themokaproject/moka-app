package fr.utc.nf28.moka;

import retrofit.RestAdapter;

public class MokaRestAdapter {
	private static RestAdapter sInstance = null;

	public static RestAdapter getInstance(String apiUrl) {
		if (sInstance == null) {
			sInstance = new RestAdapter.Builder().setServer(apiUrl).setDebug(BuildConfig.DEBUG).build();
		}
		return sInstance;
	}
}
