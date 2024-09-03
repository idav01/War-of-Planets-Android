package com.riseofplanets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BattleDroneActivity extends AppCompatActivity {

    private int gold = 500; // Assume starting gold is 500
    private int droneCost = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_drone);

        ImageView droneImage = findViewById(R.id.droneImage);
        Button btnPurchase = findViewById(R.id.btnPurchase);

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gold >= droneCost) {
                    gold -= droneCost;
                    Intent intent = new Intent(BattleDroneActivity.this, MainActivity.class);
                    intent.putExtra("dronePurchased", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(BattleDroneActivity.this, "Not enough gold!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
