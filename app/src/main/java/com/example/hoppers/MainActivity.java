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
    public boolean drag;
    public int startx=-1;
    public int starty=-1;
    boolean dragfrog;

    public static int diffx;
    public static int diffy;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        diffy = metrics.heightPixels/7;
        diffx = metrics.widthPixels/6;


        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        boolean is_there_a_frog = false;
        dragfrog = false;

        //dragfrog=reddrag w/e

        pond = (Pond) findViewById(R.id.wiewska);
        moveback = (ImageButton) findViewById(R.id.stepback);



        int count = -1;

         for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {
                count++;
                if (count % 2 == 0) {
                    pond.liliPads[i][j] = new LiliPads(diffx * i + diffx, j * diffy + diffy, false,false);

                    /*if (random.nextInt(4) % 2 == 0) {
                        if (is_there_a_frog == false) {
                            pond.liliPads[i][j].is_there_a_red_frog = true;
                            is_there_a_frog = true;
                            continue;
                        }
                    }
                    if (random.nextInt(3) % 2 == 0) {
                        if (pond.liliPads[i][j].is_there_a_red_frog==false)
                        pond.liliPads[i][j].hasfrog = true;
                        }
                        */
                    }
                }
            }

        pond.setup(pond.liliPads);



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




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean found;

        float x = event.getX();
        float y = event.getY();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            startx = -1;
            starty = -1;

            outerloop:
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {

                    if ( (pond.liliPads[i][j]!=null)&&(Math.sqrt(Math.pow(pond.liliPads[i][j].x-x, 2) + Math.pow(pond.liliPads[i][j].y-y+diffy, 2)) <= pond.rad)
                            &&((pond.liliPads[i][j].hasfrog==true)||(pond.liliPads[i][j].is_there_a_red_frog==true)))
                    {
                        startx=i;
                        starty=j;
                        if (pond.liliPads[i][j].hasfrog) {
                                pond.liliPads[i][j].hasfrog = false;
                                pond.draggedfrog = new LiliPads(pond.liliPads[i][j].x, pond.liliPads[i][j].y,true,false);
                        }
                        else if (pond.liliPads[i][j].is_there_a_red_frog==true){
                        pond.liliPads[i][j].is_there_a_red_frog=false;
                        pond.draggedfrog = new LiliPads(pond.liliPads[i][j].x,pond.liliPads[i][j].y,false,true);
                        dragfrog=true;
                    }
                        drag = true;
                        break outerloop;
                    }
                }
            }

        }
        else
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (drag == true) {

                    if (dragfrog == false) {
                        pond.draggedfrog = new LiliPads(x, y - diffy,true,false);
                    } else {
                        pond.draggedfrog = new LiliPads(x, y - diffy, false, true);
                    }
                }
            }
        else
            //TODO:Add an algorithm that will do checkers-like edition of a board
            if (event.getAction()== MotionEvent.ACTION_UP)
            {
                drag = false;
                found = false;
                outerloop:
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if ((pond.liliPads[i][j]!=null)&&(Math.pow(pond.liliPads[i][j].x - x, 2) + Math.pow(pond.liliPads[i][j].y - y+diffy, 2) < Math.pow(pond.rad, 2))
                                &&(pond.liliPads[i][j].hasfrog==false)&&(pond.liliPads[i][j].is_there_a_red_frog==false)&&(pond.draggedfrog!=null))
                        {
                            found=true;

                            if ((starty!=j)&&(startx==i)&&(pond.liliPads[i][2]!=null)&&(pond.liliPads[i][2].hasfrog==true)
                                    &&(Math.abs(starty-j)==4)&&(pond.liliPads[i][2].is_there_a_red_frog==false)){
                                    pond.liliPads[i][2].hasfrog=false;
                                    if (dragfrog==false)
                                    {
                                        pond.liliPads[i][j].hasfrog=true;
                                        pond.moves.add(new Move(startx,starty,i,j,i,2,false));
                                    }
                                    else {
                                        pond.liliPads[i][j].is_there_a_red_frog=true;
                                        dragfrog=false;
                                        pond.moves.add(new Move(startx,starty,i,j,i,2,true));
                                    }
                            }
                            //horisontal
                            else if ((starty==j)&&(startx!=i)&&(pond.liliPads[2][j]!=null)&&(pond.liliPads[2][j].hasfrog==true)
                                    &&(Math.abs(startx-i)==4)&&(pond.liliPads[2][j].is_there_a_red_frog==false)){
                                            pond.liliPads[2][j].hasfrog=false;
                                            if (dragfrog==false) {
                                                pond.liliPads[i][j].hasfrog = true;
                                                pond.moves.add(new Move(startx,starty,i,j,2,j,false));
                                            }
                                            else {
                                                pond.liliPads[i][j].is_there_a_red_frog=true;
                                                dragfrog=false;
                                                pond.moves.add(new Move(startx,starty,i,j,2,j,true));
                                            }
                            }
                            //vertical
                            else if ((Math.abs(startx-i)==2)&&(Math.abs(starty-j)==2)) {

                                if (i > startx) {
                                    if (j > starty) {
                                        if ((pond.liliPads[startx + 1][starty + 1].is_there_a_red_frog == false) && (pond.liliPads[startx + 1][starty + 1].hasfrog == true)) {
                                            pond.liliPads[startx + 1][starty + 1].hasfrog = false;
                                            if (dragfrog == false) {
                                                pond.moves.add(new Move(startx,starty,i,j,startx+1,starty+1,false));
                                                pond.liliPads[i][j].hasfrog = true;
                                            }
                                            else {
                                                pond.moves.add(new Move(startx,starty,i,j,startx+1,starty+1,true));
                                                pond.liliPads[i][j].is_there_a_red_frog = true;
                                                dragfrog = false;
                                            }
                                        } else found=false;
                                    } else {
                                        if ((pond.liliPads[startx + 1][starty - 1].is_there_a_red_frog == false) && (pond.liliPads[startx + 1][starty - 1].hasfrog == true)) {
                                            pond.liliPads[startx + 1][starty - 1].hasfrog = false;
                                            if (dragfrog == false) {
                                                pond.moves.add(new Move(startx,starty,i,j,startx+1,starty-1,false));
                                                pond.liliPads[i][j].hasfrog = true;
                                            }
                                            else {
                                                pond.moves.add(new Move(startx,starty,i,j,startx+1,starty-1,true));
                                                pond.liliPads[i][j].is_there_a_red_frog = true;
                                            }
                                            dragfrog = false;
                                        } else found=false;
                                    }

                                } else if (j > starty) {

                                    if ((pond.liliPads[startx - 1][starty + 1].is_there_a_red_frog == false) && (pond.liliPads[startx - 1][starty + 1].hasfrog == true)) {
                                        pond.liliPads[startx - 1][starty + 1].hasfrog = false;
                                        if (dragfrog == false) {
                                            pond.moves.add(new Move(startx,starty,i,j,startx-1,starty+1,false));
                                            pond.liliPads[i][j].hasfrog = true;
                                        }
                                        else {
                                            pond.liliPads[i][j].is_there_a_red_frog = true;
                                            pond.moves.add(new Move(startx,starty,i,j,startx-1,starty+1,true));
                                        }
                                        dragfrog = false;
                                    } else found=false;
                                } else if ((pond.liliPads[startx - 1][starty - 1].is_there_a_red_frog == false) && (pond.liliPads[startx - 1][starty - 1].hasfrog == true)) {
                                    pond.liliPads[startx - 1][starty - 1].hasfrog = false;
                                    if (dragfrog == false) {
                                        pond.moves.add(new Move(startx,starty,i,j,startx-1,starty-1,false));
                                        pond.liliPads[i][j].hasfrog = true;
                                    }
                                    else {
                                        pond.moves.add(new Move(startx,starty,i,j,startx-1,starty-1,true));
                                        pond.liliPads[i][j].is_there_a_red_frog = true;
                                        dragfrog = false;
                                    }
                                } else found=false;
                            }

                            //For debugging
                             else {found=false;}
                            break outerloop;
                        }
                    }
                }
                if (found==false&&startx!=-1&&starty!=-1) {
                    if (dragfrog==false)
                    pond.liliPads[startx][starty].hasfrog=true;
                    else {
                        pond.liliPads[startx][starty].is_there_a_red_frog=true;
                        dragfrog=false;
                    }
                }
                pond.draggedfrog=null;
            }

        return super.onTouchEvent(event);
    }

}
