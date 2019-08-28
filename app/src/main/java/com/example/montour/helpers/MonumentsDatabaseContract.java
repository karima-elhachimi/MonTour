package com.example.montour.helpers;

import android.provider.BaseColumns;

public final class MonumentsDatabaseContract {
    private MonumentsDatabaseContract() {

    }

    public static final class MonumentInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "monument_info";
        public static final String  COLUMN_NAME = "monument_name";
        public static final String COLUMN_STREETNAME = "monument_streetname";
        public static final String COLUMN_HOUSENUMBER = "monument_housenumber";
        public static final String COLUMN_POSTALCODE = "monument_postalcode";
        public static final String COLUMN_DISTRICT = "monument_dictrict";
        public static final String COLUMN_LAT = "monument_lat";
        public static final String COLUMN_LON = "monument_lon";

        //sql create command CREATE TABLE IF NOT EXISTS monument_info (monument_name, monument_streetname,monument_housenumber, monument_postalcode, monument_dictrict )

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_NAME+ " TEXT,"
                        + COLUMN_STREETNAME + " TEXT,"
                        + COLUMN_HOUSENUMBER + " INTEGER,"
                        + COLUMN_POSTALCODE + " TEXT,"
                        + COLUMN_DISTRICT + " TEXT,"
                        +COLUMN_LAT +" REAL,"
                        + COLUMN_LON + " REAL);";

    }
}
