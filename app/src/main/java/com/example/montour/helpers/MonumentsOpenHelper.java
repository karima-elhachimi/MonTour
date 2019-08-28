package com.example.montour.helpers;

import com.example.montour.helpers.MonumentsDatabaseContract.*;
import com.example.montour.models.MonumentItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MonumentsOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Monuments.db";
    public static final int DATABASE_VERSION = 2;
     static final String LOG_TAG = "MonumentsOpenHelper";


    public MonumentsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {


            db.execSQL(MonumentInfoEntry.SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MonumentInfoEntry.SQL_CREATE_TABLE);
    }

    public boolean isDatabaseEmpty(SQLiteDatabase db) {

        String query = "SELECT  * FROM " + MonumentInfoEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount() > 0) {
            return false;
        }

        else {
            return true;
        }

    }


    public void storeMonumentValuesIntoDatabase(ContentValues values, SQLiteDatabase db){

        db.insert(MonumentInfoEntry.TABLE_NAME, null, values);

    }

    public void storeMonumentListValuesIntoDb(ArrayList<MonumentItem> items, SQLiteDatabase db){


        Log.v(LOG_TAG, db.toString());
        if(items.size() > 0 || items != null)
            items.forEach(monument -> storeMonumentValuesIntoDatabase(monument.createValues(), db));


    }

    private ArrayList<MonumentItem> createMonumentList(SQLiteDatabase db){
        ArrayList<MonumentItem> monuments = new ArrayList<>();

        String query = "SELECT  * FROM " + MonumentInfoEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        MonumentItem item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new MonumentItem(cursor);
                monuments.add(item);
            } while (cursor.moveToNext());
        }

        return monuments;

    }


    public ArrayList<MonumentItem> getAllMonuments(SQLiteDatabase db) throws Exception {
        if(this.isDatabaseEmpty(db)) {
            throw new Exception("Database is empty");

        } else
            return this.createMonumentList(db);

    }
}
