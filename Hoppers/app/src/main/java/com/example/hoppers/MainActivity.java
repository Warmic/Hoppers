package com.example.hoppers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    public Button choselevels;
    public Button storymode;
    public Button onlineMode;


    @Override
        protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        choselevels = (Button) findViewById(R.id.setofrandomlevels);
        choselevels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(getBaseContext(),Set_of_Random_Levels.class);
             intent.putExtra("Random","random");
             startActivity(intent);
            }
        });

        storymode = (Button) findViewById(R.id.choosestorylevel);
        storymode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Set_of_Story_Difficulties.class);
                startActivity(intent);
            }
        });

        onlineMode = (Button) findViewById(R.id.online);
        onlineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()==true){
                Intent intent = new Intent(getBaseContext(),OnlineGame.class);
                startActivity(intent);
                }
                else {
                    Toast.makeText(getBaseContext(),"Check Your Network Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
