package fr.utc.nf28.moka.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.util.RegexUtils;
import fr.utc.nf28.moka.util.SharedPreferencesUtils;

public class IpAdressPreference extends EditTextPreference {

	public IpAdressPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IpAdressPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IpAdressPreference(Context context) {
		super(context);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			if (!RegexUtils.validateIpAddress(getText())) {
				setText(SharedPreferencesUtils.DEFAULT_PREF_IP);
				Crouton.makeText((Activity) getContext()
						, getContext().getResources().getString(R.string.change_error_ip)
						, Style.ALERT).show();
			} else {
				Crouton.makeText((Activity) getContext()
						, getContext().getResources().getString(R.string.change_success_ip)
						, Style.CONFIRM).show();
			}
		}
	}
}
