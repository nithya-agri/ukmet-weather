package com.example.kisanhubandroidassignment.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDataHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    private static final String WEATHER_TABLE_NAME = "weather";
    private static final String ID_COLUMN_NAME = "_id";
    private static final String REGION_COLUMN_NAME = "region";
    private static final String PARAMETER_COLUMN_NAME = "parameter";
    private static final String YEAR_COLUMN_NAME = "year";
    public static final String MONTH_COLUMN_NAME = "month";
    public static final String VALUE_COLUMN_NAME = "value";


    public WeatherDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + WEATHER_TABLE_NAME +
                " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REGION_COLUMN_NAME + " TEXT, " +
                PARAMETER_COLUMN_NAME + " TEXT, " +
                YEAR_COLUMN_NAME + " INTEGER, " +
                MONTH_COLUMN_NAME + " TEXT, " +
                VALUE_COLUMN_NAME + " DOUBLE" + ");";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public boolean writeWeatherData(String region, String parameter, int year, String month, double value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGION_COLUMN_NAME, region);
        contentValues.put(PARAMETER_COLUMN_NAME, parameter);
        contentValues.put(YEAR_COLUMN_NAME, year);
        contentValues.put(MONTH_COLUMN_NAME, month);
        contentValues.put(VALUE_COLUMN_NAME, value);

        long result = db.insert(WEATHER_TABLE_NAME, null, contentValues);
        return result > -1;
    }

    public int getWeatherDataCount() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + WEATHER_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor getWeatherDataForRegionParameterAndYear(String region, String parameter, int year) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + WEATHER_TABLE_NAME + " WHERE "
                + REGION_COLUMN_NAME + "='" + region + "' AND "
                + PARAMETER_COLUMN_NAME + "='" + parameter + "' AND "
                + YEAR_COLUMN_NAME + "=" + year + ";";
        return db.rawQuery(query, null);
    }
}
