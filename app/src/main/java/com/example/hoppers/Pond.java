package com.example.hoppers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import static com.example.hoppers.MainActivity.diffx;
import static com.example.hoppers.MainActivity.diffy;


public class Pond extends View {


    public LiliPads draggedfrog = null;
    public LiliPads liliPads[][] = new LiliPads[5][5];

    //We can limit amount of turns by limiting capacity of storage

    public ArrayList<Move> moves = new ArrayList<Move>();
    public ArrayList<Point> points = new ArrayList<>();


    public final int rad = 70;
    final double deg = Math.toRadians(45);

    Bitmap draggedmap;
    Bitmap redmap;
    Bitmap frogmap;
    Bitmap lilimap;
    Bitmap background;

    boolean BackgroundUp;
    boolean PadsUp;


    public Pond(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);


        draggedmap = BitmapFactory.decodeResource(getResources(), R.drawable.frog);
        redmap = BitmapFactory.decodeResource(getResources(), R.drawable.redfrog);
        frogmap = BitmapFactory.decodeResource(getResources(), R.drawable.frog);
        lilimap = BitmapFactory.decodeResource(getResources(), R.drawable.lilipad);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.images);

        float aspectRatio = lilimap.getWidth() / (float) lilimap.getHeight();
        int width = diffx;
        int height = Math.round(width / aspectRatio);

        lilimap = Bitmap.createScaledBitmap(lilimap, width, height, false);

        aspectRatio = frogmap.getWidth() / frogmap.getHeight();
        height = Math.round(width / aspectRatio);

        frogmap = Bitmap.createScaledBitmap(frogmap, width, height, true);
        redmap = Bitmap.createScaledBitmap(redmap, width, height, true);
        background = Bitmap.createScaledBitmap(background, diffx * 6, diffy * 7, true);

    }


    public void onDraw(Canvas canvas) {

        Paint paint = new Paint();


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

        invalidate();
    }


    //------------------------------------------------------------------------------------------------------------------------------
    //FINISHEDRAWING

    public void setup(LiliPads arr[][]) {
        Random rand = new Random();


        int starti = rand.nextInt(5);
        int startj = rand.nextInt(5);


            int tries = 0;
            int totaltries =0;
            points.clear();
            while (arr[starti][startj] == null) {
                starti = rand.nextInt(5);
                startj = rand.nextInt(5);
            }
            points.add(new Point(starti, startj));
            arr[starti][startj].hasfrog = true;


            while (points.size() < 7) {

                int size = points.size();

                for (int i = 0; i < size; i++) {
                    if (points.get(i).x % 2 == 0)
                        check_if_i_2(points.get(i).x, points.get(i).y, arr);
                    if (points.get(i).x % 2 == 1)
                        check_if_i_1(points.get(i).x, points.get(i).y, arr);
                    Log.d("TAG", " " + points.size() + " " + 2);
                    tries++;
                    totaltries++;
                }

                    if (tries > 500) {
                        tries = 0;
                        points.clear();
                        arr[starti][startj].hasfrog=false;
                        starti = rand.nextInt(5);
                        startj = rand.nextInt(5);
                        while (arr[starti][startj] == null) {
                            starti = rand.nextInt(5);
                            startj = rand.nextInt(5);
                        }
                        points.add(new Point(starti, startj));
                        arr[starti][startj].hasfrog = true;
                    }
                Log.d("TAGB",totaltries+"");

            }
                Log.d("TAGB",totaltries+"");
            }


    public LiliPads[][] check_if_i_1(int starti, int startj, LiliPads arr[][]) {


            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 2][startj + 2].hasfrog == false)) {
                arr[starti + 1][startj + 1].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti + 2][startj + 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti, startj)));
                points.add(new Point(starti + 1, startj + 1));
                points.add(new Point(starti + 2, startj + 2));

                return arr;
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)) {

                arr[starti + 1][startj - 1].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti + 2][startj - 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti, startj)));
                points.add(new Point(starti + 1, startj - 1));
                points.add(new Point(starti + 2, startj - 2));

                return arr;
            } else if ((starti > 3) && (startj > 3) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)) {
                arr[starti - 1][startj - 1].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti - 2][startj - 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti, startj)));
                points.add(new Point(starti - 1, startj - 1));
                points.add(new Point(starti - 2, startj - 2));

                return arr;
            } else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)) {
                arr[starti - 1][startj + 1].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti - 2][startj + 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti, startj)));
                points.add(new Point(starti - 1, startj + 1));
                points.add(new Point(starti - 2, startj + 2));

                return arr;
            } else return arr;
        }


    public LiliPads[][] check_if_i_2(int starti, int startj, LiliPads arr[][]) {

        if (startj == 0) {

            if ((arr[starti][startj+4].hasfrog==false)&&(arr[starti][startj+2].hasfrog==false)){
                arr[starti ][startj + 4].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti ][startj+2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti,startj+4));
                points.add(new Point(starti,startj+2));

                return arr;
            }
            else
            if ((starti==0)&&(arr[starti+2][startj].hasfrog==false)&&(arr[starti+4][startj].hasfrog==false)){
                arr[starti +4][startj ].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti +2][startj].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti+4,startj));
                points.add(new Point(starti+2,startj));

                return arr;
            }
            else
                if ((starti==4)&&(arr[starti-2][startj].hasfrog==false)&&(arr[starti-4][startj].hasfrog==false)){
                    arr[starti -4][startj ].hasfrog = true;
                    arr[starti][startj].hasfrog = false;
                    arr[starti -2][startj].hasfrog = true;

                    points.remove(points.indexOf(new Point(starti,startj)));
                    points.add(new Point(starti-4,startj));
                    points.add(new Point(starti-2,startj));

                    return arr;
                }


            else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 2][startj + 2].hasfrog == false)) {

                arr[starti + 2][startj + 2].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti + 1][startj+1].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti+1,startj+1));
                points.add(new Point(starti+2,startj+2));

                return arr;
            } else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)) {

                arr[starti][startj].hasfrog = false;
                arr[starti - 1][startj+1].hasfrog = true;
                arr[starti - 2][startj + 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti-1,startj+1));
                points.add(new Point(starti-2,startj+2));

                return arr;
            } else return arr;
        }
