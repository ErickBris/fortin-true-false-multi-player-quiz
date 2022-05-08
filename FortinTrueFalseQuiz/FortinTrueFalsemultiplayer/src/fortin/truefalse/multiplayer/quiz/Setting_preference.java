package fortin.truefalse.multiplayer.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Setting_preference {
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "truefalse";

	private static final String HIGHSCORE = "highscore";
	
	
	

	public Setting_preference(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	public void updatehighscore(int highscore) {

		editor.putInt(HIGHSCORE, highscore);
		editor.commit();

	}

	public int gethighscore() {
		int highscore = 0;

		highscore = pref.getInt(HIGHSCORE, 0);

		return highscore;
	}
	
	


}