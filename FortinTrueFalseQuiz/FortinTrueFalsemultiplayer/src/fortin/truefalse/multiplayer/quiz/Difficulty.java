package fortin.truefalse.multiplayer.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.startapp.android.publish.StartAppAd;

import fortin.truefalse.multiplayer.quiz.R;

public class Difficulty extends Activity implements OnClickListener{
	
	Button btneasy, btnnormal, btnhard;
	ImageButton btnback;
	private StartAppAd startAppAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_difficulty);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		widget();
		


        // Send a screen view.
      
	}
	
	
	public void widget()
	{
		btneasy = (Button)findViewById(R.id.btneasy);
		btnnormal = (Button)findViewById(R.id.btnnormal);
		btnhard = (Button)findViewById(R.id.btnhard);
		btnback = (ImageButton)findViewById(R.id.btnback);
		
		
		
		btneasy.setOnClickListener(this);
		btnnormal.setOnClickListener(this);
		btnhard.setOnClickListener(this);
		btnback.setOnClickListener(this);
		
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btneasy)
		{
			DataManager.difficulty = "1";
			Intent i = new Intent(this, QuestionPerRound.class);
			finish();
			startActivity(i);	
			overridePendingTransition(0, 0);
		}else if(v == btnnormal)
		{
			DataManager.difficulty = "2";
			Intent i = new Intent(this, QuestionPerRound.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		}else if(v == btnhard)
		{
			DataManager.difficulty = "3";
			Intent i = new Intent(this, QuestionPerRound.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		}else if(v == btnback)
		{
			Intent i = new Intent(this, HomeScreen.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	}
	
	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(this, HomeScreen.class);
		finish();
		startActivity(i);
		overridePendingTransition(0, 0);
		startAppAd.showAd();
		startAppAd.loadAd();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    startAppAd.onPause();
	}

	@Override
	public void onResume() {
	    super.onResume();
	    startAppAd.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

}
