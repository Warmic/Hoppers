package com.example.hoppers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Peter on 19.04.2017.
 */

public class Pond extends View {


    public LiliPads draggedfrog = null;
    public LiliPads liliPads[][] = new LiliPads[5][5];
    public ArrayList<Move> moves = new ArrayList<Move>();
    public ArrayList<Point> points = new ArrayList<>();



    public int startx = -1;
    public int starty = -1;
    public int diffx;
    public int diffy;
    public int amountoffrogs;
    public final int rad = 80;
    public int current;
    public int total;
    int startingi;
    int startingj;


    final double deg = Math.toRadians(45);

    Bitmap draggedmap;
    Bitmap redmap;
    Bitmap frogmap;
    Bitmap lilimap;
    Bitmap background;

    public String thislevel = "";
    public String nextmap;
    public String map;

    boolean dragfrog;
    public boolean PadsUp;
    public boolean randomlevel;
    public boolean levelfinished;
    public boolean setupcomplete;
    public boolean drag;
    public boolean messageshown ;



    public Pond(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        this.diffx = canvas.getWidth() / 6;
        this.diffy = canvas.getHeight() / 6;

        if (liliPads[0][0] == null) {

            points.clear();

            int count = -1;

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    count++;
                    if (count % 2 == 0) {
                        liliPads[i][j] = new LiliPads(diffx * i + diffx, j * diffy + diffy, false, false);
                    }
                }
            }

