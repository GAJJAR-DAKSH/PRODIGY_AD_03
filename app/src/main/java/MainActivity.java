package com.example.prodigy_ad_03;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView timerText;

    Button btnStart;
    Button btnPause;
    Button btnReset;
    Button btnLap;

    ListView lapList;

    Handler handler = new Handler();

    long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    ArrayList<String> laps;
    ArrayAdapter<String> adapter;

    int lapCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timerText);

        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);

        lapList = findViewById(R.id.lapList);

        laps = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                laps
        );

        lapList.setAdapter(adapter);

        btnStart.setOnClickListener(v -> {
            startTime = System.currentTimeMillis();
            handler.postDelayed(updateTimerThread, 0);
        });

        btnPause.setOnClickListener(v -> {
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimerThread);
        });

        btnReset.setOnClickListener(v -> {

            startTime = 0L;
            timeInMilliseconds = 0L;
            timeSwapBuff = 0L;
            updatedTime = 0L;

            timerText.setText("00:00:000");

            handler.removeCallbacks(updateTimerThread);

            laps.clear();
            lapCounter = 1;

            adapter.notifyDataSetChanged();
        });

        btnLap.setOnClickListener(v -> {

            String currentTime = timerText.getText().toString();

            laps.add(
                    "Lap " + lapCounter +
                            " - " + currentTime
            );

            lapCounter++;

            adapter.notifyDataSetChanged();
        });
    }

    private final Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMilliseconds =
                    System.currentTimeMillis() - startTime;

            updatedTime =
                    timeSwapBuff + timeInMilliseconds;

            int millis =
                    (int) (updatedTime % 1000);

            int seconds =
                    (int) (updatedTime / 1000);

            int minutes =
                    seconds / 60;

            seconds =
                    seconds % 60;

            timerText.setText(
                    String.format(
                            "%02d:%02d:%03d",
                            minutes,
                            seconds,
                            millis
                    )
            );

            handler.postDelayed(this, 10);
        }
    };
}
