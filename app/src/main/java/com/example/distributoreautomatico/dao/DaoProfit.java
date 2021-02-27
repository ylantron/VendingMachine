package com.example.distributoreautomatico.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.distributoreautomatico.business.Drink;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DaoProfit extends AbstractDao {
    private static DaoProfit instance = null;
    private final static String DATABASE_NAME = "profit";
    private static final int DATABASE_VERSION = 1;

    private DaoProfit(@Nullable Context context) { super(context, DATABASE_NAME, DATABASE_VERSION); }
    public static DaoProfit getInstance(Context context) {
        if (instance == null)
            instance = new DaoProfit(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getDatabaseCreateQuery(DATABASE_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // OTHER IF UNTIL WE GET TO THE CURRENT DATABASE VERSION
    }

    protected String getDatabaseCreateQuery(String databaseName) {
        return String.format(
                "CREATE TABLE %s (" +
                        "id INTEGER PRIMARY KEY," +
                        "drink_id INTEGER NOT NULL," +
                        "cost REAL NOT NULL," +
                        "date DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");",

                // parameters in String.format()
                databaseName
        );
    }

    public int size() {
        // get an readable instance of the database
        SQLiteDatabase db = getReadableDatabase();

        // get the count of the rows from the table
        Cursor resultSet = db.rawQuery("SELECT COUNT(*) AS count FROM " + DATABASE_NAME, null);
        int rows = 0;

        // save the result into the rows variable
        while (resultSet.moveToNext()) {
            rows = resultSet.getInt(resultSet.getColumnIndex("count"));
        }

        // close the result set
        resultSet.close();

        // return how many rows are in the database
        return rows;
    }

    @SuppressWarnings("unused")
    public boolean isNotEmpty() {
        // return true if there are no rows in the table
        return !isEmpty();
    }

    public boolean isEmpty() {
        // return true if there are no rows in the table
        return size() <= 0;
    }

    public Date now(TimeZone timeZone) { // TODO TO REFINE THIS METHOD
        SQLiteDatabase db = getReadableDatabase();
        Cursor resultSet = db.rawQuery("SELECT CURRENT_TIMESTAMP AS now", null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();

        while (resultSet.moveToNext()) {
            try {
                date = sdf.parse(resultSet.getString(resultSet.getColumnIndex("now")));
            } catch (ParseException ignored) {}
        }

        resultSet.close();

        sdf.setTimeZone(timeZone);
        String convertedDateTime = sdf.format(date);
        Date convertDateTime = new Date();

        try {
            convertDateTime = sdf.parse(convertedDateTime);
        } catch (ParseException ignored) {}
        return convertDateTime;
    }

    public boolean addOperation(Drink drink) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("drink_id", drink.getId());
        contentValues.put("cost", drink.getCost());

        long result = db.insert(DATABASE_NAME, null, contentValues);

        return result != -1;
    }
}
