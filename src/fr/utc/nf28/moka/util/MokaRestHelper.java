package fr.utc.nf28.moka.util;

import fr.utc.nf28.moka.io.MokaRestService;
import retrofit.RestAdapter;

public class MokaRestHelper {
	private static RestAdapter sAdapterInstance = null;
	private static MokaRestService sMokaRestServiceInstance = null;

	static RestAdapter getInstance(String apiUrl) {
		if (sAdapterInstance == null) {
			sAdapterInstance = new RestAdapter.Builder().setServer(apiUrl).build();
		}
		return sAdapterInstance;
	}

	public static MokaRestService getMokaRestService(String apiUrl) {
		if (sMokaRestServiceInstance == null) {
			sMokaRestServiceInstance = getInstance(apiUrl).create(MokaRestService.class);
		}
		return sMokaRestServiceInstance;
	}
}
