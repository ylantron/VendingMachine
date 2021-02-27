package com.example.distributoreautomatico.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.distributoreautomatico.business.Drink;

import java.util.Vector;

public final class DaoDrink extends AbstractDao {
    private static DaoDrink instance = null;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "drinks";
    private static final String[] DATABASE_COLUMNS = {"id", "name", "cost", "quantity"};

    private DaoDrink(@Nullable Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }
    public static DaoDrink getInstance(Context context) {
        if (instance == null)
            instance = new DaoDrink(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getDatabaseCreateQuery(DATABASE_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // begin a transaction
            db.beginTransaction();

            try {
                // create a new table with the correct schema
                db.execSQL(getDatabaseCreateQuery(DATABASE_NAME + "_new"));

                // copying all rows from old database to the new one
                db.execSQL(String.format(
                        "INSERT INTO %s " +
                        "SELECT rowid, * FROM %s",

                        // String.format() parameters
                        DATABASE_NAME + "_new",
                        DATABASE_NAME
                ));

                // drop the old table
                db.execSQL("DROP TABLE " + DATABASE_NAME);

                // rename the new table
                db.execSQL(String.format(
                        "ALTER TABLE %s RENAME TO %s",

                        // String.format() parameters
                        DATABASE_NAME + "_new",
                        DATABASE_NAME
                ));

                // set the transaction successful
                db.setTransactionSuccessful();
            } finally {
                // end the transaction
                db.endTransaction();
            }
        }

        // OTHER IF UNTIL WE GET TO THE CURRENT DATABASE VERSION
    }

    protected String getDatabaseCreateQuery(String databaseName) {
        return String.format(
                "CREATE TABLE %s (" +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "quantity INTEGER NOT NULL," +
                        "cost REAL NOT NULL" +
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

    public Vector<Drink> getAllDrinks() {
        // get an readable instance of the database
        SQLiteDatabase db = getReadableDatabase();

        // create a drinks vector
        Vector<Drink> drinks = new Vector<>();
        Drink drink;

        // create a result set that has every drink in the database
        Cursor resultSet = db.rawQuery("SELECT * FROM " + DATABASE_NAME, null);

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
        int rows = db.update(DATABASE_NAME, contentValues, "id = " + drink.getId(), null);

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
        long id = db.insert(DATABASE_NAME, null, contentValues);

        // return true if insert was successful (if id of the newly created item is not -1)
        return id != -1;
    }

    public boolean deleteDrink(int drinkId) {
        // get an writable instance of the database
        SQLiteDatabase db = getWritableDatabase();

        // execute delete
        int rowsAffected = db.delete(DATABASE_NAME, "id = " + drinkId, null);

        // return true if more than 0 rows have been affected
        return rowsAffected > 0;
    }
}
