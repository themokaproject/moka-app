package fr.utc.nf28.moka;

import java.util.List;

import fr.utc.nf28.moka.data.HistoryEntry;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MokaRestService {
	@GET("/items/{item}/history")
	void historyEntries(@Path("item") final int itemId, Callback<List<HistoryEntry>> cb);
}
