package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 01.05.2017.
 */

public class Set_of_Story_Difficulties extends Activity {

    public ListView lvStoryLevels;
    public Set_of_Story_Difficulties_Adapter adapter;
    public List<Story_Level_Set_Class> levelSetClassList;
    public ArrayList<Point> completed_and_total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_of_story_difficulties);

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
            levelSetClassList.add(new Story_Level_Set_Class(i,count_total-1,count_completed));

            cursor.close();
        }


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int minHeight = displaymetrics.heightPixels/(levelSetClassList.size()+2);


        adapter = new Set_of_Story_Difficulties_Adapter(getApplicationContext(),levelSetClassList);

        adapter.max = minHeight;

        lvStoryLevels.setAdapter(adapter);

        lvStoryLevels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),Set_of_Story_Levels.class);
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
