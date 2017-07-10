package com.example.android.workflowtracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.workflowtracker.data.WorkContract.WorkEntry;
import com.example.android.workflowtracker.data.WorkDbHelper;

public class ResultActivity extends AppCompatActivity {

    private int Breaks;
    private String workTime;
    private String breakTime;

    private long mWork;
    private long mBreak;

    private WorkDbHelper mDbHelper;

    private static final long MILIS_TO_MINUTES = 60000;
    private static final long MILIS_TO_HOURS = 3600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Breaks = getIntent().getExtras().getInt("breaks");
        workTime = getIntent().getExtras().getString("workTime");
        mWork = Long.parseLong(workTime);
        breakTime = getIntent().getExtras().getString("breakTime");
        mBreak = Long.parseLong(breakTime);

        convertTime();

        Button backToWork = (Button)findViewById(R.id.backToWork);
        backToWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Back to WorkActivity
                Intent intent = new Intent(ResultActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new WorkDbHelper(this);
        insertWorkFlow();
        displayDatabaseInfo();
    }

    private void insertWorkFlow(){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkEntry.COLUMN_WF_WORK_TIME, workTime);
        values.put(WorkEntry.COLUMN_WF_BREAK_TIME, breakTime);
        values.put(WorkEntry.COLUMN_WF_NUM_OF_BREAKS, Breaks);

        // Insert the new row, returning the primary key value of the new row
        // The first argument for db.insert() is the workflow table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(WorkEntry.TABLE_NAME, null, values);

        if (newRowId == -1){
            Toast.makeText(this, "Error with saving workflow", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Workflow saved with row id " + newRowId, Toast.LENGTH_SHORT).show();
        } db.close();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WorkEntry._ID,
                WorkEntry.COLUMN_WF_WORK_TIME,
                WorkEntry.COLUMN_WF_BREAK_TIME,
                WorkEntry.COLUMN_WF_NUM_OF_BREAKS };

        // Perform a query on the workflow table
        Cursor cursor = db.query(
                WorkEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.display_results);

        try {
            displayView.setText("Total work sessions " + cursor.getCount() + "\n\n");
            displayView.append(WorkEntry._ID + " - " +
                    WorkEntry.COLUMN_WF_WORK_TIME + " - " +
                    WorkEntry.COLUMN_WF_BREAK_TIME + " - " +
                    WorkEntry.COLUMN_WF_NUM_OF_BREAKS + " - " + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(WorkEntry._ID);
            int workColumnIndex = cursor.getColumnIndex(WorkEntry.COLUMN_WF_WORK_TIME);
            int breakColumnIndex = cursor.getColumnIndex(WorkEntry.COLUMN_WF_BREAK_TIME);
            int numBreaksColumnIndex = cursor.getColumnIndex(WorkEntry.COLUMN_WF_NUM_OF_BREAKS);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentWork = cursor.getString(workColumnIndex);
                String currentBreak = cursor.getString(breakColumnIndex);
                int currentNumBreaks = cursor.getInt(numBreaksColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentWork + " - " +
                        currentBreak + " - " +
                        currentNumBreaks));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
            db.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void convertTime(){
        long timeW = mWork;
        // Calculation from milliseconds to seconds, minutes, and hours
        // The % symbol specifies the limit of seconds/minute/hours
        int secondsW = (int) ((timeW / 1000) % 60);
        int minutesW = (int) ((timeW / (MILIS_TO_MINUTES)) % 60);
        int hoursW = (int) ((timeW / (MILIS_TO_HOURS)) % 24);
        getWorkTime(String.format("%02d:%02d:%02d", hoursW, minutesW, secondsW));

        long timeB = mBreak;
        int secondsB = (int) ((timeB / 1000) % 60);
        int minutesB = (int) ((timeB / (MILIS_TO_MINUTES)) % 60);
        int hoursB = (int) ((timeB / (MILIS_TO_HOURS)) % 24);
        getBreakTime(String.format("%02d:%02d:%02d", hoursB, minutesB, secondsB));
    }

    private void getBreakTime(String bTime) {
        breakTime = bTime;
    }

    private void getWorkTime(String wTime) {
        workTime = wTime;
    }


}
