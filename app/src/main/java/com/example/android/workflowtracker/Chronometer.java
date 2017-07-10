package com.example.android.workflowtracker;

import android.content.Context;

import java.util.Locale;

/**
 * Created by Cian on 07/07/2017.
 */

public class Chronometer implements Runnable {

    private static final long MILIS_TO_MINUTES = 60000;
    private static final long MILIS_TO_HOURS = 3600000;

    public boolean isRunning;
    private long mStartTime;
    private Context mContext;

    // Constructor
    public Chronometer(Context context){
        mContext = context;
    }

    public boolean start(){
        mStartTime = System.currentTimeMillis();
        return isRunning = true;
    }

    public boolean finish(){
        return isRunning = false;
    }

    @Override
    public void run() {

        // While the clock is running count in increments of 1 millisecond
        while (isRunning){
            long currentTimeInMilis = System.currentTimeMillis() - mStartTime;

            // Calculation from milliseconds to seconds, minutes, and hours
            // The % symbol specifies the limit of seconds/minute/hours
            int seconds = (int) ((currentTimeInMilis / 1000) % 60);
            int minutes = (int) ((currentTimeInMilis / (MILIS_TO_MINUTES)) % 60);
            int hours = (int) ((currentTimeInMilis / (MILIS_TO_HOURS)) % 24);

            // Format time to HH:MM:SS
            ((WorkActivity)mContext).updateTimerText(String.format(Locale.getDefault(),
                    "%02d:%02d:%02d", hours, minutes, seconds
            ));
        }
    }

}
