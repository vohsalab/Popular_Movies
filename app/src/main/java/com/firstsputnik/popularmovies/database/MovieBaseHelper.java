package com.firstsputnik.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firstsputnik.popularmovies.database.MovieDbSchema.MovieTable;

/**
 * Created by ibalashov on 2/19/2016.
 */
public class MovieBaseHelper extends SQLiteOpenHelper {
        private static final int VERSION = 1;
        private static final String DATABASE_NAME  = "movieBase.db";

    public MovieBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MovieTable.NAME + "(" +
         " _id integer primary key autoincrement, " +
        MovieTable.Cols.ID + ", " +
        MovieTable.Cols.DESC + ", " +
        MovieTable.Cols.TITLE + ", " +
        MovieTable.Cols.RELEASE_DATE + ", " +
        MovieTable.Cols.POSTER_PATH + ", " +
        MovieTable.Cols.RATING +  ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
