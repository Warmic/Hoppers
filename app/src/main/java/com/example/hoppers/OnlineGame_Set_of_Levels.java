package com.example.hoppers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Peter on 18.05.2017.
 */

public class OnlineGame_Set_of_Levels extends Activity {

    ArrayList<String> list_recieved_levels;
    ArrayList<Integer> iviewsids;
    ArrayList<Integer> data;
    List<Integer> buttonids;
    String recieved_level;
    int enemy_token;
    Timer t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game_levels_layout);

        t = new Timer();
        t.schedule(new TimerTask() {

            public void run() {

                if (isNetworkAvailable()==true) {

                        JSONObject jsononbject = new JSONObject();
                        try {
                            jsononbject.put("action", "game_state");
                            jsononbject.put("token",enemy_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Get_JSON_Reply_class().execute(jsononbject.toString());
                }
                else {
                    Toast.makeText(getBaseContext(),"Check Your Network Connection",Toast.LENGTH_SHORT).show();
                }

            }
        }, 1000);

        data = new ArrayList<>();
        buttonids = new ArrayList(){};
        list_recieved_levels = new ArrayList();

        buttonids.add(R.id._1);
        buttonids.add(R.id._2);
        buttonids.add(R.id._3);
        buttonids.add(R.id._4);
        buttonids.add(R.id._5);

        iviewsids = new ArrayList<>();

        iviewsids.add(R.id._iv1);
        iviewsids.add(R.id._iv2);
        iviewsids.add(R.id._iv3);
        iviewsids.add(R.id._iv4);
        iviewsids.add(R.id._iv5);


        DatabaseHandler dbh = new DatabaseHandler(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Intent recieved = getIntent();

        if (dbh.check_if_exists(db,DatabaseHandler.TABLE_ONLINE_PROFILE) == false) {

            dbh.create_online_profile(db);
            db = dbh.getWritableDatabase();

            if (recieved.getStringExtra("Level") != null) {
                recieved_level = recieved.getStringExtra("Level");
                int pos = 0;

                for (int i = 0; i < recieved_level.length(); i++) {

                    if (recieved_level.charAt(i) == ',') {
                        if (pos == 0)

                            list_recieved_levels.add(recieved_level.substring(0, i));

                        else list_recieved_levels.add(recieved_level.substring(pos + 1, i));

                        pos = i;
                    }
                }
                if (recieved_level.length() < 100) list_recieved_levels.add(recieved_level);


                for (int i = 0; i < list_recieved_levels.size(); i++) {
                    String map = list_recieved_levels.get(i);
                    db.execSQL("INSERT OR REPLACE INTO " + DatabaseHandler.TABLE_ONLINE_PROFILE + "(" + DatabaseHandler.LEVEL + ","
                            + DatabaseHandler.MAP+ "," + DatabaseHandler.COMPLETED + ")" +
                            " VALUES ( "+ i+", '"+ map+"' ,"+0 +")");

                }
            }
        }
        else {

           String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_ONLINE_PROFILE +
                   " where "+ DatabaseHandler.MAP +" IS NOT NULL " ;

                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {

                        list_recieved_levels.add(cursor.getString(cursor.getColumnIndex("map")));

                        data.add(cursor.getInt(cursor.getColumnIndex("completed")));

                    } while (cursor.moveToNext());
                }

                cursor.close();
        }

        int amount_of_levels = list_recieved_levels.size();

        for (int i = 4; i > amount_of_levels - 1; i--) {
            Button button = (Button) findViewById(buttonids.get(i));
            button.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < data.size(); i++) {

            Integer integ = data.get(i);

            if (integ == 1) {
                ImageView image = (ImageView) findViewById(iviewsids.get(i));
                image.setVisibility(View.VISIBLE);
            }
        }

        if (recieved.getIntExtra("token",0)!=0) enemy_token = recieved.getIntExtra("token",0);

        db.close();
        dbh.close();
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getBaseContext(),OnlineGame.class);
        startActivity(setIntent);
    }

    public void onClick(View v){
        if (v.getTag()!=null) {

            int tag = Integer.parseInt(v.getTag().toString())-1;

            Intent intent = new Intent(getBaseContext(), ChosenLevel.class);

            intent.putExtra("IsOnline","xd");

            intent.putExtra("Level",Integer.parseInt(list_recieved_levels.get(tag).substring(0,2)));

            intent.putExtra("Map",list_recieved_levels.get(tag).substring(2, list_recieved_levels.get(tag).length()));

            if (tag+1<list_recieved_levels.size()) intent.putExtra("NextMap",list_recieved_levels.get(tag+1));

            intent.putExtra("Current",tag);

            intent.putExtra("Total",list_recieved_levels.size());

            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

public class Get_JSON_Reply_class extends AsyncTask<String,Void,String> {

    String request;

    @Override
    protected String doInBackground(String... params) {

        try {
            request = params[0];

            String set_server_url = "http://194.176.114.21:8080";

            URL url = new URL(set_server_url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);

            OutputStream out = urlConnection.getOutputStream();

            out.write(params[0].getBytes());

            Scanner in = new Scanner(urlConnection.getInputStream());
            String completeReply = "";
            if (in.hasNext()) {
                completeReply = in.nextLine();
            }
            urlConnection.disconnect();

            return completeReply;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {

        if (request != null && s.equals("") == false) {

            try {
                JSONObject Jrequest = new JSONObject(request);
                JSONObject Jresponse = new JSONObject(s);

                if (Jrequest.getString("action").equals("game_state")){

                    if (Jresponse.getString("status").equals("error")==false){

                        JSONArray arr = Jresponse.getJSONArray("maps");
                        String res = "Your opponent has already finished:";
                        for (int i = 0; i < arr.length(); i++) {
                            if ((boolean)arr.get(i)==true) {res+=(i+1)+" ";}
                        }
                     if (res.equals("Your opponent has already finished:")==false)
                         Toast.makeText(getBaseContext(), res+" maps", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
}
