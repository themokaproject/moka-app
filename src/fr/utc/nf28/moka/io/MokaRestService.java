package fr.utc.nf28.moka.io;

import java.util.List;

import fr.utc.nf28.moka.data.HistoryEntry;
import retrofit.Callback;
import retrofit.http.GET;

public interface MokaRestService {
	@GET("/history")
	void historyEntries(Callback<List<HistoryEntry>> cb);
}
