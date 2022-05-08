package fortin.truefalse.multiplayer.quiz;

import java.util.List;

import android.app.Activity;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.games.Games;

import fortin.truefalse.multiplayer.basegame.BaseGameActivity;
import fortin.truefalse.multiplayer.quiz.R;

public class MultiplayerResult extends BaseGameActivity implements OnClickListener{

	TextView txtplayerone, txtplayertwo, txtplayeronescore, txtplayertwoscore, txtheader, txtresult;
	ImageButton btnplayagain, btnleaderboard, btnexit;
	int playeronescore, playertwoscore;
	String url= "http://play.google.com/store/apps/details?id=";
	Typeface tf;
	ImageView btnfacebook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_player_result);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
		String PACKAGE_NAME = getApplicationContext().getPackageName();
		
		url = url+PACKAGE_NAME;
		
		tf = Typeface.createFromAsset(getAssets(), "font.ttf");
		widget();
		playeronescore = getIntent().getIntExtra("playeronescore", 0);
		playertwoscore = getIntent().getIntExtra("playertwoscore", 0);
		
		txtplayeronescore.setText(""+playeronescore);
		txtplayertwoscore.setText(""+playertwoscore);
		
		String result ;
		if(playeronescore > playertwoscore)
		{
			result = "Player 1 is Winner!";
		}else if(playeronescore ==  playertwoscore)
		{
			result = "Good Luck Again! Match Tied!";
		}else
		{
			result = "Player 2 is Winner!";
		}
		
		txtresult.setText(""+result);
	}
	
	
	public void widget()
	{
		txtplayerone = (TextView)findViewById(R.id.txtplayerone);
		txtplayertwo = (TextView)findViewById(R.id.txtplayertwo);
		txtplayeronescore = (TextView)findViewById(R.id.txtplayeronescore);
		txtplayertwoscore = (TextView)findViewById(R.id.txtplayertwoscore);
		txtheader = (TextView)findViewById(R.id.txtheader);
		txtresult = (TextView)findViewById(R.id.txtresult);
		btnfacebook = (ImageView)findViewById(R.id.btnfb);
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

	    // As fallback, launch sharer.php in a browser
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
		
	}
	

}
