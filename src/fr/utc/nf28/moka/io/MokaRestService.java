package fr.utc.nf28.moka.io;

import java.util.HashMap;
import java.util.List;

import fr.utc.nf28.moka.data.HistoryEntry;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;

public interface MokaRestService {
	@GET("/history/list")
	void historyEntries(Callback<List<HistoryEntry>> cb);

	@GET("/item/list")
	void itemsEntries(Callback<Response> cb);
}