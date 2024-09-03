package com.riseofplanets;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NewUserEventActivity extends AppCompatActivity {

    private Profile profile;
    private TextView tvDay1, tvDay2, tvDay3, tvDay4, tvDay5, tvDay6, tvDay7, tvBonusDay7, tvTimerDay1, tvTimerDay2, tvTimerDay3, tvTimerDay4, tvTimerDay5, tvTimerDay6, tvTimerDay7;
    private Button btnCollectDay1, btnCollectDay2, btnCollectDay3, btnCollectDay4, btnCollectDay5, btnCollectDay6, btnCollectDay7;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_event);

        profile = new Profile(this);

        tvDay1 = findViewById(R.id.tvDay1);
        tvDay2 = findViewById(R.id.tvDay2);
        tvDay3 = findViewById(R.id.tvDay3);
        tvDay4 = findViewById(R.id.tvDay4);
        tvDay5 = findViewById(R.id.tvDay5);
        tvDay6 = findViewById(R.id.tvDay6);
        tvDay7 = findViewById(R.id.tvDay7);
        tvBonusDay7 = findViewById(R.id.tvBonusDay7);

        tvTimerDay1 = findViewById(R.id.tvTimerDay1);
        tvTimerDay2 = findViewById(R.id.tvTimerDay2);
        tvTimerDay3 = findViewById(R.id.tvTimerDay3);
        tvTimerDay4 = findViewById(R.id.tvTimerDay4);
        tvTimerDay5 = findViewById(R.id.tvTimerDay5);
        tvTimerDay6 = findViewById(R.id.tvTimerDay6);
        tvTimerDay7 = findViewById(R.id.tvTimerDay7);

        btnCollectDay1 = findViewById(R.id.btnCollectDay1);
        btnCollectDay2 = findViewById(R.id.btnCollectDay2);
        btnCollectDay3 = findViewById(R.id.btnCollectDay3);
        btnCollectDay4 = findViewById(R.id.btnCollectDay4);
        btnCollectDay5 = findViewById(R.id.btnCollectDay5);
        btnCollectDay6 = findViewById(R.id.btnCollectDay6);
        btnCollectDay7 = findViewById(R.id.btnCollectDay7);

        initializeButtons();
        updateUI();
        startTimers();
    }

    private void initializeButtons() {
        btnCollectDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(1, 100);
            }
        });

        btnCollectDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(2, 200);
            }
        });

        btnCollectDay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(3, 300);
            }
        });

        btnCollectDay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(4, 400);
            }
        });

        btnCollectDay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(5, 500);
            }
        });

        btnCollectDay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(6, 600);
            }
        });

        btnCollectDay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectReward(7, 700);
                addNewPlanet();
            }
        });
    }

    private void collectReward(int day, int goldAmount) {
        if (!profile.isDayCollected(day)) {
            profile.setGoldAmount(profile.getGoldAmount() + goldAmount);
            profile.markDayAsCollected(day);
            updateUI(); // Update UI without finishing the activity
        }
    }

    private void addNewPlanet() {
        ArrayList<Planet> planets = profile.getPlanets();
        planets.add(new Planet("New Planet", 500, 500, 500));
        profile.setPlanets(planets);
    }

    private void updateUI() {
        updateButtonState(btnCollectDay1, tvTimerDay1, 1);
        updateButtonState(btnCollectDay2, tvTimerDay2, 2);
        updateButtonState(btnCollectDay3, tvTimerDay3, 3);
        updateButtonState(btnCollectDay4, tvTimerDay4, 4);
        updateButtonState(btnCollectDay5, tvTimerDay5, 5);
        updateButtonState(btnCollectDay6, tvTimerDay6, 6);
        updateButtonState(btnCollectDay7, tvTimerDay7, 7);
    }

    private void updateButtonState(Button button, TextView timerTextView, int day) {
        if (profile.isDayCollected(day)) {
            button.setEnabled(false);
            button.setText("Collected");
            timerTextView.setText("Collected");
        } else {
            long remainingTime = profile.getRemainingTimeForDay(day);
            if (remainingTime <= TimeUnit.HOURS.toMillis(24)) {
                button.setEnabled(true);
                button.setText("Collect");
                timerTextView.setText("00:00:00"); // Set timer to zero when the day is collectible
            } else {
                button.setEnabled(false);
                button.setText("Collect");
                timerTextView.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(remainingTime), TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60, TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60));
            }
        }
    }

    private void startTimers() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }
}
