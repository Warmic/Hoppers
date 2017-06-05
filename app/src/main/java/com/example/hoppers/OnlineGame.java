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
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class OnlineGame extends Activity {

    EditText setname;
    Button new_name;
    Button find_opponent;
    Button delete_name;
    TextView name;
    LinearLayout setup_layout;
    LinearLayout search_layout;
    LinearLayout name_layout;
    ArrayList<Spinner> spinners = new ArrayList<>();

    ArrayAdapter<String> adapter ;
    String [] levels = new String[] {"0","4","5","6","7","8","9","10","11"};
    String [] levels_extra = new String[] {"0","1","2","3","4","5"};
    String msg = "";

    int [] arr = new int [5];
    int amount_random_levels = 0;
    int amount_not_random_levels = 0;
    int token = 0;
    int count = 0;
    boolean registered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game_layout);


        spinners.add((Spinner)findViewById(R.id.spin0));
        spinners.add((Spinner)findViewById(R.id.spin1));
        spinners.add((Spinner)findViewById(R.id.spin2));
        spinners.add((Spinner)findViewById(R.id.spin3));
        spinners.add((Spinner)findViewById(R.id.spin4));

        setup_layout = (LinearLayout) findViewById(R.id.setup_layout);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        search_layout = (LinearLayout) findViewById(R.id.search_layout);

        setname = (EditText) findViewById(R.id.new_name);
        name = (TextView) findViewById(R.id.name);
        new_name = (Button) findViewById(R.id.new_name_button);
        delete_name = (Button) findViewById(R.id.delete_name);
        find_opponent = (Button) findViewById(R.id.find_opponent);

        DatabaseHandler dbh = new DatabaseHandler(this);
        dbh.create_player_profile(dbh.getWritableDatabase());
        SQLiteDatabase db = dbh.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + DatabaseHandler.TABLE_PLAYER_PROFILE ;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
                do {
                    String expected_nickname = cursor.getString(cursor.getColumnIndex(DatabaseHandler.NICKNAME));

                    if (expected_nickname.equals("") == false) {
                        name.setText("Logged as : " + expected_nickname);
                        registered = true;
                        token = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.TOKEN));
                    }
                } while (cursor.moveToNext());
            }



        cursor.close();
        db.close();
        dbh.close();

        if (name.getText().toString().equals("")==true){
            new_name.setVisibility(View.VISIBLE);
            setname.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            delete_name.setVisibility(View.GONE);
            search_layout.setVisibility(View.GONE);
            setup_layout.setVisibility(View.GONE);
        } else {
            new_name.setVisibility(View.GONE);
            setname.setVisibility(View.GONE);
            delete_name.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.VISIBLE);
            setup_layout.setVisibility(View.VISIBLE);

            getGamesRequest();
        }

        new_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()==true) {
                    if (setname.getText().toString().equals("") == false) {
                        JSONObject jsononbject = new JSONObject();
                        try {
                            jsononbject.put("action", "register");
                            jsononbject.put("nickname", setname.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Get_JSON_Reply_class().execute(jsononbject.toString());
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"Check Your Network Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        find_opponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isNetworkAvailable()==true) {

                if (amount_not_random_levels != 0 || amount_random_levels != 0) {
                        JSONObject jsonobject = new JSONObject();
                        try {
                            jsonobject.put("action", "find_opponent");
                            jsonobject.put("token", token);
                            jsonobject.put("nickname",name.toString());
                            if (amount_random_levels != 0) {
                                jsonobject.put("random", true);
                                jsonobject.put("amount", amount_random_levels);
                            } else if (amount_not_random_levels != 0) {
                                jsonobject.put("random", false);
                                jsonobject.put("amount", amount_not_random_levels);
                                jsonobject.put("difficulties", Arrays.toString(arr));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Get_JSON_Reply_class().execute(jsonobject.toString());
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"Check Your Network Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onRadioButtonClick(View v) {

        LinearLayout spinners_layout = (LinearLayout) findViewById(R.id.spinners_layout);
        msg = v.getTag().toString();
        spinners_layout.setVisibility(View.VISIBLE);


        if (msg.equals("rand") == false) {
            for (Spinner spin : spinners
                    ) {

                adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,levels);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin.setAdapter(adapter);

                spin.setPrompt("LevelChoice");

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        count = 0;
                        int pos = Integer.parseInt(parent.getTag().toString());

                            if (position != 0 && pos + 1 < spinners.size()) {
                                spinners.get(pos + 1).setVisibility(View.VISIBLE);
                                arr[pos] = 1;
                                find_opponent.setClickable(true);
                            }
                            if (pos + 1 == spinners.size()) arr[pos] = Integer.parseInt(levels[position]);

                            if (position == 0 && arr[pos] == 1) arr[pos] = 0;

                            for (int i = 0; i < 5; i++) {
                                if (arr[i] != 0) count++;
                            }

                            amount_not_random_levels = count;
                            //arr - difficulties that will be sent
                            Toast.makeText(getBaseContext(), "count " + count, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else {

            for (int i = 1; i < 5; i++) {
                spinners.get(i).setVisibility(View.GONE);
            }
            Spinner spin = spinners.get(0);

            adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,levels_extra);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin.setAdapter(adapter);

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0) {
                        amount_random_levels = Integer.parseInt(levels_extra[position]);
                        find_opponent.setClickable(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Toast.makeText(getBaseContext(), "amount " + amount_random_levels, Toast.LENGTH_SHORT).show();
        }
    }


    public class Get_JSON_Reply_class extends AsyncTask<String,Void,String> {

        String request;

        @Override
        protected String doInBackground(String... params) {

            try {
                request = params[0];

                String set_server_url = "http://91.242.182.235:8080";

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

            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

            if (request != null && s.equals("") == false) {

                try {

                    JSONObject Jrequest = new JSONObject(request);
                    JSONObject Jresponse = new JSONObject(s);

                    if (Jrequest.getString("action").equals("getGame")){

                        if (Jresponse.getString("status").equals("error")==false){
                         //SUCCESS
                            DatabaseHandler dbh = new DatabaseHandler(getBaseContext());
                            dbh.upgrade_online_profile(dbh.getWritableDatabase());
                            dbh.close();

                            Toast.makeText(getBaseContext(), "Successful search of an opponent ", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getBaseContext(), OnlineGame_Set_of_Levels.class);
                            intent.putExtra("Level", Jresponse.getString("map"));
                            intent.putExtra("enemy_token",Jresponse.getInt("enemy_token"));
                            startActivity(intent);
                        }
                        else {Toast.makeText(getBaseContext(),"No opponent yet",Toast.LENGTH_SHORT).show();}
                    } else

                    if (Jrequest.getString("action").equals("find_opponent") && Jresponse.getString("status").equals("error") == false) {

                        DatabaseHandler dbh = new DatabaseHandler(getBaseContext());
                        dbh.upgrade_online_profile(dbh.getWritableDatabase());
                        dbh.close();

                        Toast.makeText(getBaseContext(), "Successful search of an opponent ", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(), OnlineGame_Set_of_Levels.class);
                        intent.putExtra("Level", Jresponse.getString("map"));
                        intent.putExtra("enemy_token",Jresponse.getInt("enemy_token"));
                        startActivity(intent);
                    }
                    if (Jrequest.getString("action").equals("register")) {

                        if (Jresponse.getString("status").equals("error") == false ) {

                            Toast.makeText(getBaseContext(), "Successful register ", Toast.LENGTH_LONG).show();

                            registered = true;
                            token = Jresponse.getInt("token");
                            name.setText("Logged as : "+setname.getText().toString());

                            DatabaseHandler dbh = new DatabaseHandler(getBaseContext());

                            SQLiteDatabase db = dbh.getWritableDatabase();

                            String insertQuery = "REPLACE INTO " + DatabaseHandler.TABLE_PLAYER_PROFILE+ "(" + DatabaseHandler.NICKNAME+ ","
                                    + DatabaseHandler.TOKEN+ ",count)" + " VALUES ('"+setname.getText().toString()+"',"+token+",0)";

                            db.execSQL(insertQuery);
                            db.close();
                            dbh.close();

                            setname.setVisibility(View.GONE);
                            new_name.setVisibility(View.GONE);

                            delete_name.setVisibility(View.VISIBLE);
                            name.setVisibility(View.VISIBLE);
                            search_layout.setVisibility(View.VISIBLE);
                            setup_layout.setVisibility(View.VISIBLE);

                        } else
                            Toast.makeText(getBaseContext(), "Name already exists", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void onDeleteName(View v){
        search_layout.setVisibility(View.GONE);
        setup_layout.setVisibility(View.GONE);
        setname.setVisibility(View.VISIBLE);
        new_name.setVisibility(View.VISIBLE);
        delete_name.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        setname.setText("");

        disconnectRequest();

        DatabaseHandler dbh = new DatabaseHandler(getBaseContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        dbh.upgrade_player_profile(db);

        String insertQuery = "REPLACE INTO " + DatabaseHandler.TABLE_PLAYER_PROFILE+ "(" + DatabaseHandler.NICKNAME+ ","
                + DatabaseHandler.TOKEN+ ",count)" + " VALUES ('',0,0)";
        db.execSQL(insertQuery);

        db.close();
        dbh.close();
    }

    public void disconnectRequest(){

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action","disconnect");
                jsonObject.put("token",token);
                new Get_JSON_Reply_class().execute(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(),MainActivity.class);

        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getGamesRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("action", "getGame");
            new Get_JSON_Reply_class().execute(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
