package fortin.truefalse.multiplayer.quiz;

import java.util.List;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import fortin.truefalse.multiplayer.basegame.BaseGameActivity;

public class ResultScore extends BaseGameActivity implements OnClickListener {

	TextView txtincorrscore, txtincorrect, txtcorrectscore, txtcorrect,
			txthighscore, txtscorehead, txtscore;
	ImageButton btnplayagain, btnleaderboard, btnexit;
	int rightans, wrongans, score, highscore;
	Typeface tf;
	Setting_preference pref;
	ImageView btnfacebook;
	String url= "http://play.google.com/store/apps/details?id=";
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_score);
		
		
		String PACKAGE_NAME = getApplicationContext().getPackageName();
		
		url = url+PACKAGE_NAME;
		
		
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
		pref = new Setting_preference(this);
		
		highscore = pref.gethighscore();
		
		
		tf = Typeface.createFromAsset(getAssets(), "font.ttf");
		widget();

		rightans = getIntent().getIntExtra("correct", 0);
		wrongans = getIntent().getIntExtra("incorrect", 0);
		score = getIntent().getIntExtra("score", 0);

		txtcorrectscore.setText("" + rightans);
		txtincorrscore.setText("" + wrongans);
		txtscore.setText("" + score);
		txthighscore.setText("High Score   " + score);	
		if(score > highscore)
		{
			
			if(score > 0)
			{
				pref.updatehighscore(score);
				txthighscore.setText("New Highscore..");
			}
		}
		
	
	}

	public void widget() {
		txtincorrect = (TextView) findViewById(R.id.txtincorrect);
		txtcorrect = (TextView) findViewById(R.id.txtcorrect);
		txtcorrectscore = (TextView) findViewById(R.id.txtcorrectscore);
		txtincorrscore = (TextView) findViewById(R.id.txtincorrscore);
		txthighscore = (TextView) findViewById(R.id.txthighscore);
		txtscore = (TextView) findViewById(R.id.txtscore);
		txtscorehead = (TextView) findViewById(R.id.txtscorehead);
		btnfacebook = (ImageView)findViewById(R.id.btnfb);
		txtincorrect.setTypeface(tf);
		txtcorrect.setTypeface(tf);
		txtcorrectscore.setTypeface(tf);
		txtincorrscore.setTypeface(tf);
		txtscore.setTypeface(tf);
		txtscorehead.setTypeface(tf);
		txthighscore.setTypeface(tf);

		btnplayagain = (ImageButton) findViewById(R.id.btnplayagain);
		btnleaderboard = (ImageButton) findViewById(R.id.btnleaderboard);
		btnexit = (ImageButton) findViewById(R.id.btnexit);

		btnplayagain.setOnClickListener(this);
		btnleaderboard.setOnClickListener(this);
		btnexit.setOnClickListener(this);
		btnfacebook.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnplayagain) {
			Intent i = new Intent(this, QuestionPerRound.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		} else if (v == btnleaderboard) {
			
			try
			{
				startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 1);
			}catch(Exception e)
			{
				
			}
			
		} else if (v == btnexit) {
			Intent i = new Intent(this, HomeScreen.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		}else if(v == btnfacebook)
		{
			shareAppLinkViaFacebook();
		}
	}
	
	private void shareAppLinkViaFacebook() {
	    String urlToShare = url;
	    
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
	    intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

	    // See if official Facebook app is found
	    boolean facebookAppFound = false;
	    List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
	    for (ResolveInfo info : matches) {
	        if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
	            intent.setPackage(info.activityInfo.packageName);
	            facebookAppFound = true;
	            break;
	        }
	    }

	    if (!facebookAppFound) {
	        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
	        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
	    }

	    startActivity(intent);
	}
	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(this, HomeScreen.class);
		finish();
		startActivity(i);
		overridePendingTransition(0, 0);
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
		
		try{
			
			if(DataManager.difficulty.equals("1"))
			{
		Games.Leaderboards.submitScore(getApiClient(),
                getString(R.string.leaderboard_easy),
                Long.valueOf(score));
		
			}else if(DataManager.difficulty.equals("2"))
			{
		Games.Leaderboards.submitScore(getApiClient(),
                getString(R.string.leaderboard_normal),
                Long.valueOf(score));
			}else if(DataManager.difficulty.equals("3"))
			{
		Games.Leaderboards.submitScore(getApiClient(),
                getString(R.string.leaderboard_hard),
                Long.valueOf(score));
			}
		}catch(Exception e)
		{
			Toast.makeText(ResultScore.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
