package com.example.jabsa.database;

import static com.example.jabsa.database.CategoriaDbSchema.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CategoriaBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "tareaBase.db";

    public CategoriaBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ CategoriaTable.NAME + "(" +
                        "_id integer primary key autoincrement, "+
                        CategoriaTable.Cols.UUID+", "+
                        CategoriaTable.Cols.NOMBRE+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
