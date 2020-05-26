package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private static final String DATABASE_NAME = "WhackAMoleDB.db";
    private static final int DATABASE_VER = 1;

    private static final String TABLE_ACCOUNTS = "Accounts";
    private static final String ACCOUNTS_COL_USERNAME = "Username";
    private static final String ACCOUNTS_COL_PASSWORD = "Password";
    private static final String ACCOUNTS_COL_LEVEL = "Level";
    private static final String ACCOUNTS_COL_SCORE = "Score";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        /* HINT:
            This is used to init the database.
         */
        super(context, DATABASE_NAME, factory, DATABASE_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* HINT:
            This is triggered on DB creation.
            Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);
         */
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS +
                "(" + ACCOUNTS_COL_USERNAME + " TEXT NOT NULL," +
                ACCOUNTS_COL_PASSWORD + " TEXT NOT NULL," +
                ACCOUNTS_COL_LEVEL + " INTEGER NOT NULL," +
                ACCOUNTS_COL_SCORE + " INTEGER NOT NULL," +
                "PRIMARY KEY (" + ACCOUNTS_COL_USERNAME + "," + ACCOUNTS_COL_LEVEL + ")" + ");";
        db.execSQL(CREATE_USER_TABLE);
        Log.v(TAG, "DB Created: " + TABLE_ACCOUNTS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    public void addUser(UserData userData)
    {
        /* HINT:
            This adds the user to the database based on the information given.
            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
         */
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=0; i<10; i++) {
            ContentValues values = new ContentValues();
            values.put(ACCOUNTS_COL_USERNAME, userData.getMyUserName());
            values.put(ACCOUNTS_COL_PASSWORD, userData.getMyPassword());
            values.put(ACCOUNTS_COL_LEVEL, userData.getLevels().get(i));
            values.put(ACCOUNTS_COL_SCORE, userData.getScores().get(i));
            db.insert(TABLE_ACCOUNTS, null, values);
            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
        }

        db.close();
    }

    public UserData findUser(String username)
    {
        /* HINT:
            This finds the user that is specified and returns the data information if it is found.
            If not found, it will return a null.
            Log.v(TAG, FILENAME +": Find user form database: " + query);

            The following should be used in getting the query data.
            you may modify the code to suit your design.

            if(cursor.moveToFirst()){
                do{
                    ...
                    .....
                    ...
                }while(cursor.moveToNext());
                Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            }
            else{
                Log.v(TAG, FILENAME+ ": No data found!");
            }
         */
        String query = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " +
                ACCOUNTS_COL_USERNAME  + " = \"" + username + "\"";
                //+ " ORDER BY " + ACCOUNTS_COL_LEVEL;

        Log.v(TAG, FILENAME +": Find user from database: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst() == false) {
            cursor.close();
            db.close();
            Log.v(TAG, FILENAME+ ": No data found!");
            return null;
        }

        String password = cursor.getString(1);
        ArrayList<Integer> levelList = new ArrayList<>();
        ArrayList<Integer> scoreList = new ArrayList<>();

        do {
            levelList.add(cursor.getInt(2));
            scoreList.add(cursor.getInt(3));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        UserData queryData = new UserData(username, password, levelList, scoreList);
        Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
        return queryData;
    }

    public boolean deleteAccount(String username) {
        /* HINT:
            This finds and delete the user data in the database.
            This is not reversible.
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
         */
        String query = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " +
                ACCOUNTS_COL_USERNAME  + " = \"" + username + "\"";

        Log.v(TAG, FILENAME + ": Database delete user: " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst() == false) {
            cursor.close();
            db.close();
            Log.v(TAG, FILENAME+ ": No data found!");
            return false;
        }

        do {
            db.delete(TABLE_ACCOUNTS, ACCOUNTS_COL_USERNAME + " = ?",
                    new String[] { username });
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return true;
    }
}
