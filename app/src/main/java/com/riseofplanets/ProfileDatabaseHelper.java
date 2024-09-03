package com.riseofplanets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profile.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PROFILE = "profile";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GOLD_AMOUNT = "gold_amount";
    public static final String COLUMN_SPACESHIP_COUNT = "spaceship_count";
    public static final String COLUMN_LAST_PURCHASE_TIME = "last_purchase_time";
    public static final String COLUMN_DRONE_ASSIGNED = "drone_assigned";
    public static final String COLUMN_LAST_COLLECTION_TIME = "last_collection_time";
    public static final String COLUMN_TIMER_START_TIME = "timer_start_time";
    public static final String COLUMN_LAST_LOGIN_TIME = "last_login_time";
    public static final String COLUMN_IS_TIMER_ACCESSIBLE = "is_timer_accessible";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PROFILE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GOLD_AMOUNT + " INTEGER, " +
                    COLUMN_SPACESHIP_COUNT + " INTEGER, " +
                    COLUMN_LAST_PURCHASE_TIME + " INTEGER, " +
                    COLUMN_DRONE_ASSIGNED + " INTEGER, " +
                    COLUMN_LAST_COLLECTION_TIME + " INTEGER, " +
                    COLUMN_TIMER_START_TIME + " INTEGER, " +
                    COLUMN_LAST_LOGIN_TIME + " INTEGER, " +
                    COLUMN_IS_TIMER_ACCESSIBLE + " INTEGER);";

    public ProfileDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOLD_AMOUNT, 1000);
        values.put(COLUMN_SPACESHIP_COUNT, 0);
        values.put(COLUMN_LAST_PURCHASE_TIME, 0);
        values.put(COLUMN_DRONE_ASSIGNED, 0);
        values.put(COLUMN_LAST_COLLECTION_TIME, 0);
        values.put(COLUMN_TIMER_START_TIME, System.currentTimeMillis());
        values.put(COLUMN_LAST_LOGIN_TIME, 0);
        values.put(COLUMN_IS_TIMER_ACCESSIBLE, 1);
        db.insert(TABLE_PROFILE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }

    public void updateGoldAmount(int goldAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOLD_AMOUNT, goldAmount);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateSpaceshipCount(int spaceshipCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SPACESHIP_COUNT, spaceshipCount);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateLastPurchaseTime(long lastPurchaseTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_PURCHASE_TIME, lastPurchaseTime);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateDroneAssigned(boolean droneAssigned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRONE_ASSIGNED, droneAssigned ? 1 : 0);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateLastCollectionTime(long lastCollectionTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_COLLECTION_TIME, lastCollectionTime);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateTimerStartTime(long timerStartTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMER_START_TIME, timerStartTime);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateLastLoginTime(long lastLoginTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_LOGIN_TIME, lastLoginTime);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public void updateTimerAccessible(boolean isTimerAccessible) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_TIMER_ACCESSIBLE, isTimerAccessible ? 1 : 0);
        db.update(TABLE_PROFILE, values, COLUMN_ID + " = 1", null);
    }

    public Cursor getProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROFILE, null, COLUMN_ID + " = 1", null, null, null, null);
    }
}
