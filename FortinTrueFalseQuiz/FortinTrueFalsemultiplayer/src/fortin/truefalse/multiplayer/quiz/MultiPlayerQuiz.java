package fortin.truefalse.multiplayer.quiz;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.startapp.android.publish.StartAppAd;

import fortin.truefalse.multiplayer.quiz.R;

public class MultiPlayerQuiz extends Activity implements OnClickListener {

	TextView txtplayertwoques, txtplayeroneques, txtplayeronescore,
			txtplayertwoscore, txtquenotwo, txtquenoone;

	ImageButton btntrueone, btnfalseone, btnfalsetwo, btntruetwo;
	ProgressBar progresstimer;
	String noofquestions;
	String difficulty, question, answer;
	final private static int DIALOG_EXIT = 1;
	DataBaseHelper db;
	List<QuizPojo> getquestions = null;
	QuizPojo cn = null;
	MyCounter timer = null;
	int savedtimer, rightansone, wrongansone, rightanstwo, wronganstwo,
			totalQueLen;
	int currentQuestion, life = 0;
	int i = 0;
	boolean playeroneanswered, playertwoanswered;
	boolean answer1, answer2;
	Typeface tf;
	StartAppAd startAppAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_player_quiz);
		
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		tf = Typeface.createFromAsset(getAssets(), "font.ttf");
		widget();
		db = new DataBaseHelper(this);
		savedtimer = DataManager.timer;
		timer = new MyCounter(savedtimer * 1000, 1000);
		timer.start();

		difficulty = DataManager.difficulty;
		noofquestions = DataManager.noofquestions;
		rightansone = 0;
		wrongansone = 0;
		rightanstwo = 0;
		wronganstwo = 0;

		currentQuestion = 0;

		totalQueLen = Integer.valueOf(noofquestions);
		totalQueLen = db.getcount(difficulty, totalQueLen);

		getquestions = db.getquestion(difficulty, totalQueLen);

		getquestionsanswers(currentQuestion);
		progresstimer.setMax(DataManager.timer);
	}

	public void widget() {
		txtplayertwoques = (TextView) findViewById(R.id.txtplayertwoques);
		txtplayeroneques = (TextView) findViewById(R.id.txtplayeroneques);
		txtplayeronescore = (TextView) findViewById(R.id.txtplayeronescore);
		txtplayertwoscore = (TextView) findViewById(R.id.txtplayertwoscore);
		txtquenotwo = (TextView) findViewById(R.id.txtquenotwo);
		txtquenoone = (TextView) findViewById(R.id.txtquenoone);
		btntrueone = (ImageButton) findViewById(R.id.btntrueone);
		btnfalseone = (ImageButton) findViewById(R.id.btnfalseone);
		btntruetwo = (ImageButton) findViewById(R.id.btntruetwo);
		btnfalsetwo = (ImageButton) findViewById(R.id.btnfalsetwo);

		progresstimer = (ProgressBar) findViewById(R.id.progressBar1);

		btntrueone.setTag("TRUE");
		btntruetwo.setTag("TRUE");
		btnfalseone.setTag("FALSE");
		btnfalsetwo.setTag("FALSE");

		txtplayeroneques.setTypeface(tf);
		txtplayertwoques.setTypeface(tf);
		txtplayeronescore.setTypeface(tf);
		txtplayertwoscore.setTypeface(tf);

		txtquenotwo.setTypeface(tf);
		txtquenoone.setTypeface(tf);
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

	public void setCurrentQuestion() {

		answer1 = false;
		answer2 = false;
		btntrueone.setEnabled(true);
		btnfalseone.setEnabled(true);
		btntruetwo.setEnabled(true);
		btnfalsetwo.setEnabled(true);
		btntrueone.setOnClickListener(this);
		btntruetwo.setOnClickListener(this);
		btnfalseone.setOnClickListener(this);
		btnfalsetwo.setOnClickListener(this);

		btntrueone.setImageDrawable(getResources().getDrawable(
				R.drawable.btntrue));
		btnfalseone.setImageDrawable(getResources().getDrawable(
				R.drawable.btnfalse));
		btntruetwo.setImageDrawable(getResources().getDrawable(
				R.drawable.btntrue));
		btnfalsetwo.setImageDrawable(getResources().getDrawable(
				R.drawable.btnfalse));

		playeroneanswered = false;
		playertwoanswered = false;

		try {

			i++;

			txtplayeroneques.setText(question);
			txtplayertwoques.setText(question);
			txtquenoone.setText("" + i + "/" + DataManager.noofquestions);
			txtquenotwo.setText("" + i + "/" + DataManager.noofquestions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		String sel = v.getTag().toString();
		System.out.println("Utpal---answer =="+answer);
		
		
		if (v == btntrueone) {
			playeroneanswered = true;
			
			btnfalseone.setOnClickListener(null);
			btntrueone.setOnClickListener(null);
			btntrueone.setImageDrawable(getResources().getDrawable(
					R.drawable.btntruepassive));
			btnfalseone.setImageDrawable(getResources().getDrawable(
					R.drawable.btnfalsepassive));

			if (sel.equalsIgnoreCase(answer)) {
				rightansone++;
				answer1 = true;
				
			} else {
				answer1 = false;
				wrongansone++;
				/*
				 * Toast.makeText(this, "Player 1 Incorrect ",
				 * Toast.LENGTH_LONG) .show();
				 */
			}
		
		} else if (v == btnfalseone) {
			playeroneanswered = true;
			btntrueone.setImageDrawable(getResources().getDrawable(
					R.drawable.btntruepassive));
			btnfalseone.setImageDrawable(getResources().getDrawable(
					R.drawable.btnfalsepassive));
			btnfalseone.setOnClickListener(null);
			btntrueone.setOnClickListener(null);
			if (sel.equalsIgnoreCase(answer)) {
				answer1 = true;

				rightansone++;
			} else {
				answer1 = false;

				wrongansone++;
			}
		
		}

		else if (v == btntruetwo) {	
			playertwoanswered = true;
			btnfalsetwo.setOnClickListener(null);
			btntruetwo.setOnClickListener(null);

			btntruetwo.setImageDrawable(getResources().getDrawable(
					R.drawable.btntruepassive));
			btnfalsetwo.setImageDrawable(getResources().getDrawable(
					R.drawable.btnfalsepassive));
			if (sel.equalsIgnoreCase(answer)) {
				answer2 = true;
				rightanstwo++;

			} else {
				answer2 = false;
				wronganstwo++;

			}
			System.out.println("Utpal===ans2--True button-"+answer2);
		} else if (v == btnfalsetwo) {
			playertwoanswered = true;
			btnfalsetwo.setOnClickListener(null);
			btntruetwo.setOnClickListener(null);
			btntruetwo.setImageDrawable(getResources().getDrawable(
					R.drawable.btntruepassive));
			btnfalsetwo.setImageDrawable(getResources().getDrawable(
					R.drawable.btnfalsepassive));
			if (sel.equalsIgnoreCase(answer)) {
				answer2 = true;
				rightanstwo++;
				
			} else {
				wronganstwo++;
				answer2 = false;

			}
			System.out.println("Utpal===ans2--False button-"+answer2);
		}

		if (playeroneanswered) {

			if (playertwoanswered) {
			
				timer.cancel();
				System.out.println("Utpal===rightanstwo---"+rightanstwo+"===rightansone--"+rightansone);
				
				showtoastplayer1(answer1, answer2);

				nextquestion(3500);
			}		

		}		
		
	}

	public void nextquestion(int SPLASHTIME) {

		
		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				db.close();
				currentQuestion++;	

				if (currentQuestion == 7) {
					startAppAd.showAd();
					startAppAd.loadAd();

				}

				if (currentQuestion < totalQueLen) {
					
					timer.cancel();
					getquestionsanswers(currentQuestion);
					timer = new MyCounter(savedtimer * 1000, 1000);
					timer.start();
					progresstimer.setMax(DataManager.timer);

				} else {

					Intent i = new Intent(MultiPlayerQuiz.this,
							MultiplayerResult.class);
					i.putExtra("playeronescore", rightansone);
					i.putExtra("playertwoscore", rightanstwo);
					finish();
					startActivityForResult(i, 0);
					startAppAd.showAd();
					startAppAd.loadAd();

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

			if (!playeroneanswered) {
				wrongansone++;
			}
			if (!playertwoanswered) {
				wronganstwo++;
			}

			nextquestion(1000);

		}

		@Override
		public void onTick(long millisUntilFinished) {

			Integer milisec = new Integer(
					new Double(millisUntilFinished).intValue());
			Integer cd_secs = milisec / 1000;

			Integer seconds = (cd_secs % 3600) % 60;
			progresstimer.setProgress(seconds);

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

			View dialogview = inflater.inflate(R.layout.custom_dialog_rateapp,
					null);

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
					Intent i = new Intent(MultiPlayerQuiz.this,
							MultiplayerResult.class);
					i.putExtra("playeronescore", rightansone);
					i.putExtra("playertwoscore", rightanstwo);
					finish();
					startActivityForResult(i, 0);
					overridePendingTransition(0, 0);
					startAppAd.showAd();
					startAppAd.loadAd();
					myDialog.cancel();
				}
			});

			btncancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startAppAd.showAd();
					startAppAd.loadAd();
					myDialog.cancel();
				}
			});
			break;
		}
	}

	public void showtoastplayer1(boolean ans1, boolean ans2) {

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.reverse_custom_toast, null);
		ImageView img = (ImageView) view.findViewById(R.id.img); // player 2
		ImageView img2 = (ImageView) view.findViewById(R.id.img2); // player 1
		TextView txtpl1 = (TextView) view.findViewById(R.id.txtpl1);
		TextView txtpl2 = (TextView) view.findViewById(R.id.txtpl2);

		if (ans1) {
			img2.setImageResource(R.drawable.corr);
			txtpl1.setText("Correct : " + rightansone);
		} else {
			img2.setImageResource(R.drawable.incorr);
			txtpl1.setText("Correct : " + rightansone);
		}

		if (ans2)

		{
			img.setImageResource(R.drawable.corr);

			txtpl2.setText("Correct : " + rightanstwo);
		} else {
			img.setImageResource(R.drawable.incorr);
			txtpl2.setText("Correct : " + rightanstwo);
		}
		Toast toast = new Toast(this);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(3000);
		toast.show();

		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		img.startAnimation(shake);
		img2.startAnimation(shake);

		

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
