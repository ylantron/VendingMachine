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

public class DaoProfit extends AbstractDao {
    private final static String TABLE_NAME = "profit";

    public DaoProfit(@Nullable Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, drink_id INTEGER NOT NULL, cost REAL NOT NULL, date DATETIME DEFAULT CURRENT_TIMESTAMP);");
    }

    public int size() {
        // get an readable instance of the database
        SQLiteDatabase db = getReadableDatabase();

        // get the count of the rows from the table
        Cursor resultSet = db.rawQuery("SELECT COUNT(*) AS count FROM " + TABLE_NAME, null);
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
            } catch (ParseException parseException) {}
        }

        resultSet.close();

        sdf.setTimeZone(timeZone);
        String convertedDateTime = sdf.format(date);
        Date convertDateTime = new Date();

        try {
            convertDateTime = sdf.parse(convertedDateTime);
        } catch (ParseException parseException) {}
        return convertDateTime;
    }

    public boolean addOperation(Drink drink) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("drink_id", drink.getId());
        contentValues.put("cost", drink.getCost());

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }
}
