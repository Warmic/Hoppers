package com.example.hoppers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;



public class MainActivity extends AppCompatActivity {




    public Pond pond;
    public Button move;


    @Override
        protected void onCreate(Bundle savedInstanceState) {



        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        move = (Button) findViewById(R.id.stepback);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Pond.class);
                startActivity(intent);
            }
        });
        }



}
