package fortin.truefalse.multiplayer.quiz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DataBaseHelper";
	public static String DB_PATH = "";
	public static String DB_NAME = "truefalsedb";
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	// Contacts table name
	private static final String TABLE_CONTACTS = "truefalse";
	private static final String KEY_ID = "id";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_ANSWER = "answer";
	private static final String KEY_DIFFICULTY = "difficulty";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 3);// 1? its Database Version
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	// Check that the database exists here: /data/data/your package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
		return dbFile.exists();
	}

	public void deleteDatabase() {

		File dbFile = new File(DB_PATH + DB_NAME);
		dbFile.delete();

	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		// Log.v("mPath", mPath);
		mDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDataBase != null;
	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	void addContact(QuizPojo contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KEY_ID, contact.get_id());
		values.put(KEY_QUESTION, contact.get_question());
		values.put(KEY_ANSWER, contact.get_answer());
		values.put(KEY_DIFFICULTY, contact.getCategory_name());

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	public List<QuizPojo> getquestion(String difficulty, int quesno) {
		List<QuizPojo> questionList = new ArrayList<QuizPojo>();

		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " where "
				+ KEY_DIFFICULTY + "= ? ORDER BY RANDOM() LIMIT " + quesno;

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] { difficulty });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				QuizPojo contact = new QuizPojo();

				contact.set_question(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_QUESTION)));

				contact.set_answer(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_ANSWER)));

				// Adding contact to list
				questionList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return questionList;

	}
	
	public int getcount(String difficulty, int quesno) {
		int counter = 0;

		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " where "
				+ KEY_DIFFICULTY + "= ? ORDER BY RANDOM() LIMIT " + quesno;

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] { difficulty });

		counter = cursor.getCount();
				// return contact list
		return counter;

	}

}