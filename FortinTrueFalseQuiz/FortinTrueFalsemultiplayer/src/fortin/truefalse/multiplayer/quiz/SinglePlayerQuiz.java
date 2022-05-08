package fortin.truefalse.multiplayer.quiz;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.startapp.android.publish.StartAppAd;

import fortin.truefalse.multiplayer.quiz.R;

public class SinglePlayerQuiz extends Activity implements OnClickListener{

	CircleProgress arc;
	TextView txtquestion, txtscore, txtlives, txtqueno;
	ImageButton btntrue, btnfalse;
	String noofquestions;
	String difficulty, question, answer;
	DataBaseHelper db;
	List<QuizPojo> getquestions = null;
	QuizPojo cn = null;
	MyCounter timer = null;
	int savedtimer, rightans, wrongans, 		totalQueLen;
	int currentQuestion, score = 0, mistake = 5 , life = 0;
	int i = 0;
	final private static int DIALOG_EXIT = 1;
	Typeface tf;
	StartAppAd startAppAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_player_quiz);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		
		
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		
		tf = Typeface.createFromAsset(getAssets(), "font.ttf");
		db = new DataBaseHelper(this);

		widget();

		savedtimer = DataManager.timer;
		timer = new MyCounter(savedtimer * 1000, 1000);
		timer.start();
		
		difficulty = DataManager.difficulty;
		noofquestions = DataManager.noofquestions;
		rightans = 0;
		wrongans = 0;
		currentQuestion = 0;
		totalQueLen = Integer.valueOf(noofquestions);
		totalQueLen = db.getcount(difficulty, totalQueLen);

		getquestions = db.getquestion(difficulty, totalQueLen);

		getquestionsanswers(currentQuestion);
		

	}

	public void widget() {
		
		txtquestion = (TextView) findViewById(R.id.txtquestion);
		txtscore = (TextView) findViewById(R.id.txtscore);
		txtlives = (TextView) findViewById(R.id.txtlives);
		txtqueno = (TextView)findViewById(R.id.txtqueno);
		
		
		arc = (CircleProgress) findViewById(R.id.arc);
		btntrue = (ImageButton) findViewById(R.id.btntrue);
		btnfalse = (ImageButton) findViewById(R.id.btnfalse);
		arc.setType(CircleProgress.SECTOR);
		btntrue.setTag("TRUE");
		btnfalse.setTag("FALSE");
		btntrue.setOnClickListener(this);
		btnfalse.setOnClickListener(this);
		
		
		txtqueno.setTypeface(tf);
		txtscore.setTypeface(tf);
		txtquestion.setTypeface(tf);
		txtqueno.setTypeface(tf);
		txtqueno.setTypeface(tf);
		
	}
	
	public void getquestionsanswers(int index) {
		cn = getquestions.get(currentQuestion);
		question = cn.get_question().toString().trim();

		try {
			
			answer = new String(cn.get_answer().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setCurrentQuestion();

	}
	
	public void playsound(boolean ans)
	{
		  MediaPlayer m = new MediaPlayer();
		  String filename = "";
		  
		  if(ans)
		  {
			  filename = "correct.mp3";
		  }else
		  {
			  filename = "incorrect.mp3";
		  }

          try{
               AssetFileDescriptor descriptor = this.getAssets().openFd(filename);
               m.setDataSource(descriptor.getFileDescriptor(),                      
               descriptor.getStartOffset(), descriptor.getLength() );
               descriptor.close();
               m.prepare();
               m.start();
           } catch(Exception e){
               // handle error here..
           }

	}
	
	public void setCurrentQuestion() {

		btntrue.setOnClickListener(this);
		btnfalse.setOnClickListener(this);
		btntrue.setImageDrawable(getResources().getDrawable(R.drawable.btntrue));
		btnfalse.setImageDrawable(getResources().getDrawable(R.drawable.btnfalse));
		try {

			i++;
			String noofque =  i + " / " + totalQueLen;
			txtqueno.setText(noofque);
			txtlives.setText(life + " / "+ mistake);
			txtquestion.setText(question);
			txtscore.setText(""+score);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v1) {
		
		
		timer.cancel();
		btntrue.setImageDrawable(getResources().getDrawable(R.drawable.btntruepassive));
		btnfalse.setImageDrawable(getResources().getDrawable(R.drawable.btnfalsepassive));
		ImageButton tmp = (ImageButton) v1;
		String sel = tmp.getTag().toString();
		btnfalse.setOnClickListener(null);
		btntrue.setOnClickListener(null);
		if(sel.equals(answer))
		{
		showtoast(true);
		rightans++;
		score =  score +100;
		playsound(true);
		}else
		{
			life++;
			wrongans++;
			showtoast(false);
			score = score - 50;
			playsound(false);
		}
		nextquestion(3000);
	}

	public void nextquestion(int SPLASHTIME) {
		
		
		
		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				db.close();
				currentQuestion++;
				
				if(currentQuestion == 7 )
				{
					startAppAd.showAd();
					startAppAd.loadAd();
				}
				if (life < mistake) {

					if (currentQuestion < totalQueLen) {
						
						
						getquestionsanswers(currentQuestion);
						timer = new MyCounter(savedtimer * 1000, 1000);
						timer.start();
						

					} else {
						timer.cancel();
						Intent i = new Intent(SinglePlayerQuiz.this, ResultScore.class);
						i.putExtra("score", score);
						i.putExtra("correct", rightans);
						finish();
						i.putExtra("incorrect", wrongans);
						startActivityForResult(i, 0);
						startAppAd.showAd();
						startAppAd.loadAd();
						overridePendingTransition(0, 0);

					}
				} else {
					timer.cancel();
					Intent i = new Intent(SinglePlayerQuiz.this, ResultScore.class);
					i.putExtra("score", score);
					i.putExtra("correct", rightans);
					i.putExtra("incorrect", wrongans);
					finish();
					startActivityForResult(i, 0);
					startAppAd.showAd();
					startAppAd.loadAd();
					overridePendingTransition(0, 0);
				}

			}

		}, SPLASHTIME);
	}

	public class MyCounter extends CountDownTimer {

		public MyCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

			nextquestion(100);

		}

		@Override
		public void onTick(long millisUntilFinished) {

			Integer milisec = new Integer(
					new Double(millisUntilFinished).intValue());
			Integer cd_secs = milisec / 1000;

			Integer seconds = (cd_secs % 3600) % 60;
			arc.setmSubCurProgress(seconds);

		}

	}
	public void showtoast(boolean ans)
	{
		if(ans)
		{
			LayoutInflater inflater = getLayoutInflater();
		    View view = inflater.inflate(R.layout.custom_toast,
		                                   null);
		    ImageView img = (ImageView)view.findViewById(R.id.img);
		    img.setImageResource(R.drawable.corr);
		    Toast toast = new Toast(this);
		    toast.setView(view);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		    img.startAnimation(shake);
			
		}else 
		{
			LayoutInflater inflater = getLayoutInflater();
		    View view = inflater.inflate(R.layout.custom_toast,
		                                   null);
		    ImageView img = (ImageView)view.findViewById(R.id.img);
		    img.setImageResource(R.drawable.incorr);
		    Toast toast = new Toast(this);
		    toast.setView(view);
		    toast.setDuration(1000);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			   
		    img.startAnimation(shake);
		}
	}

	@Override
	public void onBackPressed() {
		
		showDialog(DIALOG_EXIT);
	}
	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_EXIT:
			LayoutInflater inflater = LayoutInflater.from(this);
			
			View dialogview = inflater.inflate(
					R.layout.custom_dialog_rateapp, null);
	
			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setView(dialogview);
			
			dialogDetails = dialogbuilder.create();
			
			break;
		}

		return dialogDetails;
	}


	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		
		switch (id) {
		case DIALOG_EXIT:
			final AlertDialog myDialog = (AlertDialog) dialog;
			myDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			Button btnok = (Button) myDialog.findViewById(R.id.btnOk);
			Button btncancel = (Button) myDialog.findViewById(R.id.btncancel);

			

			btnok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					timer.cancel();
					Intent i = new Intent(SinglePlayerQuiz.this, ResultScore.class);
					finish();
					i.putExtra("score", score);
					i.putExtra("correct", rightans);
					i.putExtra("incorrect", wrongans);
					startActivityForResult(i, 0);
					overridePendingTransition(0, 0);
					startAppAd.showAd();
					startAppAd.loadAd();
					myDialog.cancel();
				}
			});

			btncancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					myDialog.cancel();
					startAppAd.showAd();
					startAppAd.loadAd();
				}
			});
			break;
		}
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
