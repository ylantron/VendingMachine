package com.example.distributoreautomatico.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public abstract class AbstractDao extends SQLiteOpenHelper {

    /* ORIGINAL CONSTRUCTOR ====================================
    public AbstractDao(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    } ========================================================= */

    public AbstractDao(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public abstract void onCreate(SQLiteDatabase db);

    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    protected abstract String getDatabaseCreateQuery(String databaseName);
}
