package com.example.distributoreautomatico.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.distributoreautomatico.business.Drink;

import java.util.Vector;

public class DaoDrink extends AbstractDao {
    private static final String TABLE_NAME = "drinks";

    public DaoDrink(@Nullable Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE drinks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity INTEGER NOT NULL, cost REAL NOT NULL);");
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

    public Vector<Drink> getAllDrinks() {
        // get an readable instance of the database
        SQLiteDatabase db = getReadableDatabase();

        // create a drinks vector
        Vector<Drink> drinks = new Vector<>();
        Drink drink;

        // create a result set that has every drink in the database
        Cursor resultSet = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // while there are still drinks add them to the vector
        while (resultSet.moveToNext()) {
            drink = new Drink(
                    resultSet.getString(resultSet.getColumnIndex("name")),
                    resultSet.getInt(resultSet.getColumnIndex("quantity")),
                    resultSet.getFloat(resultSet.getColumnIndex("cost"))
            );
            drink.setId(resultSet.getInt(resultSet.getColumnIndex("id")));
            drinks.add(drink);
        }

        // close the result set
        resultSet.close();

        // return the drinks
        return drinks;
    }

    public boolean updateDrink(Drink drink) {
        // get an writable instance of the database
        SQLiteDatabase db = getWritableDatabase();

        // Create content values
        ContentValues contentValues = new ContentValues();

        // Add drink attributes to content values
        contentValues.put("name", drink.getName());
        contentValues.put("quantity", drink.getQuantity());
        contentValues.put("cost", drink.getCost());

        // execute update
        int rows = db.update(TABLE_NAME, contentValues, "id = " + drink.getId(), null);

        // return true if update was successful (if rows affected are more than 0)
        return rows > 0;
    }

    public boolean updateDrink(int id, String name, float cost, int quantity) {
        Drink drink = new Drink(name, quantity, cost);
        drink.setId(id);
        return updateDrink(drink);
    }

    public boolean addDrink(Drink drink) {
        // get an writable instance of the database
        SQLiteDatabase db = getWritableDatabase();

        // Create content values
        ContentValues contentValues = new ContentValues();

        // Add drink attributes to content values
        contentValues.put("name", drink.getName());
        contentValues.put("cost", drink.getCost());
        contentValues.put("quantity", drink.getQuantity());

        // execute insert
        long id = db.insert(TABLE_NAME, null, contentValues);

        // return true if insert was successful (if id of the newly created item is not -1)
        return id != -1;
    }

    public boolean deleteDrink(int drinkId) {
        // get an writable instance of the database
        SQLiteDatabase db = getWritableDatabase();

        // execute delete
        int rowsAffected = db.delete(TABLE_NAME, "id = " + drinkId, null);

        // return true if more than 0 rows have been affected
        return rowsAffected > 0;
    }
}
