package com.riseofplanets;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Profile {
    private static final String PREFS_NAME = "RiseOfPlanetsProfile";
    private static final String KEY_GOLD_AMOUNT = "goldAmount";
    private static final String KEY_TIMER_START_TIME = "timerStartTime";
    private static final String KEY_LAST_COLLECTION_TIME = "lastCollectionTime";
    private static final String KEY_LAST_LOGIN_TIME = "lastLoginTime";
    private static final String KEY_DRONE_ASSIGNED = "droneAssigned";
    private static final String KEY_PLANETS = "planets";
    private static final String KEY_REWARDS_COLLECTED = "rewardsCollected";
    private static final String KEY_SPACESHIPS = "spaceships";
    private static final String KEY_TIMER_ACCESSIBLE = "timerAccessible";
    private static final String KEY_LAST_PURCHASE_TIME = "lastPurchaseTime";
    private static final String KEY_COLLECTED_DAYS = "collectedDays";

    private SharedPreferences prefs;
    private int goldAmount;
    private long timerStartTime;
    private long lastCollectionTime;
    private long lastLoginTime;
    private long lastPurchaseTime;
    private boolean droneAssigned;
    private ArrayList<Planet> planets;
    private Set<Integer> rewardsCollected;
    private int spaceshipCount;
    private boolean timerAccessible;
    private Set<Integer> collectedDays;

    public Profile(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        collectedDays = new HashSet<>();
        loadState();
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
        saveState();
    }

    public long getTimerStartTime() {
        return timerStartTime;
    }

    public void setTimerStartTime(long timerStartTime) {
        this.timerStartTime = timerStartTime;
        saveState();
    }

    public long getLastCollectionTime() {
        return lastCollectionTime;
    }

    public void setLastCollectionTime(long lastCollectionTime) {
        this.lastCollectionTime = lastCollectionTime;
        saveState();
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        saveState();
    }

    public long getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    public void setLastPurchaseTime(long lastPurchaseTime) {
        this.lastPurchaseTime = lastPurchaseTime;
        saveState();
    }

    public boolean isDroneAssigned() {
        return droneAssigned;
    }

    public void setDroneAssigned(boolean droneAssigned) {
        this.droneAssigned = droneAssigned;
        saveState();
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(ArrayList<Planet> planets) {
        this.planets = planets;
        saveState();
    }

    public Set<Integer> getRewardsCollected() {
        return rewardsCollected;
    }

    public void collectReward(int dayIndex) {
        rewardsCollected.add(dayIndex);
        saveState();
    }

    public int getSpaceshipCount() {
        return spaceshipCount;
    }

    public void addSpaceship(String spaceshipType) {
        spaceshipCount++;
        saveState();
    }

    public boolean isTimerAccessible() {
        return timerAccessible;
    }

    public void setTimerAccessible(boolean timerAccessible) {
        this.timerAccessible = timerAccessible;
        saveState();
    }

    public String getRemainingCollectionTime() {
        long currentTime = System.currentTimeMillis();
        long remainingTime = 3600000 - (currentTime - lastCollectionTime); // 1 hour in milliseconds

        if (remainingTime <= 0) {
            return "00:00";
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime) - TimeUnit.MINUTES.toSeconds(minutes);
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public boolean isDayCollected(int day) {
        return collectedDays.contains(day);
    }

    public void markDayAsCollected(int day) {
        collectedDays.add(day);
        saveState();
    }

    public long getRemainingTimeForDay(int day) {
        if (isDayCollected(day)) {
            return 0; // No remaining time for collected days
        }
        long dayInMillis = TimeUnit.DAYS.toMillis(day);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timerStartTime;
        long remainingTime = dayInMillis - elapsedTime;

        return remainingTime > 0 ? remainingTime : 0;
    }

    public void saveState() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_GOLD_AMOUNT, goldAmount);
        editor.putLong(KEY_TIMER_START_TIME, timerStartTime);
        editor.putLong(KEY_LAST_COLLECTION_TIME, lastCollectionTime);
        editor.putLong(KEY_LAST_LOGIN_TIME, lastLoginTime);
        editor.putLong(KEY_LAST_PURCHASE_TIME, lastPurchaseTime);
        editor.putBoolean(KEY_DRONE_ASSIGNED, droneAssigned);
        editor.putStringSet(KEY_PLANETS, convertPlanetsToStringSet());
        editor.putStringSet(KEY_REWARDS_COLLECTED, convertRewardsToStringSet());
        editor.putInt(KEY_SPACESHIPS, spaceshipCount);
        editor.putBoolean(KEY_TIMER_ACCESSIBLE, timerAccessible);
        editor.putStringSet(KEY_COLLECTED_DAYS, convertIntegerSetToStringSet(collectedDays)); // Save collected days
        editor.apply();
    }

    public void loadState() {
        goldAmount = prefs.getInt(KEY_GOLD_AMOUNT, 1000);
        timerStartTime = prefs.getLong(KEY_TIMER_START_TIME, System.currentTimeMillis());
        lastCollectionTime = prefs.getLong(KEY_LAST_COLLECTION_TIME, 0);
        lastLoginTime = prefs.getLong(KEY_LAST_LOGIN_TIME, 0);
        lastPurchaseTime = prefs.getLong(KEY_LAST_PURCHASE_TIME, 0);
        droneAssigned = prefs.getBoolean(KEY_DRONE_ASSIGNED, false);
        planets = convertStringSetToPlanets(prefs.getStringSet(KEY_PLANETS, new HashSet<>()));
        rewardsCollected = convertStringSetToRewards(prefs.getStringSet(KEY_REWARDS_COLLECTED, new HashSet<>()));
        spaceshipCount = prefs.getInt(KEY_SPACESHIPS, 0);
        timerAccessible = prefs.getBoolean(KEY_TIMER_ACCESSIBLE, true); // Default to true
        collectedDays = convertStringSetToIntegerSet(prefs.getStringSet(KEY_COLLECTED_DAYS, new HashSet<>())); // Load collected days
    }

    private Set<String> convertIntegerSetToStringSet(Set<Integer> integerSet) {
        Set<String> stringSet = new HashSet<>();
        for (Integer integer : integerSet) {
            stringSet.add(integer.toString());
        }
        return stringSet;
    }

    private Set<Integer> convertStringSetToIntegerSet(Set<String> stringSet) {
        Set<Integer> integerSet = new HashSet<>();
        for (String string : stringSet) {
            integerSet.add(Integer.parseInt(string));
        }
        return integerSet;
    }

    private Set<String> convertPlanetsToStringSet() {
        Set<String> planetSet = new HashSet<>();
        for (Planet planet : planets) {
            planetSet.add(planet.toString());
        }
        return planetSet;
    }

    private ArrayList<Planet> convertStringSetToPlanets(Set<String> planetSet) {
        ArrayList<Planet> planetList = new ArrayList<>();
        for (String planetString : planetSet) {
            planetList.add(Planet.fromString(planetString));
        }
        return planetList;
    }

    private Set<String> convertRewardsToStringSet() {
        Set<String> rewardSet = new HashSet<>();
        for (Integer reward : rewardsCollected) {
            rewardSet.add(reward.toString());
        }
        return rewardSet;
    }

    private Set<Integer> convertStringSetToRewards(Set<String> rewardSet) {
        Set<Integer> rewardList = new HashSet<>();
        for (String rewardString : rewardSet) {
            rewardList.add(Integer.parseInt(rewardString));
        }
        return rewardList;
    }
}
