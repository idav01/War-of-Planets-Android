package com.riseofplanets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SpaceshipActivity extends AppCompatActivity {

    private Profile profile;
    private ImageView ivSpaceship;
    private TextView tvSpaceshipInfo;
    private Button btnPurchaseSpaceship;
    private long lastPurchaseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spaceship);

        profile = new Profile(this);

        ivSpaceship = findViewById(R.id.ivSpaceship);
        tvSpaceshipInfo = findViewById(R.id.tvSpaceshipInfo);
        btnPurchaseSpaceship = findViewById(R.id.btnPurchaseSpaceship);

        ivSpaceship.setImageResource(R.drawable.transport_ship); // Ensure this image exists in res/drawable
        tvSpaceshipInfo.setText("Transport Ship\nCost: 500 Gold\nPower: +500");

        lastPurchaseTime = profile.getLastPurchaseTime();

        btnPurchaseSpaceship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastPurchaseTime >= 24 * 60 * 60 * 1000) { // 24 hours in milliseconds
                    if (profile.getGoldAmount() >= 500) {
                        profile.setGoldAmount(profile.getGoldAmount() - 500);
                        profile.addSpaceship("transport_ship_a");
                        profile.setLastPurchaseTime(currentTime);
                        Toast.makeText(SpaceshipActivity.this, "Purchased Transport Ship!", Toast.LENGTH_SHORT).show();

                        // Navigate back to MainActivity
                        Intent intent = new Intent(SpaceshipActivity.this, MainActivity.class);
                        intent.putExtra("spaceshipPurchased", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SpaceshipActivity.this, "Not enough gold!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SpaceshipActivity.this, "You can only purchase one transport ship per day.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
