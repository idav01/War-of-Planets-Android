package com.riseofplanets;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlanetsOwnedActivity extends AppCompatActivity {

    private Profile profile;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> planetNames;
    private ListView lvOwnedPlanets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planets_owned);  // Ensure this matches the layout file name

        profile = new Profile(this);

        lvOwnedPlanets = findViewById(R.id.lvOwnedPlanets);
        planetNames = new ArrayList<>();

        updatePlanetNames();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, planetNames);
        lvOwnedPlanets.setAdapter(adapter);
    }

    private void updatePlanetNames() {
        planetNames.clear();
        for (Planet planet : profile.getPlanets()) {
            planetNames.add(planet.getName());
        }
    }
}
