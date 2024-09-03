package com.riseofplanets;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_USER_EVENT = 1;

    private Profile profile;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> planetNames;
    private UserManager userManager;
    private TextView tvPlanetName;
    private TextView tvPlanetCount;
    private TextView tvSpaceshipCount;
    private TextView tvPlanetStats;
    private TextView tvGoldAmount;
    private TextView tvCollectionTimer;
    private TextView tvTimer;
    private TextView tvFreeGift;
    private LinearLayout llFreeGift;
    private Button btnCollect;
    private Button btnBattleDrone;
    private Button btnBuild;
    private ImageView ivHomePlanet;
    private Handler handler = new Handler();
    private boolean dronePurchased = false;
    private boolean droneAssigned = false;
    private int gold = 1000;
    private int droneCount = 0;
    private long lastPlunderTime = 0;
    private int selectedPlanetIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userManager = new UserManager(this);
        profile = new Profile(this);

        if (userManager.isNewUser()) {
            profile.setTimerStartTime(System.currentTimeMillis());
            profile.setLastLoginTime(System.currentTimeMillis());
            userManager.setUserAsOld();
        }

        tvPlanetName = findViewById(R.id.tvPlanetName);
        tvPlanetCount = findViewById(R.id.tvPlanetCount);
        tvSpaceshipCount = findViewById(R.id.tvSpaceshipCount);
        tvPlanetStats = findViewById(R.id.tvPlanetStats);
        tvGoldAmount = findViewById(R.id.tvGoldAmount);
        tvCollectionTimer = findViewById(R.id.tvCollectionTimer);
        tvTimer = findViewById(R.id.tvTimer);
        tvFreeGift = findViewById(R.id.tvFreeGift);
        llFreeGift = findViewById(R.id.llFreeGift);
        btnCollect = findViewById(R.id.btnCollect);
        btnBattleDrone = findViewById(R.id.btnBattleDrone);
        btnBuild = findViewById(R.id.btnBuild);
        ivHomePlanet = findViewById(R.id.ivHomePlanet);

        updateUI();
        startTimer();

        tvPlanetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlanetsOwnedActivity.class);
                startActivity(intent);
            }
        });

        llFreeGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUserEventActivity.class);
                startActivityForResult(intent, REQUEST_NEW_USER_EVENT);
            }
        });

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectResources();
            }
        });

        btnBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuildActivity.class);
                startActivity(intent);
            }
        });

        btnBattleDrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile.getGoldAmount() >= 200) {
                    Intent intent = new Intent(MainActivity.this, BattleDroneActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(MainActivity.this, "Not enough gold!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivHomePlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAssignDroneDialog();
            }
        });

        if (getIntent().getBooleanExtra("dronePurchased", false)) {
            dronePurchased = true;
            droneCount++;
            profile.setGoldAmount(profile.getGoldAmount() - 200);
            btnBattleDrone.setText("Battle Drone (" + droneCount + ")");
            tvGoldAmount.setText("Gold: " + profile.getGoldAmount());
        }

        droneAssigned = profile.isDroneAssigned();
        updateCollectButtonState();
    }

    private void updateUI() {
        tvPlanetCount.setText("Planets: " + profile.getPlanets().size());
        tvSpaceshipCount.setText("Spaceships: " + profile.getSpaceshipCount());
        tvGoldAmount.setText("Gold: " + profile.getGoldAmount());

        if (!profile.getPlanets().isEmpty()) {
            Planet selectedPlanet = profile.getPlanets().get(selectedPlanetIndex);
            tvPlanetName.setText(selectedPlanet.getName() + " (Level " + selectedPlanet.getLevel() + ")");
            tvPlanetStats.setText("Power: " + selectedPlanet.getPower() +
                    "\nInfluence: " + selectedPlanet.getInfluence() +
                    "\nCommerce: " + selectedPlanet.getCommerce());
        }

        updateTimer();
    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCollectionTimer();
                updateCollectButtonState();
                updateTimer();
                handler.postDelayed(this, 1000); // Update every second
            }
        }, 0);
    }

    private void updateCollectionTimer() {
        tvCollectionTimer.setText(profile.getRemainingCollectionTime());
    }

    private void updateTimer() {
        long remainingTimeMillis = (7 * 24 * 3600000) - (System.currentTimeMillis() - profile.getTimerStartTime());
        if (remainingTimeMillis <= 0) {
            tvTimer.setText("00:00:00:00");
            profile.setTimerAccessible(false);
            profile.saveState();
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(remainingTimeMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(remainingTimeMillis) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeMillis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);
            tvTimer.setText(String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds));
        }
    }

    private void updateCollectButtonState() {
        long currentTime = System.currentTimeMillis();
        long lastCollectionTime = profile.getLastCollectionTime();
        boolean canCollect = (currentTime - lastCollectionTime >= 3600000); // 1 hour in milliseconds

        if (canCollect) {
            btnCollect.setEnabled(true);
            btnCollect.setText("Collect");
        } else {
            btnCollect.setEnabled(false);
            btnCollect.setText("Plundering");
        }
    }

    private void collectResources() {
        if (droneAssigned) {
            long currentTime = System.currentTimeMillis();
            long lastCollectionTime = profile.getLastCollectionTime();
            if (currentTime - lastCollectionTime >= 3600000) { // 1 hour in milliseconds
                profile.setGoldAmount(profile.getGoldAmount() + 200);
                profile.setLastCollectionTime(currentTime);
                tvGoldAmount.setText("Gold: " + profile.getGoldAmount()); // Update the gold amount on the screen
                Toast.makeText(this, "Collected 200 gold!", Toast.LENGTH_SHORT).show();
                updateCollectButtonState();
                updateCollectionTimer();
            }
        } else {
            Toast.makeText(this, "No drone assigned to collect resources.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAssignDroneDialog() {
        if (droneCount > 0 && !droneAssigned) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_assign_drone, null);
            builder.setView(dialogView);

            TextView tvAvailableDrones = dialogView.findViewById(R.id.tvAvailableDrones);
            Button btnAssignDrone = dialogView.findViewById(R.id.btnAssignDrone);

            tvAvailableDrones.setText("Available Drones: " + droneCount);

            AlertDialog dialog = builder.create();

            btnAssignDrone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignDrone();
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else {
            Toast.makeText(this, "No drones available or drone already assigned.", Toast.LENGTH_SHORT).show();
        }
    }

    private void assignDrone() {
        droneAssigned = true;
        profile.setDroneAssigned(droneAssigned);
        droneCount--;
        btnBattleDrone.setText("Battle Drone (" + droneCount + ")");
        lastPlunderTime = System.currentTimeMillis();
        Toast.makeText(this, "Drone assigned to Home Planet", Toast.LENGTH_SHORT).show();
        startPlundering();
    }

    private void startPlundering() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (droneAssigned && (currentTime - lastPlunderTime >= 3600000)) { // 1 hour in milliseconds
                    profile.setGoldAmount(profile.getGoldAmount() + 200);
                    lastPlunderTime = currentTime;
                    tvGoldAmount.setText("Gold: " + profile.getGoldAmount()); // Update the gold amount on the screen
                    Toast.makeText(MainActivity.this, "Plundered 200 gold!", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this, 1000 * 60); // Check every minute
            }
        }, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        profile.saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profile.loadState();
        updateUI();
        droneAssigned = profile.isDroneAssigned();
        updateCollectButtonState();

        // Check if spaceship was purchased
        if (getIntent().getBooleanExtra("spaceshipPurchased", false)) {
            updateUI();
            getIntent().removeExtra("spaceshipPurchased");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_USER_EVENT && resultCode == RESULT_OK) {
            if (data != null) {
                int collectedGold = data.getIntExtra("collectedGold", 0);
                profile.setGoldAmount(profile.getGoldAmount() + collectedGold);
                tvGoldAmount.setText("Gold: " + profile.getGoldAmount());

                if (data.getBooleanExtra("addedPlanet", false)) {
                    updateUI();
                }
            }
        }
    }
}