            if (setupcomplete==false) Pond.this.setup(liliPads); else
            if (map!=null)
            {
                for (int i = 0; i < map.length(); i+=5) {
                    int x =  map.charAt(i)-48;
                    int y =  map.charAt(i+1)-48;
                    boolean fr;
                    boolean rfr;
                    if (map.charAt(i+2)=='t') {fr = true; rfr = false;} else
                    if (map.charAt(i+3)=='t') {fr = false;rfr = true;} else
                    {fr = false;rfr=false;}
                    liliPads[x][y].hasfrog=fr;
                    liliPads[x][y].is_there_a_red_frog=rfr;
                    if (points.size()!=amountoffrogs) points.add(new Point(1,1));
                }

            }
        }

        if (redmap == null || frogmap == null || lilimap == null || background == null || draggedmap == null) {
            draggedmap = BitmapFactory.decodeResource(getResources(), R.drawable.frog);
            redmap = BitmapFactory.decodeResource(getResources(), R.drawable.redfrog);
            frogmap = BitmapFactory.decodeResource(getResources(), R.drawable.frog);
            lilimap = BitmapFactory.decodeResource(getResources(), R.drawable.lilipad);
            background = BitmapFactory.decodeResource(getResources(), R.drawable.pondbackground);


            float aspectRatio = lilimap.getWidth() / (float) lilimap.getHeight();

            if (diffx < diffy) {
                int width = diffx;

                int height = Math.round(width / aspectRatio);

                lilimap = Bitmap.createScaledBitmap(lilimap, width, height, false);

                aspectRatio = frogmap.getWidth() / frogmap.getHeight();
                height = Math.round(width / aspectRatio);

                frogmap = Bitmap.createScaledBitmap(frogmap, width, height, true);
                redmap = Bitmap.createScaledBitmap(redmap, width, height, true);
                background = Bitmap.createScaledBitmap(background, diffx * 6, diffy * 6, true);
            }
            //TODO:Fix scaling
            else {
                int width = diffx;

                int height = Math.round(width / aspectRatio);

                lilimap = Bitmap.createScaledBitmap(lilimap, width, height, false);

                aspectRatio = frogmap.getWidth() / frogmap.getHeight();
                height = Math.round(width / aspectRatio);

                frogmap = Bitmap.createScaledBitmap(frogmap, width, height, true);
                redmap = Bitmap.createScaledBitmap(redmap, width, height, true);
                background = Bitmap.createScaledBitmap(background, diffx * 6, diffy * 6, true);
            }
        }

        canvas.drawBitmap(background, 0, 0, paint);

        paint.setStrokeWidth(6);
        paint.setColor(Color.BLACK);

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {

                if (liliPads[i][j] != null) {

                    int x = (int) liliPads[i][j].x;
                    int y = (int) liliPads[i][j].y;

                    if ((j == 0 || j == 4 || j == 2) && i == 2) {
                        canvas.drawLine(x - diffx * 2, y, x + diffx * 2, y, paint);
                        //horisontal
                    }
                    if (j == 2 && (i == 0 || i == 4)) {
                        canvas.drawLine(x, y - diffy * 2, x, y + diffy * 2, paint);
                        //vertical
                    }
                    if ((j > 1) && (i < 3)) {
                        canvas.drawLine(x, y, x + diffx * 3 * (float) Math.cos(deg), y - diffy * 3 * (float) Math.sin(deg), paint);
                        //diagonal
                    }
                    if ((j > 1) && (i > 1)) {
                        canvas.drawLine(x, y, x - diffx * 3 * (float) Math.cos(deg), y - diffy * 3 * (float) Math.sin(deg), paint);
                        //diagonal
                    }
                    if ((j==0)&&(i==2)){
                        canvas.drawLine(x,y,x,y+diffy*2,paint);
                    }
                    if ((j==4)&&(i==2)){
                        canvas.drawLine(x,y,x,y-diffy*2,paint);
                    }

                }

            }

        }

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {

                if (liliPads[i][j] != null) {

                    int x = (int) liliPads[i][j].x;
                    int y = (int) liliPads[i][j].y;

                    if (PadsUp == false)
                        canvas.drawBitmap(lilimap, liliPads[i][j].x - lilimap.getWidth() / 2, liliPads[i][j].y - lilimap.getHeight() / 2, paint);
                    if (liliPads[i][j].hasfrog == true)
                        canvas.drawBitmap(frogmap, x - frogmap.getWidth() / 2, y - frogmap.getHeight() / 2, paint);
                    if (liliPads[i][j].is_there_a_red_frog == true)
                        canvas.drawBitmap(redmap, x - redmap.getWidth() / 2, y - redmap.getHeight() / 2, paint);
                }
            }
            if (i == 5) PadsUp = true;
        }

        if (draggedfrog != null) {
            if (draggedfrog.is_there_a_red_frog == false)
                canvas.drawBitmap(frogmap, draggedfrog.x - frogmap.getWidth() / 2, draggedfrog.y - frogmap.getHeight() / 2, paint);
            else {
                canvas.drawBitmap(redmap, draggedfrog.x - redmap.getWidth() / 2, draggedfrog.y - redmap.getHeight() / 2, paint);
            }
        }


        if (moves.size()==points.size()-1) {
            levelfinished =true;
            if (current>0){

                DatabaseHandler dbh = new DatabaseHandler(getContext());

                SQLiteDatabase db = dbh.getWritableDatabase();

                String insertQuery = "UPDATE difficulties  SET completed=1 where difficulty is'"+points.size()+"' AND level is '"+current+"'" ;

                db.execSQL(insertQuery);

                db.close();
            }
        } else levelfinished = false;

        if (levelfinished && current<total && messageshown==false) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Congratulations! \n Next level?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getContext(), ChosenLevel.class);
                            intent.putExtra("Map", nextmap);
                            intent.putExtra("IsStory", "xd");
                            intent.putExtra("Current", current + 1 + "");
                            intent.putExtra("Level", points.size() );
                            intent.putExtra("Total", total);

                            getContext().startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            messageshown = true;
        }
        if (levelfinished&&randomlevel&&messageshown==false){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("    Congratulations!     \n     Next level?     ")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getContext(), ChosenLevel.class);

                            intent.putExtra("Random","rand");
                            intent.putExtra("Level",points.size());

                            getContext().startActivity(intent);
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            messageshown = true;
        }

        invalidate();
    }


    //------------------------------------------------------------------------------------------------------------------------------
    //FINISHED_DRAWING

    public void setup(LiliPads arr[][]) {
        Random rand = new Random();

        int starti = rand.nextInt(5);
        int startj = rand.nextInt(5);

        thislevel ="";


        int tries = 0;
        points.clear();


        while (arr[starti][startj] == null) {
            starti = rand.nextInt(5);
            startj = rand.nextInt(5);
        }

        points.add(new Point(starti, startj));
        startingi = starti;
        startingj = startj;

        arr[starti][startj].is_there_a_red_frog = true;

        while (points.size() != amountoffrogs) {

            int size = points.size();

            for (int i = 0; i < size; i++) {

                if (points.get(i).x % 2 == 0)
                    check_if_i_2(points.get(i).x, points.get(i).y, arr);

                if (points.get(i).x % 2 == 1)
                    check_if_i_1(points.get(i).x, points.get(i).y, arr);
                tries++;


            }

            if (tries > 250) {


                tries = 0;
                points.clear();

                int count = -1;

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        count++;
                        if (count % 2 == 0) {
                            arr[i][j] = new LiliPads(diffx * i + diffx, j * diffy + diffy, false, false);
                        }
                    }
                }


                //-------------------CLREARING MAP,ARRAY OF POINTS TO COME UP WITH A NEW SETUP

                starti = rand.nextInt(5);
                startj = rand.nextInt(5);
                while (arr[starti][startj] == null) {
                    starti = rand.nextInt(5);
                    startj = rand.nextInt(5);
                }
                Log.d("st",starti +" "+startj);

                points.add(new Point(starti, startj));
                startingi = starti;
                startingj = startj;
                arr[starti][startj].is_there_a_red_frog = true;

            }

        }


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (arr[i][j]!=null) {
                    thislevel +=""+ i + "" + j;
                    if (arr[i][j].hasfrog) thislevel +=  "t";
                    else thislevel +=  "f";
                    if (arr[i][j].is_there_a_red_frog) thislevel += "t";
                    else thislevel +=  "f";
                }
                else thislevel+="n";
            }
        }

        Log.d("LEV",thislevel);
        setupcomplete = true;
    }


    public LiliPads[][] check_if_i_2(int starti, int startj, LiliPads arr[][]) {


        if ((startj == 0)&&(arr[starti][startj + 4].hasfrog == false) && (arr[starti][startj + 2].hasfrog == false)
                && (arr[starti][startj + 4].is_there_a_red_frog == false) && (arr[starti][startj + 2].is_there_a_red_frog == false)) {
            arr[starti][startj + 2].hasfrog = true;

            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti][startj + 4].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti][startj + 4].is_there_a_red_frog = true;
            }
            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti, startj + 4));
            points.add(new Point(starti, startj + 2));

            return arr;
        } else if ((startj == 0)&&(starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
            arr[starti + 2][startj].hasfrog = true;
            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti + 4][startj].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti + 4][startj].is_there_a_red_frog = true;
            }

            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti + 4, startj));
            points.add(new Point(starti + 2, startj));

            return arr;
        } else if ((startj == 0)&&(starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
            arr[starti - 2][startj].hasfrog = true;
            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti - 4][startj].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti - 4][startj].is_there_a_red_frog = true;
            }

            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti - 4, startj));
            points.add(new Point(starti - 2, startj));

            return arr;
        }
        //-------------------------------------------------------------------------------------


        else   if ((startj == 4)&&(arr[starti][startj - 4].hasfrog == false) && (arr[starti][startj - 2].hasfrog == false)
                && (arr[starti][startj - 4].is_there_a_red_frog == false) && (arr[starti][startj - 2].is_there_a_red_frog == false)) {
            arr[starti][startj - 2].hasfrog = true;
            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti][startj - 4].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti][startj - 4].is_there_a_red_frog = true;
            }
            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti, startj - 4));
            points.add(new Point(starti, startj - 2));

            return arr;
        } else if ((startj == 4)&&(starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
            arr[starti + 2][startj].hasfrog = true;

            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti + 4][startj].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti + 4][startj].is_there_a_red_frog = true;
            }
            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti + 4, startj));
            points.add(new Point(starti + 2, startj));

            return arr;
        }
        else if ((startj == 4)&&(starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
            arr[starti - 2][startj].hasfrog = true;
            if (arr[starti][startj].hasfrog) {
                arr[starti][startj].hasfrog = false;
                arr[starti - 4][startj].hasfrog = true;
            } else if (arr[starti][startj].is_there_a_red_frog) {
                arr[starti][startj].is_there_a_red_frog = false;
                arr[starti - 4][startj].is_there_a_red_frog = true;
            }

            points.remove(points.indexOf(new Point(starti, startj)));
            points.add(new Point(starti - 4, startj));
            points.add(new Point(starti - 2, startj));

            return arr;
        }

        else if (startj == 2&& starti==2) {

            return check_if_i_1(starti, startj, arr);

        }

        else
        if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {

            return i_les_3_j_gr_1(starti,startj,arr);
        }
        else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {

            return i_gr_1_j_gr_1(starti,startj,arr);

        }
        else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 2][startj + 2].hasfrog == false)
                && (arr[starti + 1][startj + 1].is_there_a_red_frog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {

            return i_les_3_j_les_3(starti,startj,arr);

        }
        else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {

            return i_gr_1_j_les_3(starti,startj,arr);

        } else return arr;
    }


