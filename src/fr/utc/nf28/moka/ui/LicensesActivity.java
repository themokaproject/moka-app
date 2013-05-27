package fr.utc.nf28.moka.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import fr.utc.nf28.moka.R;

public class LicensesActivity extends SherlockActivity {
	private static final String LICENSES_URL = "file:///android_asset/licenses.html";

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.licenses_activity);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final WebView webView = (WebView) findViewById(R.id.web_view);
		webView.setWebViewClient(new MokaWebClient());

		setSupportProgressBarIndeterminateVisibility(true);

		webView.loadUrl(LICENSES_URL);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class MokaWebClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}
}
