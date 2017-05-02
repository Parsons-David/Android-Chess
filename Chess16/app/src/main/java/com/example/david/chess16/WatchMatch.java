package com.example.david.chess16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.david.chess16.control.Engine;
import com.example.david.chess16.control.Match;
import com.example.david.chess16.control.WatchableMatch;

public class WatchMatch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_chess);

        int matchNum = getIntent().getExtras().getInt("Match");

        Toast.makeText(getApplicationContext(), "Watching Match " + String.valueOf(matchNum) , Toast.LENGTH_SHORT).show();

        WatchableMatch m = Engine.getMatchByIndex(matchNum);

        if(m == null){

            Toast.makeText(getApplicationContext(), "Match not Found" , Toast.LENGTH_SHORT).show();
        }

    }
}