//-------------------------------------------------------------------------------------
        else if (startj == 4) {

            if ((arr[starti][startj-4].hasfrog==false)&&(arr[starti][startj-2].hasfrog==false)){
                arr[starti ][startj - 4].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti ][startj-2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti,startj-4));
                points.add(new Point(starti,startj-2));

                return arr;
            }
            else
            if ((starti==0)&&(arr[starti+2][startj].hasfrog==false)&&(arr[starti+4][startj].hasfrog==false)){
                arr[starti +4][startj ].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti +2][startj].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti+4,startj));
                points.add(new Point(starti+2,startj));

                return arr;
            }
            else if ((starti==0)&&(arr[starti+2][startj].hasfrog==false)&&(arr[starti+4][startj].hasfrog==false)){
                arr[starti +4][startj ].hasfrog = true;
                arr[starti][startj].hasfrog = false;
                arr[starti +2][startj].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti+4,startj));
                points.add(new Point(starti+2,startj));

                return arr;
            }
            if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)) {

                arr[starti][startj].hasfrog = false;
                arr[starti + 1][startj-1].hasfrog = true;
                arr[starti + 2][startj - 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti+1,startj-1));
                points.add(new Point(starti+2,startj-2));

                return arr;
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)) {

                arr[starti][startj].hasfrog = false;
                arr[starti - 1][startj-1].hasfrog = true;
                arr[starti - 2][startj - 2].hasfrog = true;

                points.remove(points.indexOf(new Point(starti,startj)));
                points.add(new Point(starti-1,startj-1));
                points.add(new Point(starti-2,startj-2));

                return arr;
            } else return arr;
        }
        else if (startj == 2) {
            return check_if_i_1(starti, startj, arr);
        }
        else return arr;
    }

}


