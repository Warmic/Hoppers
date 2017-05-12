package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 01.05.2017.
 */

public class SetofStoryDifficulties extends Activity {

    public ListView lvStoryLevels;
    public SetofStoryDifficultiesAdapter adapter;
    public List<StoryLevelSetClass> levelSetClassList;
    public ArrayList<Point> completed_and_total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setofstorydifficulties);

        lvStoryLevels = (ListView) findViewById(R.id.storylevellist);

        levelSetClassList = new ArrayList<>();
        completed_and_total = new ArrayList<>();

        DatabaseHandler dbh = new DatabaseHandler(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        for (int i = 4; i <= 11; i++) {

            String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_DIFFICULTIES + " where "+DatabaseHandler.DIFFICULTY +" = " + i;

            int count_completed = 0;
            int count_total = 0;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex("completed")) ==1) count_completed++;
                    count_total++;
                } while (cursor.moveToNext());
            }
            levelSetClassList.add(new StoryLevelSetClass(i,count_total,count_completed));

            cursor.close();
        }


        adapter = new SetofStoryDifficultiesAdapter(getApplicationContext(),levelSetClassList);

        lvStoryLevels.setAdapter(adapter);

        lvStoryLevels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),SetofStoryLevels.class);
                intent.putExtra("IsStory","Story");
                intent.putExtra("Level", levelSetClassList.get(position).getAmountoffrogs());
                intent.putExtra("Total",levelSetClassList.get(position).getTotallevels());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(setIntent);
    }

}