//------------------------------------------------------------------------------- preparing functions for algorithm

    public LiliPads[][] i_gr_1_j_gr_1(int i,int j,LiliPads arr[][]){
        arr[i - 1][j - 1].hasfrog = true;
        if (arr[i][j].hasfrog) {
            arr[i][j].hasfrog = false;
            arr[i - 2][j - 2].hasfrog = true;
        } else if (arr[i][j].is_there_a_red_frog) {
            arr[i][j].is_there_a_red_frog = false;
            arr[i - 2][j - 2].is_there_a_red_frog = true;
        }

        points.remove(points.indexOf(new Point(i, j)));
        points.add(new Point(i - 1, j - 1));
        points.add(new Point(i - 2, j - 2));

        return arr;
    }

    public LiliPads[][] i_les_3_j_les_3(int i,int j,LiliPads arr[][]){
        arr[i + 1][j + 1].hasfrog = true;
        if (arr[i][j].hasfrog) {
            arr[i][j].hasfrog = false;
            arr[i + 2][j + 2].hasfrog = true;
        } else if (arr[i][j].is_there_a_red_frog) {
            arr[i + 2][j + 2].is_there_a_red_frog = true;
            arr[i][j].is_there_a_red_frog = false;
        }

        points.remove(points.indexOf(new Point(i, j)));
        points.add(new Point(i + 1, j + 1));
        points.add(new Point(i + 2, j + 2));

        return arr;
    }

    public LiliPads[][] i_gr_1_j_les_3(int i,int j,LiliPads arr[][]) {

            arr[i - 1][j + 1].hasfrog = true;

            if (arr[i][j].hasfrog) {
                arr[i][j].hasfrog = false;
                arr[i - 2][j + 2].hasfrog = true;
            } else if (arr[i][j].is_there_a_red_frog) {
                arr[i][j].is_there_a_red_frog = false;
                arr[i - 2][j + 2].is_there_a_red_frog = true;
            }


            points.remove(points.indexOf(new Point(i, j)));
            points.add(new Point(i - 1, j + 1));
            points.add(new Point(i - 2, j + 2));

            return arr;

        }

        public LiliPads[][] i_les_3_j_gr_1 ( int i, int j, LiliPads arr[][]){

            arr[i + 1][j - 1].hasfrog = true;

            if (arr[i][j].hasfrog) {
                arr[i][j].hasfrog = false;
                arr[i + 2][j - 2].hasfrog = true;
            } else if (arr[i][j].is_there_a_red_frog) {
                arr[i][j].is_there_a_red_frog = false;
                arr[i + 2][j - 2].is_there_a_red_frog = true;
            }

            points.remove(points.indexOf(new Point(i, j)));
            points.add(new Point(i + 1, j - 1));
            points.add(new Point(i + 2, j - 2));

            return arr;
        }
        //---------------------------------------------------------------------------functions are prepared


    public LiliPads[][] check_if_i_1(int starti, int startj, LiliPads arr[][]) {

        if (startingi>1&&startingj>1){

            if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {

                return i_gr_1_j_les_3(starti, startj, arr);

            }else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {

                return i_les_3_j_gr_1(starti, startj, arr);

            }  else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {

                return i_gr_1_j_gr_1(starti, startj, arr);

            }else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {

                return i_les_3_j_les_3(starti, startj, arr);

            }  else return arr;

        }

        else if (startingi>1&&startingj<3){

            if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {

                return  i_gr_1_j_les_3(starti,startj,arr);

            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {

                return i_gr_1_j_gr_1(starti,startj,arr);

            }else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {

                return i_les_3_j_les_3(starti,startj,arr);

            }else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {

                return   i_les_3_j_gr_1(starti,startj,arr);

            } else return arr;
        }
        else if (startingi<3&&startingj<3){

            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {

                return i_les_3_j_les_3(starti,startj,arr);

            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {

                return   i_les_3_j_gr_1(starti,startj,arr);

            }else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {

                return  i_gr_1_j_les_3(starti,startj,arr);

            }else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {

                return i_gr_1_j_gr_1(starti,startj,arr);

            } else return arr;

        }

        else {

            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {

                return i_les_3_j_les_3(starti,startj,arr);

            }else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {

                return  i_gr_1_j_les_3(starti,startj,arr);

            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {

                return   i_les_3_j_gr_1(starti,startj,arr);

            }else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {

              return i_gr_1_j_gr_1(starti,startj,arr);

            } else return arr;

        }

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

                    if ((liliPads[i][j] != null) && (Math.sqrt(Math.pow(liliPads[i][j].x - x, 2) + Math.pow(liliPads[i][j].y - y, 2)) <= rad)
                            && ((liliPads[i][j].hasfrog == true) || (liliPads[i][j].is_there_a_red_frog == true))) {
                        startx = i;
                        starty = j;
                        if (liliPads[i][j].hasfrog) {
                            liliPads[i][j].hasfrog = false;
                            draggedfrog = new LiliPads(liliPads[i][j].x, liliPads[i][j].y, true, false);
                        } else if (liliPads[i][j].is_there_a_red_frog == true) {
                            liliPads[i][j].is_there_a_red_frog = false;
                            draggedfrog = new LiliPads(liliPads[i][j].x, liliPads[i][j].y, false, true);
                            dragfrog = true;
                        }
                        drag = true;
                        break outerloop;

                    }
                }
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (drag == true) {

                if (dragfrog == false) {
                    draggedfrog = new LiliPads(x, y, true, false);
                } else {
                    draggedfrog = new LiliPads(x, y, false, true);
                }
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            drag = false;
            found = false;
            outerloop:
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if ((liliPads[i][j] != null) && (Math.pow(liliPads[i][j].x - x, 2) + Math.pow(liliPads[i][j].y - y, 2) < Math.pow(rad, 2))
                            && (liliPads[i][j].hasfrog == false) && (liliPads[i][j].is_there_a_red_frog == false) && (draggedfrog != null)) {
                        found = true;

                        if ((starty != j) && (startx == i) && (liliPads[i][2] != null) && (liliPads[i][2].hasfrog == true)
                                && (Math.abs(starty - j) == 4) && (liliPads[i][2].is_there_a_red_frog == false)) {
                            liliPads[i][2].hasfrog = false;
                            if (dragfrog == false) {
                                liliPads[i][j].hasfrog = true;
                                moves.add(new Move(startx, starty, i, j, i, 2, false));
                            } else {
                                liliPads[i][j].is_there_a_red_frog = true;
                                dragfrog = false;
                                moves.add(new Move(startx, starty, i, j, i, 2, true));
                            }
                        }
                        //horisontal
                        else if ((starty == j) && (startx != i) && (liliPads[2][j] != null) && (liliPads[2][j].hasfrog == true)
                                && (Math.abs(startx - i) == 4) && (liliPads[2][j].is_there_a_red_frog == false)) {
                            liliPads[2][j].hasfrog = false;
                            if (dragfrog == false) {
                                liliPads[i][j].hasfrog = true;
                                moves.add(new Move(startx, starty, i, j, 2, j, false));
                            } else {
                                liliPads[i][j].is_there_a_red_frog = true;
                                dragfrog = false;
                                moves.add(new Move(startx, starty, i, j, 2, j, true));
                            }
                        }
                        //vertical
                        else if ((Math.abs(startx - i) == 2) && (Math.abs(starty - j) == 2)) {

                            if (i > startx) {

                                if (j > starty) {
                                    if ((liliPads[startx + 1][starty + 1].is_there_a_red_frog == false) && (liliPads[startx + 1][starty + 1].hasfrog == true)) {
                                        liliPads[startx + 1][starty + 1].hasfrog = false;
                                        if (dragfrog == false) {
                                            moves.add(new Move(startx, starty, i, j, startx + 1, starty + 1, false));
                                            liliPads[i][j].hasfrog = true;
                                        } else {
                                            moves.add(new Move(startx, starty, i, j, startx + 1, starty + 1, true));
                                            liliPads[i][j].is_there_a_red_frog = true;
                                            dragfrog = false;
                                        }
                                    } else found = false;
                                } else if (j < starty) {
                                    if ((liliPads[startx + 1][starty - 1].is_there_a_red_frog == false) && (liliPads[startx + 1][starty - 1].hasfrog == true)) {
                                        liliPads[startx + 1][starty - 1].hasfrog = false;
                                        if (dragfrog == false) {
                                            moves.add(new Move(startx, starty, i, j, startx + 1, starty - 1, false));
                                            liliPads[i][j].hasfrog = true;
                                        } else {
                                            moves.add(new Move(startx, starty, i, j, startx + 1, starty - 1, true));
                                            liliPads[i][j].is_there_a_red_frog = true;
                                        }
                                        dragfrog = false;
                                    } else found = false;
                                }

                            } else if (i < startx) {

                                if (j > starty) {
                                    if ((liliPads[startx - 1][starty + 1].is_there_a_red_frog == false) && (liliPads[startx - 1][starty + 1].hasfrog == true)) {

                                        liliPads[startx - 1][starty + 1].hasfrog = false;

                                        if (dragfrog == false) {
                                            moves.add(new Move(startx, starty, i, j, startx - 1, starty + 1, false));
                                            liliPads[i][j].hasfrog = true;
                                        } else {
                                            liliPads[i][j].is_there_a_red_frog = true;
                                            moves.add(new Move(startx, starty, i, j, startx - 1, starty + 1, true));
                                        }
                                        dragfrog = false;
                                    } else found = false;

                                } else if (j < starty) {

                                    if ((liliPads[startx - 1][starty - 1].is_there_a_red_frog == false) && (liliPads[startx - 1][starty - 1].hasfrog == true)) {
                                        liliPads[startx - 1][starty - 1].hasfrog = false;
                                        if (dragfrog == false) {
                                            moves.add(new Move(startx, starty, i, j, startx - 1, starty - 1, false));
                                            liliPads[i][j].hasfrog = true;
                                        } else {
                                            moves.add(new Move(startx, starty, i, j, startx - 1, starty - 1, true));
                                            liliPads[i][j].is_there_a_red_frog = true;
                                            dragfrog = false;
                                        }
                                    } else found = false;

                                } else found = false;
                            }

                            break outerloop;
                        }
                        else found = false;
                    }
                }
            }

            if (found == false && startx != -1 && starty != -1) {
                if (dragfrog == false)
                    liliPads[startx][starty].hasfrog = true;
                else {
                    liliPads[startx][starty].is_there_a_red_frog = true;
                    dragfrog = false;
                }
            }

            draggedfrog = null;
            return true;
        }
        return super.onTouchEvent(event);
    }

}