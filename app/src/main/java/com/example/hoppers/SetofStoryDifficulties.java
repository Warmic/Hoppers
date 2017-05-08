package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setofstorydifficulties);

        lvStoryLevels = (ListView) findViewById(R.id.storylevellist);

        levelSetClassList = new ArrayList<>();

        levelSetClassList.add(new StoryLevelSetClass(4,10));
        levelSetClassList.add(new StoryLevelSetClass(5,10));
        levelSetClassList.add(new StoryLevelSetClass(6,10));
        levelSetClassList.add(new StoryLevelSetClass(7,10));
        levelSetClassList.add(new StoryLevelSetClass(8,10));
        levelSetClassList.add(new StoryLevelSetClass(9,10));
        levelSetClassList.add(new StoryLevelSetClass(10,10));
        levelSetClassList.add(new StoryLevelSetClass(11,2));

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
