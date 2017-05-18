package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 18.05.2017.
 */

public class OnlineGame_Set_of_Levels extends Activity {

    Pond pond;
    ArrayList<String> list_recieved_levels;
    List<Integer> buttonids;
    String recieved_level;
    int enemy_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game_levels_layout);

        buttonids = new ArrayList(){};
        list_recieved_levels = new ArrayList();

        buttonids.add(R.id._1);
        buttonids.add(R.id._2);
        buttonids.add(R.id._3);
        buttonids.add(R.id._4);
        buttonids.add(R.id._5);

        Intent recieved = getIntent();

        if (recieved.getStringExtra("Levels")!=null) {
            recieved_level = recieved.getStringExtra("Level");
            for (int i = 0; i < recieved_level.length(); i++) {
                int pos = 0;
                if (recieved_level.charAt(i)==',') {
                    if (pos==0)

                        list_recieved_levels.add(recieved_level.substring(0,i-1));

                    else list_recieved_levels.add(recieved_level.substring(pos+1,i-1));
                }
            }

            int amount_of_levels = list_recieved_levels.size();

            for (int i = 5 ; i > amount_of_levels-1; i--) {
                Button button = (Button) findViewById(buttonids.get(i));
                button.setVisibility(View.INVISIBLE);
            }
        }

        if (recieved.getIntExtra("token",0)!=0) enemy_token = recieved.getIntExtra("token",0);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getBaseContext(),OnlineGame.class);
        startActivity(setIntent);
    }

    public void onClick(View v){
        if (v.getTag()!=null) {

            int tag = Integer.parseInt(v.getTag().toString());

            Intent intent = new Intent(getBaseContext(), ChosenLevel.class);

            intent.putExtra("IsOnline","xd");

            intent.putExtra("Level",Integer.parseInt(list_recieved_levels.get(tag).substring(0,2)));

            intent.putExtra("Map",list_recieved_levels.get(tag));

            if (tag<list_recieved_levels.size()) intent.putExtra("NextMap",list_recieved_levels.get(tag+1));

            intent.putExtra("Current",tag);

            intent.putExtra("Total",list_recieved_levels.size());

            startActivity(intent);
        }
    }
}
