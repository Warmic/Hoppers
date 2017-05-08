package com.example.hoppers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Peter on 20.04.2017.
 */

public class ChosenLevel extends Activity {

    ImageButton moveback;
    Pond pond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.pond_layout);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        pond = (Pond) findViewById(R.id.pond);
        if (intent.getIntExtra("Level",0)!=0) pond.amountoffrogs= intent.getIntExtra("Level",0);
        if (intent.getStringExtra("Random")!=null) pond.randomlevel=true;

        if (intent.getStringExtra("IsStory")!=null) pond.setupcomplete=true;


        if (intent.getStringExtra("Current")!=null) pond.current = Integer.parseInt(intent.getStringExtra("Current"));


        if (intent.getStringExtra("Map")!=null) {
            pond.map = intent.getStringExtra("Map");
            pond.nextmap = GetLevelFromAssets(pond.current+1+"");
        }

        if (intent.getIntExtra("Total",0)!=0) pond.total = intent.getIntExtra("Total",0);

        moveback = (ImageButton) findViewById(R.id.stepback);


        moveback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pond.moves.size()>0) {
                    Move move = pond.moves.get(pond.moves.size()-1);
                    if (move.is_redfrog==false){
                        pond.liliPads[move.startx][move.starty].hasfrog=true;
                        pond.liliPads[move.finalx][move.finaly].hasfrog=false;
                    }
                    else {
                        pond.liliPads[move.startx][move.starty].is_there_a_red_frog=true;
                        pond.liliPads[move.finalx][move.finaly].is_there_a_red_frog=false;
                    }
                    pond.liliPads[move.deletedx][move.deletedy].hasfrog=true;
                    pond.moves.remove(pond.moves.size()-1);
                }
            }
        });

    }

    public String GetLevelFromAssets(String tag){

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
        catch (IOException e) {e.printStackTrace();}

        for (String a:list
                ) {
            if (tag.length()==1) tag = "0"+tag;
            Log.d("Catch",a.substring(0,2)+"   "+tag);
            if ((a.substring(0,2)).equals(tag)) return a.substring(2,a.length());
        }
        return null;
    }



    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        if (getIntent().getStringExtra("IsStory")!=null) {
            Intent setIntent = new Intent(getBaseContext(),SetofStoryLevels.class);
            setIntent.putExtra("Total",getIntent().getIntExtra("Total",0));
            setIntent.putExtra("Level",getIntent().getIntExtra("Level",0));
            startActivity(setIntent);
        }
        else if (getIntent().getStringExtra("Random")!=null) {
            Intent setIntent = new Intent(getBaseContext(),SetofRandomLevels.class);
            setIntent.putExtra("Random","random");
            startActivity(setIntent);
        }

    }

}