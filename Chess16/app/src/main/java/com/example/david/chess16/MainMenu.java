package com.example.david.chess16;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.example.david.chess16.control.Engine;

public class MainMenu extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        // Load from Previous Session, Do nothing if not file found

//        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
        try {
            Engine.load(getApplicationContext());
        } catch (Exception e){
//            Toast.makeText(getApplicationContext(), "Previous Seesion Not Loaded", Toast.LENGTH_SHORT).show();
        }

    }


    // Move to Play Match Activity
    public void showPlay(View v) {
        Intent intent = new Intent(this, PlayChess.class);
        startActivity(intent);
    }

    // Move to Watch Menu Activity
    public void showWatch(View v){
        Intent intent = new Intent(this, WatchMenu.class);
        startActivity(intent);
    }

}
