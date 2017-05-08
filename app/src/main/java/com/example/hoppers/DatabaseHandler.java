package com.example.hoppers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Peter on 07.05.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static int count = 0;

    // Database Name
    private static final String DATABASE_NAME = "levelManager";

    // Contacts table name
    private static final String TABLE_DIFFICULTIES = "difficulties";

    // Contacts Table Columns names
    private static final String DIFFICULTY = "difficulty";
    private static final String LEVEL = "level";
    private static final String COMPLETED = "completed";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DIFFICULTIES = "CREATE TABLE " + TABLE_DIFFICULTIES + "("
                + DIFFICULTY + " INTEGER  ," + LEVEL + " INTEGER  ,"
                + COMPLETED + " INTEGER" + ")";
        db.execSQL(CREATE_DIFFICULTIES);

        if (count==0) {
            for (int i = 1; i < 10; i++) {
                for (int j = 1; j < 10; j++) {
                    db.execSQL("INSERT INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                            " VALUES ( "+ i+","+ j+","+0 +")");
                }
            }
            db.execSQL("INSERT INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 1+","+0 +")");
            db.execSQL("INSERT INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 2+","+0 +")");
            db.execSQL("INSERT INTO " + TABLE_DIFFICULTIES + "(" + DIFFICULTY + "," + LEVEL + "," + COMPLETED + ")" +
                    " VALUES ( "+ 11+","+ 3+","+0 +")");
            count++;
        }

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIFFICULTIES);

        // Create tables again
        onCreate(db);
    }

    public void completionAssignment(int difficulty,int level){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DIFFICULTY, difficulty);
        values.put(LEVEL, level);

        // updating row
         db.update(TABLE_DIFFICULTIES, values, COMPLETED+ " = ? " ,new String[]{"1"});
    }


}
