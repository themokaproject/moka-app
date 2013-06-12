package fr.utc.nf28.moka.ui.base;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.util.ConnectionUtils;

public abstract class BasePagerFragment extends SherlockFragment implements LifecycleCallback {
	private static final AtomicBoolean sIsNetworkCroutonDisplayed = new AtomicBoolean(false);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			sIsNetworkCroutonDisplayed.set(false);
		}
	}

	protected void handleNetworkError() {
		if (!sIsNetworkCroutonDisplayed.get()) {
			sIsNetworkCroutonDisplayed.set(true);
			final SherlockFragmentActivity context = getSherlockActivity();
			final Crouton networkCrouton = Crouton.makeText(context,
					context.getString(R.string.network_error),
					ConnectionUtils.NETWORK_ERROR_STYLE);
			networkCrouton.setLifecycleCallback(this);
			networkCrouton.show();
		}
	}

	@Override
	public void onDisplayed() {
	}

	@Override
	public void onRemoved() {
		sIsNetworkCroutonDisplayed.set(false);
	}
}
