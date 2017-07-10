package com.example.android.workflowtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WorkActivity extends AppCompatActivity {

    private TextView timeTextView;

    private Context context;
    private Chronometer chronometer;
    private Thread threadChrono;

    public int numOfBreaks = 0;
    public long currentTimeAtStop;
    public long totalWorkTime;
    public long totalBreakTime;

    private Button timeStart;

    private static final long MILIS_TO_MINUTES = 60000;
    private static final long MILIS_TO_HOURS = 3600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        timeTextView = (TextView)findViewById(R.id.timeTV);
        timeStart = (Button) findViewById(R.id.timerStart);
        timeStart.setText(R.string.start);
        Button timeFinish = (Button) findViewById(R.id.timerFinish);

        context = this;

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chronometer != null){
                    currentTimeAtStop = getTime();
                    stopTimer();
                }
                if (timeStart.getText().equals("Work")){
                    totalBreakTime =+ currentTimeAtStop;
                    timeStart.setText(R.string.takeBreak);
                    numOfBreaks++;
                } else if (timeStart.getText().equals("Break")) {
                    totalWorkTime =+ currentTimeAtStop;
                    timeStart.setText(R.string.work);
                } else if (timeStart.getText().equals("Start")) {
                    timeStart.setText(R.string.takeBreak);
                }
                startTimer();
            }
        });

        timeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chronometer != null){
                    // Stop the clock and get the time in milliseconds
                    getTime();
                }
                if (timeStart.getText().equals("Work")){
                    totalBreakTime =+ currentTimeAtStop;
                    numOfBreaks++;
                } else {
                    totalWorkTime =+ currentTimeAtStop;
                }
                stopTimer();

                String totalBreak = String.valueOf(totalBreakTime);
                String totalWork = String.valueOf(totalWorkTime);

                // Jump to results and pass the table values
                Intent intent = new Intent(WorkActivity.this, ResultActivity.class);
                intent.putExtra("breaks", numOfBreaks);
                intent.putExtra("workTime", totalWork);
                intent.putExtra("breakTime", totalBreak);
                startActivity(intent);
            }
        });
    }

    private void startTimer(){
        // Create new counter and thread for the counter
        chronometer = new Chronometer(context);
        threadChrono = new Thread(chronometer);
        // Start the thread and counter
        threadChrono.start();
        chronometer.start();
    }

    private void stopTimer(){
        // Stop counter and thread
        chronometer.finish();
        threadChrono.interrupt();
        // Reset counter to null
        threadChrono = null;
        chronometer = null;
    }

    public void updateTimerText(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeTextView.setText(time);
            }
        });
    }

    public long getTime() {
        String time = String.valueOf(timeTextView);
        Log.i("TEST", time);
        String[] parts = time.split(":");

        String hour = parts[0];
        Log.i("TEST", hour);
        String minute = parts[1];
        Log.i("TEST", minute);
        String second = parts[2];
        Log.i("TEST", second);

        long hours = Long.parseLong(hour);
        long minutes = Long.parseLong(minute);
        long seconds = Long.parseLong(second);

        hours = hours * MILIS_TO_HOURS;
        minutes = minutes * MILIS_TO_MINUTES;
        seconds = seconds * 1000;

        currentTimeAtStop = hours + minutes + seconds;
        return currentTimeAtStop;
    }
}
