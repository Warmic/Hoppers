package com.example.hoppers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;


public class Pond extends Activity {


ImageButton moveback;
InnerPond pond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.pond_layout);
        super.onCreate(savedInstanceState);

        moveback = (ImageButton) findViewById(R.id.stepback);
        pond = (InnerPond) findViewById(R.id.wiewska);

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


