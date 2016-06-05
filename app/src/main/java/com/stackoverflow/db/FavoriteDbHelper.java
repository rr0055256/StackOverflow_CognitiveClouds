package com.stackoverflow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raman on 3/6/16.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper{
    //db_name
    private static final String db_name = "stackoverflowdb";

    //DB Version
    private static final int db_version=1;

    //table_name
    public static final String TABLE_NAME = "favourites";
    public static final String SL_NO = "sl_no";
    public static final String DISPLAY_NAME = "name";
    public static final String PROFILE_PIC = "pic";
    public static final String TITLE = "title";
    public static final String TAGS = "tags";
    public static final String LINK = "link";
    public static final String SCORE = "score";
    public static final String TIME = "time";
    public static final String QUERY = "CREATE TABLE "+TABLE_NAME+" ("+SL_NO+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ DISPLAY_NAME+" TEXT,"+ PROFILE_PIC+" TEXT,"+ TITLE+" TEXT,"+ TAGS+" TEXT,"+ SCORE+" INTEGER ,"+ LINK+" TEXT,"+TIME +" LONG);";

    //query to drop table
    private static final String dropQuery = "DROP TABLE "+TABLE_NAME;

    private SQLiteDatabase writableDatabase;

    private String areaToBeReturned;

    public FavoriteDbHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropQuery);
        onCreate(db);
    }
}