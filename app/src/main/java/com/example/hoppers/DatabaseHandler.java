package com.example.hoppers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static int startpar = 0;

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
    public static final String MAP = "map";
    //progress,history of games etc. TABLE
    public static final String TABLE_PLAYER_PROFILE = "player_profile";
    public static final String NICKNAME = "nickname";
    public static final String TOKEN = "token";
    public static final String COUNT = "count";

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

        db.execSQL("CREATE TABLE " + TABLE_DIFFICULTIES + "("
                + DIFFICULTY + " INTEGER  ," + LEVEL + " INTEGER  ,"
                + COMPLETED + " INTEGER" + ")");

        for (int i = 4; i < 8; i++) {
            for (int j = 1; j < 11; j++) {
                db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                        " VALUES ( "+ i+","+ j+","+0 +")");
            }
        }

        for (int i = 8; i < 12; i++) {
            for (int j = 1; j < 7; j++) {
                db.execSQL("INSERT OR REPLACE INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                        " VALUES ( "+ i+","+ j+","+0 +")");
            }
        }

    }

    public void upgrade_online_profile(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONLINE_PROFILE);

    }

    public void upgrade_player_profile(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_PROFILE);

        db.execSQL("CREATE TABLE "+ TABLE_PLAYER_PROFILE+ "("+NICKNAME+" TEXT ,"
                +TOKEN+" INTEGER ,"+COUNT+" INTEGER AUTO_INCREMENT )");

        db.execSQL("INSERT INTO "+TABLE_PLAYER_PROFILE+"("+NICKNAME+","+TOKEN+") VALUES ('',0)");
    }

    //creating databases
    public void create_player_profile(SQLiteDatabase db){


        String CREATE_PLAYER_PROFILE = "CREATE TABLE IF NOT EXISTS "+ TABLE_PLAYER_PROFILE+ "("+NICKNAME+" TEXT ,"
                +TOKEN+" INTEGER ,"+COUNT+" INTEGER AUTO_INCREMENT )";

        db.execSQL(CREATE_PLAYER_PROFILE);

        String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_PLAYER_PROFILE ;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount()<1) upgrade_player_profile(db);

        cursor.close();


    }

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

            String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_DIFFICULTIES;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount()<1) upgrade_difficluties(db);

            cursor.close();


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


}
