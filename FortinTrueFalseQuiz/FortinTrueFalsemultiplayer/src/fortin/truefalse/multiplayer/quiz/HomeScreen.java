package fortin.truefalse.multiplayer.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.games.Games;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import fortin.truefalse.multiplayer.quiz.R;
import fortin.truefalse.multiplayer.basegame.BaseGameActivity;

public class HomeScreen extends BaseGameActivity implements OnClickListener{

	
	Button btnsingleplayer, btndoubleplayer;
	ImageButton btnleaderboard, btnsharepp, btnexit;
	ImageView imglogo;
	private StartAppAd startAppAd;
	String url= "http://market.android.com/details?id=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		String PACKAGE_NAME = getApplicationContext().getPackageName();
		
		url = url+PACKAGE_NAME;
		
		StartAppSDK.init(this, "107135504", "209341252", true);
		startAppAd = new StartAppAd(this);
		widget();
	
	}
	
	public void shareapp()
	{
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"Download True False Quiz and Challenge your friends and beat... : "+url);
		startActivity(Intent
				.createChooser(sharingIntent, "Share using"));
	}
	
	


	
	public void widget()
	{
		btnsingleplayer = (Button)findViewById(R.id.btnsingleplayer);
		btndoubleplayer = (Button)findViewById(R.id.btndoubleplayer);
		btnleaderboard = (ImageButton)findViewById(R.id.btnleaderboard);
		btnexit = (ImageButton)findViewById(R.id.btnexit);
		btnsharepp = (ImageButton)findViewById(R.id.btnshareapp);
		imglogo = (ImageView)findViewById(R.id.imglogo);
		
		btnsingleplayer.setOnClickListener(this);
		btndoubleplayer.setOnClickListener(this);
		btnleaderboard.setOnClickListener(this);
		btnexit.setOnClickListener(this);
		btnsharepp.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btnsingleplayer)
		{
			
			DataManager.player = "single";
			Intent i = new Intent(this, Difficulty.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		}else if(v == btndoubleplayer)
		{
			DataManager.player = "double";
			Intent i = new Intent(this, Difficulty.class);
			finish();
			startActivity(i);
			overridePendingTransition(0, 0);
		}else if(v == btnleaderboard)
		{	
			if(getApiClient().isConnected())
			{
			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 1);
			}
		}else if(v == btnsharepp)
		{
			shareapp();
		}
	}


	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
	
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
