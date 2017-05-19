package com.example.hoppers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static int startparam = 0;

    // Database Name
    private static final String DATABASE_NAME = "levelManager";

    // Difficulties Table name
    public static final String TABLE_DIFFICULTIES = "difficulties";

    // Difficulties Table Columns names
    public static final String DIFFICULTY = "difficulty";
    public static final String LEVEL = "level";
    public static final String COMPLETED = "completed";

    //Online_profile table name
    public static final String TABLE_ONLINE_PROFILE = "online_profile";

    //Online_profile Table Columns names
    public static final String NICKNAME = "nickname";
    public static final String MAP = "map";
    //progress,history of games etc.

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        create_difficulties(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Upgrading database
    public void upgrade_difficluties(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIFFICULTIES);

        // Create tables again
        onCreate(db);
    }

    public void upgrade_online_profile(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONLINE_PROFILE);

    }

    public boolean check_if_exists(SQLiteDatabase db, String tableName) {
            if (tableName == null || db == null || !db.isOpen())
            {
                return false;
            }
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
            if (!cursor.moveToFirst())
            {
                cursor.close();
                return false;
            }
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }


    //creating database
    public void create_online_profile(SQLiteDatabase db){


        String CREATE_ONLINE_PROFILE = "CREATE TABLE IF NOT EXISTS " + TABLE_ONLINE_PROFILE + "("
                + LEVEL + " INTEGER ," +MAP+" TEXT ,"+COMPLETED + " INTEGER"+ ")";

        db.execSQL(CREATE_ONLINE_PROFILE);

    }

    public void create_difficulties(SQLiteDatabase db){


        String CREATE_DIFFICULTIES = "CREATE TABLE IF NOT EXISTS " + TABLE_DIFFICULTIES + "("
                + DIFFICULTY + " INTEGER  ," + LEVEL + " INTEGER  ,"
                + COMPLETED + " INTEGER" + ")";

        db.execSQL(CREATE_DIFFICULTIES);

        if (startparam==0){

            for (int i = 4; i < 11; i++) {
                for (int j = 1; j < 11; j++) {
                    db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                            " VALUES ( "+ i+","+ j+","+0 +")");
                }
            }
            db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 1+","+0 +")");
            db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 2+","+0 +")");
            db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 3+","+0 +")");

            startparam++;
        }
    }




}
