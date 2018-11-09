package com.example.pwesc62.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pwesc62.inventory.data.InvContract.InvEntry;

public class InvDbHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "inventory.db";


    public InvDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + InvEntry.TABLE_NAME + " (" +
                        InvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        InvEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                        InvEntry.COLUMN_PRODUCT_CREATOR + " TEXT NOT NULL, " +
                        InvEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                        InvEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                        InvEntry.COLUMN_SUPPLIER_NAME + " TEXT, " +
                        InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL DEFAULT 0" + ");";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ENTRIES);

        DATABASE_VERSION = DATABASE_VERSION + 1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //   db.execSQL(SQL_DELETE_ENTRIES);
       //   onCreate(db);
    }
}
