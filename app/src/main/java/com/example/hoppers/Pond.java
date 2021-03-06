package com.example.hoppers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class Pond extends View {


    public LiliPads draggedfrog = null;
    public LiliPads liliPads[][] = new LiliPads[5][5];
    public ArrayList<Move> moves = new ArrayList<Move>();
    public ArrayList<Point> points = new ArrayList();


    private int startx = -1;
    private int starty = -1;
    private int diffx;
    private int diffy;
    public int amountoffrogs;
    public  int rad;
    public int current;
    public int total;
    private int startingi;
    private int startingj;


    private final double deg = Math.toRadians(45);

    private Bitmap redmap = BitmapFactory.decodeResource(getResources(), R.drawable.redfrog);
    private Bitmap frogmap = BitmapFactory.decodeResource(getResources(), R.drawable.frog);
    private Bitmap lilimap = BitmapFactory.decodeResource(getResources(), R.drawable.lilipad);
    private Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.pondbackground);


    public String thislevel = "";
    public String nextmap;
    public String map;

    private boolean dragfrog;
    private boolean drag;
    private boolean drawn;
    private boolean messageshown;
    public boolean randomlevel;
    public boolean levelfinished;
    public boolean setupcomplete;
    public boolean online;
    public boolean story;


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
        //setting up an empty field
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    count++;
                    if (count % 2 == 0) {
                        liliPads[i][j] = new LiliPads(diffx * i + diffx, j * diffy + diffy, false, false);
                    }
                }
            }

            if (setupcomplete == false) Pond.this.setup(liliPads);
        //getting map out of string
            else if (map != null) {
                for (int i = 0; i < map.length(); i += 5) {
                    int x = map.charAt(i) - 48;
                    int y = map.charAt(i + 1) - 48;
                    boolean fr;
                    boolean rfr;
                    if (map.charAt(i + 2) == 't') {
                        fr = true;
                        rfr = false;
                    } else if (map.charAt(i + 3) == 't') {
                        fr = false;
                        rfr = true;
                    } else {
                        fr = false;
                        rfr = false;
                    }
                    liliPads[x][y].hasfrog = fr;
                    liliPads[x][y].is_there_a_red_frog = rfr;
                    if (points.size() != amountoffrogs) points.add(new Point(1, 1));
                }

            }
        }
        //scaling bitmaps properly
        if (drawn == false) {

            drawn = true;

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
                rad = canvas.getHeight()/9;

            }
        }

        canvas.drawBitmap(background, 0, 0, paint);

        paint.setStrokeWidth(6);
        paint.setColor(Color.BLACK);
        //drawing lines
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
                    if ((j == 0) && (i == 2)) {
                        canvas.drawLine(x, y, x, y + diffy * 2, paint);
                    }
                    if ((j == 4) && (i == 2)) {
                        canvas.drawLine(x, y, x, y - diffy * 2, paint);
                    }

                }

            }

        }

        //drawing bitmaps
        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {

                if (liliPads[i][j] != null) {

                    int x = (int) liliPads[i][j].x;
                    int y = (int) liliPads[i][j].y;


                    canvas.drawBitmap(lilimap, liliPads[i][j].x - lilimap.getWidth() / 2, liliPads[i][j].y - lilimap.getHeight() / 2, paint);

                    if (liliPads[i][j].hasfrog == true)
                        canvas.drawBitmap(frogmap, x - frogmap.getWidth() / 2, y - frogmap.getHeight() / 2, paint);
                    if (liliPads[i][j].is_there_a_red_frog == true)
                        canvas.drawBitmap(redmap, x - redmap.getWidth() / 2, y - redmap.getHeight() / 2, paint);
                }
            }

        }

        if (draggedfrog != null) {
            if (draggedfrog.is_there_a_red_frog == false)
                canvas.drawBitmap(frogmap, draggedfrog.x - frogmap.getWidth() / 2, draggedfrog.y - frogmap.getHeight() / 2, paint);
            else {
                canvas.drawBitmap(redmap, draggedfrog.x - redmap.getWidth() / 2, draggedfrog.y - redmap.getHeight() / 2, paint);
            }
        }


        if (moves.size() == points.size() - 1) {
            //checking if level has been finished
            levelfinished = true;
            if (current > 0 && story) {

                DatabaseHandler dbh = new DatabaseHandler(getContext());

                SQLiteDatabase db = dbh.getWritableDatabase();

                String insertQuery = "UPDATE difficulties  SET completed=1 where difficulty is'" + points.size() + "' AND level is '" + current + "'";

                db.execSQL(insertQuery);

                db.close();
                dbh.close();
            } else if (online) {
                DatabaseHandler dbh = new DatabaseHandler(getContext());

                SQLiteDatabase db = dbh.getWritableDatabase();

                String insertQuery = "UPDATE online_profile SET completed=1 where level is'" +
                        current + "'";

                db.execSQL(insertQuery);

                db.close();
                dbh.close();
            }
        } else levelfinished = false;

        if (levelfinished && messageshown == false) {
            if (online) {
                if (current < total) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Congratulations! \n Next level?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(getContext(), ChosenLevel.class);
                                    intent.putExtra("Map", nextmap);
                                    intent.putExtra("IsOnline", "xd");
                                    intent.putExtra("Current",current + 1);
                                    intent.putExtra("Level", Integer.parseInt(nextmap.substring(0, 2)));
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
                }
            }
            if (story) {
                if (current < total) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Congratulations! \n Next level?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(getContext(), ChosenLevel.class);
                                    intent.putExtra("Map", nextmap);
                                    intent.putExtra("IsStory", "xd");
                                    intent.putExtra("Current", current + 1);
                                    intent.putExtra("Level", points.size());
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Congratulations! \n Do you want to choose another difficulty?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(getContext(), Set_of_Story_Difficulties.class);
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
                }
            }

            if (randomlevel) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("    Congratulations!     \n     Next level?     ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getContext(), ChosenLevel.class);
                                intent.putExtra("Random", "rand");
                                intent.putExtra("Level", points.size());
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
            }
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

        thislevel = "";


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
        //adding a first point

        outerloop:
        while (points.size() != amountoffrogs) {
            //running function as long as we don't get requested amount of frogs

            int size = points.size();

            for (int i = 0; i < size; i++) {

                if (points.size() == amountoffrogs) break outerloop;
                if (points.get(i).x % 2 == 0)
                    check_if_i_2(points.get(i).x, points.get(i).y, arr);

                if (points.size() == amountoffrogs) break outerloop;
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

                points.add(new Point(starti, startj));
                startingi = starti;
                startingj = startj;
                arr[starti][startj].is_there_a_red_frog = true;

            }

        }

        //writing an entire level in a string
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (arr[i][j] != null) {
                    thislevel += "" + i + "" + j;
                    if (arr[i][j].hasfrog) thislevel += "t";
                    else thislevel += "f";
                    if (arr[i][j].is_there_a_red_frog) thislevel += "t";
                    else thislevel += "f";
                } else thislevel += "n";
            }
        }

        setupcomplete = true;
    }


    //----------------------------------------------------------------preparing functions for an algorithm
    public LiliPads[][] j_is_0_check_bottom(int starti, int startj, LiliPads arr[][]) {

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
    }

    public LiliPads[][] i_is_0_check_right(int starti, int startj, LiliPads arr[][]) {

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

    public LiliPads[][] i_is_4_check_left(int starti, int startj, LiliPads arr[][]) {

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


    public LiliPads[][] j_is_4_check_top(int starti, int startj, LiliPads arr[][]) {

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
    }


    public LiliPads[][] check_if_i_2(int starti, int startj, LiliPads arr[][]) {


        //some starting points require a certain order of functions in order to construct a level

        if (
                startingi == 1 && startingj == 1) {
            if ((startj == 0) && (arr[starti][startj + 4].hasfrog == false) && (arr[starti][startj + 2].hasfrog == false)
                    && (arr[starti][startj + 4].is_there_a_red_frog == false) && (arr[starti][startj + 2].is_there_a_red_frog == false)) {
                return j_is_0_check_bottom(starti, startj, arr);
            } else if ((starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                    && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
                return i_is_0_check_right(starti, startj, arr);
            } else if ((starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                    && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
                return i_is_4_check_left(starti, startj, arr);
            } else if ((startj == 4) && (arr[starti][startj - 4].hasfrog == false) && (arr[starti][startj - 2].hasfrog == false)
                    && (arr[starti][startj - 4].is_there_a_red_frog == false) && (arr[starti][startj - 2].is_there_a_red_frog == false)) {
                return j_is_4_check_top(starti, startj, arr);
            } else return check_if_i_1(starti, startj, arr);
        } else if (startingi == 3 && startingj == 1 || startingi == 4 && startingj == 2 || startingi == 0 && startingj == 2) {
            if ((starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                    && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
                return i_is_4_check_left(starti, startj, arr);
            } else if ((startj == 0) && (arr[starti][startj + 4].hasfrog == false) && (arr[starti][startj + 2].hasfrog == false)
                    && (arr[starti][startj + 4].is_there_a_red_frog == false) && (arr[starti][startj + 2].is_there_a_red_frog == false)) {
                return j_is_0_check_bottom(starti, startj, arr);
            } else if ((starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                    && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
                return i_is_0_check_right(starti, startj, arr);
            } else if ((startj == 4) && (arr[starti][startj - 4].hasfrog == false) && (arr[starti][startj - 2].hasfrog == false)
                    && (arr[starti][startj - 4].is_there_a_red_frog == false) && (arr[starti][startj - 2].is_there_a_red_frog == false)) {
                return j_is_4_check_top(starti, startj, arr);
            } else return check_if_i_1(starti, startj, arr);
        } else if (startingi == 1 && startingj == 3) {
            if ((startj == 4) && (arr[starti][startj - 4].hasfrog == false) && (arr[starti][startj - 2].hasfrog == false)
                    && (arr[starti][startj - 4].is_there_a_red_frog == false) && (arr[starti][startj - 2].is_there_a_red_frog == false)) {
                return j_is_4_check_top(starti, startj, arr);
            } else if ((starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                    && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
                return i_is_4_check_left(starti, startj, arr);
            } else if ((startj == 0) && (arr[starti][startj + 4].hasfrog == false) && (arr[starti][startj + 2].hasfrog == false)
                    && (arr[starti][startj + 4].is_there_a_red_frog == false) && (arr[starti][startj + 2].is_there_a_red_frog == false)) {
                return j_is_0_check_bottom(starti, startj, arr);
            } else if ((starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                    && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
                return i_is_0_check_right(starti, startj, arr);
            } else return check_if_i_1(starti, startj, arr);
        } else if (startingi == 3 && startingj == 3) {
            if ((starti == 4) && (arr[starti - 2][startj].hasfrog == false) && (arr[starti - 4][startj].hasfrog == false)
                    && (arr[starti - 2][startj].is_there_a_red_frog == false) && (arr[starti - 4][startj].is_there_a_red_frog == false)) {
                return i_is_4_check_left(starti, startj, arr);
            } else if ((startj == 0) && (arr[starti][startj + 4].hasfrog == false) && (arr[starti][startj + 2].hasfrog == false)
                    && (arr[starti][startj + 4].is_there_a_red_frog == false) && (arr[starti][startj + 2].is_there_a_red_frog == false)) {
                return j_is_0_check_bottom(starti, startj, arr);
            } else if ((startj == 4) && (arr[starti][startj - 4].hasfrog == false) && (arr[starti][startj - 2].hasfrog == false)
                    && (arr[starti][startj - 4].is_there_a_red_frog == false) && (arr[starti][startj - 2].is_there_a_red_frog == false)) {
                return j_is_4_check_top(starti, startj, arr);
            } else if ((starti == 0) && (arr[starti + 2][startj].hasfrog == false) && (arr[starti + 4][startj].hasfrog == false)
                    && (arr[starti + 2][startj].is_there_a_red_frog == false) && (arr[starti + 4][startj].is_there_a_red_frog == false)) {
                return i_is_0_check_right(starti, startj, arr);
            } else return check_if_i_1(starti, startj, arr);
        } else return check_if_i_1(starti, startj, arr);
    }

//-------------------------------------------------------------------- preparing functions for an algorithm

    public LiliPads[][] i_gr_1_j_gr_1(int i, int j, LiliPads arr[][]) {
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

    public LiliPads[][] i_les_3_j_les_3(int i, int j, LiliPads arr[][]) {
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

    public LiliPads[][] i_gr_1_j_les_3(int i, int j, LiliPads arr[][]) {

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

    public LiliPads[][] i_les_3_j_gr_1(int i, int j, LiliPads arr[][]) {

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

        //some starting points require a certain order of functions in order to construct a level

        if (startingi == 4 && startingj == 2) {

            if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
            } else return arr;
        } else if (startingi == 2 && startingj == 0) {
            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else return arr;
        } else if (startingi > 1 && startingj > 1) {
            if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
            } else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else return arr;
        } else if (startingi > 1 && startingj < 3) {

            if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
            } else if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else return arr;
        } else if (startingi < 3 && startingj < 3) {
            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
            } else return arr;
        } else {
            if ((starti < 3) && (startj < 3) && (arr[starti + 1][startj + 1].hasfrog == false) && (arr[starti + 1][startj + 1].is_there_a_red_frog == false)
                    && (arr[starti + 2][startj + 2].hasfrog == false) && (arr[starti + 2][startj + 2].is_there_a_red_frog == false)) {
                return i_les_3_j_les_3(starti, startj, arr);
            } else if ((starti > 1) && (startj < 3) && (arr[starti - 1][startj + 1].hasfrog == false) && (arr[starti - 2][startj + 2].hasfrog == false)
                    && (arr[starti - 1][startj + 1].is_there_a_red_frog == false) && (arr[starti - 2][startj + 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_les_3(starti, startj, arr);
            } else if ((starti < 3) && (startj > 1) && (arr[starti + 1][startj - 1].hasfrog == false) && (arr[starti + 2][startj - 2].hasfrog == false)
                    && (arr[starti + 1][startj - 1].is_there_a_red_frog == false) && (arr[starti + 2][startj - 2].is_there_a_red_frog == false)) {
                return i_les_3_j_gr_1(starti, startj, arr);
            } else if ((starti > 1) && (startj > 1) && (arr[starti - 1][startj - 1].hasfrog == false) && (arr[starti - 2][startj - 2].hasfrog == false)
                    && (arr[starti - 1][startj - 1].is_there_a_red_frog == false) && (arr[starti - 2][startj - 2].is_there_a_red_frog == false)) {
                return i_gr_1_j_gr_1(starti, startj, arr);
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
            //checking if a touch landed on an existing frog,and if so then which one:green or red

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
                //dragging a frog along screen
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
                    //checking if this turn is allowed
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
                        } else found = false;
                    }
                }
            }

            //if turn is for some reason not allowed/possible
            if (found == false && startx != -1 && starty != -1) {
                if (dragfrog == false)
                    liliPads[startx][starty].hasfrog = true;
                else {
                    liliPads[startx][starty].is_there_a_red_frog = true;
                    dragfrog = false;
                }
            }
            //if turn is allowed,then seeing if there are any possible turns with a current setup
            if (found == true && moves.size() != points.size() - 1) {
                boolean possible_turn = false;
                outerloop:
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (liliPads[i][j] != null && (liliPads[i][j].hasfrog || liliPads[i][j].is_there_a_red_frog == true)) {
                            if (i % 2 == 1) {
                                if ((i > 1) && (j < 3) && (liliPads[i - 1][j + 1].hasfrog == true) && (liliPads[i - 2][j + 2].hasfrog == false)
                                        && (liliPads[i - 1][j + 1].is_there_a_red_frog == false) && (liliPads[i - 2][j + 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i < 3) && (j > 1) && (liliPads[i + 1][j - 1].hasfrog == true) && (liliPads[i + 2][j - 2].hasfrog == false)
                                        && (liliPads[i + 1][j - 1].is_there_a_red_frog == false) && (liliPads[i + 2][j - 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i < 3) && (j < 3) && (liliPads[i + 1][j + 1].hasfrog == true) && (liliPads[i + 1][j + 1].is_there_a_red_frog == false)
                                        && (liliPads[i + 2][j + 2].hasfrog == false) && (liliPads[i + 2][j + 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i > 1) && (j > 1) && (liliPads[i - 1][j - 1].hasfrog == true) && (liliPads[i - 2][j - 2].hasfrog == false)
                                        && (liliPads[i - 1][j - 1].is_there_a_red_frog == false) && (liliPads[i - 2][j - 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                }
                            } else {
                                if ((j == 0) && (liliPads[i][j + 4].hasfrog == false) && (liliPads[i][j + 2].hasfrog == true)
                                        && (liliPads[i][j + 4].is_there_a_red_frog == false) && (liliPads[i][j + 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i == 0) && (liliPads[i + 2][j].hasfrog == true) && (liliPads[i + 4][j].hasfrog == false)
                                        && (liliPads[i + 2][j].is_there_a_red_frog == false) && (liliPads[i + 4][j].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i == 4) && (liliPads[i - 2][j].hasfrog == true) && (liliPads[i - 4][j].hasfrog == false)
                                        && (liliPads[i - 2][j].is_there_a_red_frog == false) && (liliPads[i - 4][j].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((j == 4) && (liliPads[i][j - 4].hasfrog == false) && (liliPads[i][j - 2].hasfrog == true)
                                        && (liliPads[i][j - 4].is_there_a_red_frog == false) && (liliPads[i][j - 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                }
                                //could be parted into functions
                                if ((i > 1) && (j < 3) && (liliPads[i - 1][j + 1].hasfrog == true) && (liliPads[i - 2][j + 2].hasfrog == false)
                                        && (liliPads[i - 1][j + 1].is_there_a_red_frog == false) && (liliPads[i - 2][j + 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i < 3) && (j > 1) && (liliPads[i + 1][j - 1].hasfrog == true) && (liliPads[i + 2][j - 2].hasfrog == false)
                                        && (liliPads[i + 1][j - 1].is_there_a_red_frog == false) && (liliPads[i + 2][j - 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i < 3) && (j < 3) && (liliPads[i + 1][j + 1].hasfrog == true) && (liliPads[i + 1][j + 1].is_there_a_red_frog == false)
                                        && (liliPads[i + 2][j + 2].hasfrog == false) && (liliPads[i + 2][j + 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                } else if ((i > 1) && (j > 1) && (liliPads[i - 1][j - 1].hasfrog == true) && (liliPads[i - 2][j - 2].hasfrog == false)
                                        && (liliPads[i - 1][j - 1].is_there_a_red_frog == false) && (liliPads[i - 2][j - 2].is_there_a_red_frog == false)) {
                                    possible_turn = true;
                                }
                            }
                        }
                    }
                }
                if (possible_turn == false) Toast.makeText(getContext(),"No possible turns",Toast.LENGTH_SHORT).show();
            }
            draggedfrog = null;
            return true;
        }
        return super.onTouchEvent(event);
    }
}