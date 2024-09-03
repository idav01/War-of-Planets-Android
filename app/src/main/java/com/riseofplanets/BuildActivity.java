package com.riseofplanets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BuildActivity extends AppCompatActivity {

    private Button btnBuildings;
    private Button btnSpaceships;
    private Button btnResearch;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);

        btnBuildings = findViewById(R.id.btnBuildings);
        btnSpaceships = findViewById(R.id.btnSpaceships);
        btnResearch = findViewById(R.id.btnResearch);
        btnHome = findViewById(R.id.btnHome);

        btnBuildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Buildings section
            }
        });

        btnSpaceships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildActivity.this, SpaceshipActivity.class);
                startActivity(intent);
            }
        });

        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Research section
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
