package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SetofStoryLevels extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_story_level);

        List<Integer> buttonids = new ArrayList(){};

        buttonids.add(R.id._1);
        buttonids.add(R.id._2);
        buttonids.add(R.id._3);
        buttonids.add(R.id._4);
        buttonids.add(R.id._5);
        buttonids.add(R.id._6);
        buttonids.add(R.id._7);
        buttonids.add(R.id._8);
        buttonids.add(R.id._9);

        for (int i = 1; i < 10; i++) {
            if (i>getIntent().getIntExtra("Total",0)) {
                Button but = (Button) findViewById(buttonids.get(i-1));
                but.setVisibility(View.GONE);
            }
        }

        ArrayList<Integer> iviewsids = new ArrayList<>();

        iviewsids.add(R.id._iv1);
        iviewsids.add(R.id._iv2);
        iviewsids.add(R.id._iv3);
        iviewsids.add(R.id._iv4);
        iviewsids.add(R.id._iv5);
        iviewsids.add(R.id._iv6);
        iviewsids.add(R.id._iv7);
        iviewsids.add(R.id._iv8);
        iviewsids.add(R.id._iv9);

        DatabaseHandler dbh = new DatabaseHandler(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        ArrayList<Integer> data = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_DIFFICULTIES + " where "+DatabaseHandler.DIFFICULTY +" = " + getIntent().getIntExtra("Level",0);

        Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    data.add(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.COMPLETED)));
                } while (cursor.moveToNext());
            }
            cursor.close();

        for (int i = 0; i < data.size(); i++) {

            Integer integ = data.get(i);

            if (integ == 1) {
                ImageView image = (ImageView) findViewById(iviewsids.get(i));
                image.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onClick(View v){
        if (v.getTag()!=null) {
            Intent recieved = getIntent();
            Intent intent = new Intent(getBaseContext(), ChosenLevel.class);
            intent.putExtra("IsStory","xd");
            intent.putExtra("Level",recieved.getIntExtra("Level",0));
            intent.putExtra("Map",GetLevelFromAssets(v));
            intent.putExtra("Current",v.getTag().toString());
            intent.putExtra("Total",getIntent().getIntExtra("Total",0));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getBaseContext(),SetofStoryDifficulties.class);
        startActivity(setIntent);
    }


    public String GetLevelFromAssets(View v){

        ArrayList<String> list = new ArrayList<>();

        try {
        AssetManager as = getAssets();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(as.open("level"+getIntent().getIntExtra("Level",0))));
        String line;
        while ((line = bReader.readLine()) != null) {
            list.add(line);
        }
        bReader.close();
    }
        catch (FileNotFoundException e) {
        Toast toast = Toast.makeText(getApplicationContext(),"NOTFOUND", Toast.LENGTH_SHORT);
        toast.show();
    }
        catch (IOException e) {e.printStackTrace();}

        for (String a:list
             ) {
            String tag = (String) v.getTag();
            if (tag.length()==1) tag = "0"+tag;
            if ((a.substring(0,2)).equals(tag)) return a.substring(2,a.length());
        }
        return null;
    }
}