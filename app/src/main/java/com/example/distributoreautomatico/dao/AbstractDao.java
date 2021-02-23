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

    public AbstractDao(@Nullable Context context, @Nullable String tableName) {
        super(context, tableName, null, 1);
    }

    @Override
    public abstract void onCreate(SQLiteDatabase db);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // EMPTY
    }
}
