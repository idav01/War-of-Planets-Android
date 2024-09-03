package com.riseofplanets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CutSceneActivity extends AppCompatActivity {

    private int sceneIndex = 0;
    private ImageView ivCutSceneBackground;
    private LinearLayout finalSceneLayout;
    private TextView tvStoryText;
    private TextView tvFinalSceneText;
    private Button btnNext;
    private Button btnCollect;

    private String[] storyTexts = {
            "In the age of quantumverse technology, Planet Legions have emerged to harness the power of the universe...",
            "With the ability to harvest mass amounts of power, our world faces new threats and opportunities...",
            "Now, it's time to mount defenses and expand through the ever-evolving quantumverse. Your journey begins here...",
            "Quantum signature has been locked for your home planet. Press collect to add signature to planetary matrix...."
    };

    private int[] sceneImages = {
            R.drawable.scene1_image, // Scene 1 image
            R.drawable.scene2_image, // Scene 2 image
            R.drawable.scene3_image, // Scene 3 image
            R.drawable.home_planet   // Scene 4 image
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_scene);

        ivCutSceneBackground = findViewById(R.id.ivCutSceneBackground);
        finalSceneLayout = findViewById(R.id.finalSceneLayout);
        tvStoryText = findViewById(R.id.tvStoryText);
        tvFinalSceneText = findViewById(R.id.tvFinalSceneText);
        btnNext = findViewById(R.id.btnNext);
        btnCollect = findViewById(R.id.btnCollect);

        updateScene();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sceneIndex++;
                if (sceneIndex < storyTexts.length - 1) {
                    updateScene();
                } else {
                    showFinalScene();
                }
            }
        });

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomePlanetAndProceed();
            }
        });
    }

    private void updateScene() {
        tvStoryText.setVisibility(View.VISIBLE);
        tvStoryText.setText(storyTexts[sceneIndex]);
        ivCutSceneBackground.setImageResource(sceneImages[sceneIndex]);
        ivCutSceneBackground.setVisibility(View.VISIBLE);
        finalSceneLayout.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        btnCollect.setVisibility(View.GONE);
        tvFinalSceneText.setVisibility(View.GONE);
    }

    private void showFinalScene() {
        tvStoryText.setVisibility(View.GONE);
        tvFinalSceneText.setVisibility(View.VISIBLE);
        tvFinalSceneText.setText(storyTexts[sceneIndex]);
        ivCutSceneBackground.setVisibility(View.GONE);
        finalSceneLayout.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
        btnCollect.setVisibility(View.VISIBLE);
    }

    private void addHomePlanetAndProceed() {
        Profile profile = new Profile(this);
        ArrayList<Planet> planets = profile.getPlanets();
        planets.add(new Planet("Gaia", 500, 500, 500));
        profile.setPlanets(planets);
        profile.saveState();

        // Set first-time launch flag to false
        getSharedPreferences("RiseOfPlanetsPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("firstTimeLaunch", false)
                .apply();

        // Proceed to the main activity
        Intent intent = new Intent(CutSceneActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
