package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class OnlineGame extends Activity {

    EditText setname;
    Button new_name;
    Button load_levels;
    Button find_opponent;
    TextView opponent;
    TextView name;
    TextView map;
    TextView map_too;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game_layout);

        setname = (EditText) findViewById(R.id.new_name);
        new_name = (Button) findViewById(R.id.new_name_button);
        name = (TextView) findViewById(R.id.name);
        find_opponent = (Button) findViewById(R.id.find_opponent);
        opponent = (TextView) findViewById(R.id.opponent_name);
        map = (TextView) findViewById(R.id.map_text_test);
        map_too = (TextView) findViewById(R.id.map_text_test_too);


        if (name.getText().toString().equals(null)==false){
            new_name.setVisibility(View.VISIBLE);
            setname.setVisibility(View.VISIBLE);
            opponent.setVisibility(View.GONE);
            find_opponent.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            map.setVisibility(View.GONE);
            map_too.setVisibility(View.GONE);
            load_levels.setVisibility(View.GONE);
        }

        new_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (setname.getText().toString().equals("")==false){

                   new Get_JSON_Reply_class().execute("{\"action\" : \"register\", \"nickname\" : \""+setname.getText().toString()+"\"}");

                   name.setText(setname.getText().toString());
                   setname.setVisibility(View.GONE);
                   new_name.setVisibility(View.GONE);

                   name.setVisibility(View.VISIBLE);
                   find_opponent.setVisibility(View.VISIBLE);
                   opponent.setVisibility(View.VISIBLE);
                   load_levels.setVisibility(View.VISIBLE);
               }
            }
        });

        find_opponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Get_JSON_Reply_class().execute("{\"action\" : \"find_opponent\", \"token\" : "+7914803+"}");
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
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

                if (request.contains("find_opponent")) {

                    opponent.setText(s.substring(s.indexOf("opponent_token") + 17, s.indexOf("opponent_token") + 24));
                    map.setText(s.substring(s.indexOf("map") + 7, s.indexOf("map") + 73));

                    map.setVisibility(View.VISIBLE);
                    map_too.setVisibility(View.VISIBLE);

                    SystemClock.sleep(7000);

                    Intent intent = new Intent(getBaseContext(), OnlineGame.class);
                    intent.putExtra("Level", s.substring(s.indexOf("map" + 7, s.indexOf("your_token") - 2)));
                    Toast.makeText(getBaseContext(), "Successful search of an opponent ", Toast.LENGTH_LONG).show();

                }

                if (request.contains("register")) {

                    Toast.makeText(getBaseContext(), "Successful register ", Toast.LENGTH_LONG).show();

                }

            }

        }
    }

}
