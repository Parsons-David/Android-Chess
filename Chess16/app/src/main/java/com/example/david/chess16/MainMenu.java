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


    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        btnPlay = (Button) findViewById(R.id.btnPlay);

        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
        try {
            Engine.load();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Previous Seesion Not Loaded", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPlay(View v) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, PlayChess.class);
        intent.putExtras(bundle);
        startActivity(intent);
        // ADD TITLE AND RECORD
    }

    public void showWatch(View v){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, WatchMenu.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
