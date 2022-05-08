package fortin.truefalse.multiplayer.quiz;

import java.io.IOException;

import com.google.android.gms.analytics.GoogleAnalytics;

import fortin.truefalse.multiplayer.quiz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 3000; // 3 seconds
	DataBaseHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		db = new DataBaseHelper(this);
		
		final DataBaseHelper dbHelper = new DataBaseHelper(this);

		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Handler handler = new Handler();

		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (!mIsBackButtonPressed) {

					Intent i = new Intent(Splash.this, HomeScreen.class);
					finish();
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}

		}, SPLASH_DURATION);

	}

	@Override
	public void onBackPressed() {
		
		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}
	

}
