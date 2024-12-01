package com.example.walksyncandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "walkSyncDatabase.db";

    // Table Name
    private static final String TABLE_NAME = "users";

    // Table Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CALORIE_INTAKE = "calorie_intake";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_GOAL_WEIGHT = "goal_weight";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_PASSWORD = "password";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_CALORIE_INTAKE + " TEXT, " +
                COLUMN_GOAL_WEIGHT + " TEXT, " +
                COLUMN_WEIGHT + " TEXT, " +
                COLUMN_HEIGHT + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String calorieIntake, String weight, String height, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_CALORIE_INTAKE, calorieIntake);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_GOAL_WEIGHT, "0");
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Returns true if insertion is successful
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean isUserExists = cursor.getCount() > 0;
        cursor.close();
        return isUserExists;
    }

    public boolean updateUser(String email, String name, String calorieIntake, String weight, String goalWeight, String height) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_CALORIE_INTAKE, calorieIntake);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_GOAL_WEIGHT, goalWeight);
        contentValues.put(COLUMN_HEIGHT, height);

        int rowsUpdated = db.update(TABLE_NAME, contentValues, COLUMN_EMAIL + " = ?", new String[]{email});
        return rowsUpdated > 0;
    }

    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
    }
}
