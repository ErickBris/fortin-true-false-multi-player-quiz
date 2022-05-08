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
import com.startapp.android.publish.StartAppAd;

import fortin.truefalse.multiplayer.quiz.R;

public class QuestionPerRound extends Activity  implements OnClickListener{

	Button btnfifty, btntwenty, btnten;
	ImageButton btnback;
	StartAppAd startAppAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_per_round);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		widget();
	}
	
	public void widget()
	{
		btnfifty = (Button)findViewById(R.id.btnfifty);
		btntwenty = (Button)findViewById(R.id.btntwenty);
		btnten = (Button)findViewById(R.id.btnten);
		btnback = (ImageButton)findViewById(R.id.btnback);
		
		
		
		btnfifty.setOnClickListener(this);
		btntwenty.setOnClickListener(this);
		btnten.setOnClickListener(this);
		btnback.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btnten)
		{	
			DataManager.noofquestions = "10";
			if(DataManager.player.equals("single"))
			{
			Intent i = new Intent(this, SinglePlayerQuiz.class);
			finish();
			startActivity(i);
			}
			else
			{
				Intent i = new Intent(this, MultiPlayerQuiz.class);
				finish();
				startActivity(i);
			}
		}else if(v == btntwenty)
		{
			DataManager.noofquestions = "20";
			if(DataManager.player.equals("single"))
			{
			Intent i = new Intent(this, SinglePlayerQuiz.class);
			finish();
			startActivity(i);
			}
			else
			{
				Intent i = new Intent(this, MultiPlayerQuiz.class);
				finish();
				startActivity(i);
			}
		}else if(v== btnfifty)
		{
			DataManager.noofquestions = "50";
			if(DataManager.player.equals("single"))
			{
			Intent i = new Intent(this, SinglePlayerQuiz.class);
			finish();
			startActivity(i);
			}
			else
			{
				Intent i = new Intent(this, MultiPlayerQuiz.class);
				finish();
				startActivity(i);
			}
		}else if(v == btnback)
		{
			Intent i = new Intent(this, Difficulty.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	}
	
	
	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(this, Difficulty.class);
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

	
}
