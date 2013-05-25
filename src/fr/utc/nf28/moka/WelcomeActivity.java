package fr.utc.nf28.moka;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class WelcomeActivity extends SherlockActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
	}
}
