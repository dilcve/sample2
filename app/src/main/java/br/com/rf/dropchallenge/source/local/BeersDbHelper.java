package br.com.rf.dropchallenge.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BeersDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Beers.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BeersPersistenceContract.UserEntry.TABLE_NAME + " (" +
                    BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON + TEXT_TYPE +
            " )";

    public BeersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
