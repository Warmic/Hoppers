package com.example.hoppers;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import java.util.Random;


public class MainActivity extends AppCompatActivity {




    public Pond pond;
    public ImageButton moveback;


    @Override
        protected void onCreate(Bundle savedInstanceState) {



        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        pond = (Pond) findViewById(R.id.wiewska);
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


}
