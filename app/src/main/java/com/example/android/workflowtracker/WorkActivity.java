package com.example.android.workflowtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        timeTextView = (TextView)findViewById(R.id.timeTV);
        final Button timeStart = (Button) findViewById(R.id.timerStart);
        timeStart.setText(R.string.start);
        Button timeFinish = (Button) findViewById(R.id.timerFinish);

        context = this;

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chronometer != null){
                    getTimeInMilis(currentTimeAtStop);
                    stopTimer();

                    if (timeStart.getText() == "Work"){
                        totalBreakTime =+ currentTimeAtStop;
                        timeStart.setText(R.string.takeBreak);
                        numOfBreaks++;
                    } else if (timeStart.getText() == "Break") {
                        totalWorkTime =+ currentTimeAtStop;
                        timeStart.setText(R.string.work);
                    }
                } else if (timeStart.getText() == "Start") {
                    timeStart.setText(R.string.takeBreak);
                } startTimer();
            }
        });

        timeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chronometer != null){
                    // Stop the clock and get the time in milliseconds
                    getTimeInMilis(currentTimeAtStop);

                    if (timeStart.getText() == "Work"){
                        totalBreakTime =+ currentTimeAtStop;
                        numOfBreaks++;
                    } else if (timeStart.getText() == "Break"){
                        totalWorkTime =+ currentTimeAtStop;
                    }
                    stopTimer();
                }

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

    public long getTimeInMilis(long currentTimeInMilis) {
        long currentTimeAtStop = currentTimeInMilis;
        return currentTimeAtStop;
    }
}
