package com.example.hoppers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    public Button choselevels;
    public Button storymode;


    @Override
        protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        choselevels = (Button) findViewById(R.id.setofrandomlevels);

        choselevels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(getBaseContext(),SetofRandomLevels.class);
             intent.putExtra("Random","random");
             startActivity(intent);
            }
        });

        storymode = (Button) findViewById(R.id.choosestorylevel);

        storymode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SetofStoryDifficulties.class);
                startActivity(intent);
            }
        });

        }



}
