package com.example.hoppers;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;


/**
 * Created by Peter on 19.04.2017.
 */

public class SetofRandomLevels extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.setofrandomlevels_layout);
        super.onCreate(savedInstanceState);
    }

    public void onClick(View v){
        if (v.getTag()!=null) {
            Intent intent = new Intent(getBaseContext(), ChosenLevel.class);
            intent.putExtra("Level", Integer.parseInt(v.getTag().toString()));
            intent.putExtra("Random","random");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(setIntent);
    }
}
