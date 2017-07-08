package com.example.android.workflowtracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.workflowtracker.data.WorkContract.WorkEntry;

/**
 * Created by Cian on 07/07/2017.
 */

public class WorkDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = WorkDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "workflow.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link WorkDbHelper}.
     *
     * @param context of the app
     */
    public WorkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + WorkEntry.TABLE_NAME + " ("
                + WorkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorkEntry.COLUMN_WF_WORK_TIME + " TEXT NOT NULL DEFAULT 0, "
                + WorkEntry.COLUMN_WF_BREAK_TIME + " TEXT, "
                + WorkEntry.COLUMN_WF_NUM_OF_BREAKS + " INTEGER DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);

        Log.v(LOG_TAG,SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
