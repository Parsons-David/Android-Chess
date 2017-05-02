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

public class MainMenu extends Activity {


    private Button btnPlay;
    private EditText txtTitle;
    private CheckBox cboxRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        cboxRecord = (CheckBox) findViewById(R.id.cboxRecord);

        txtTitle.setEnabled(false);

        txtTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                setRecord(txtTitle);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        Toast t = Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT);
        t.show();
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

    public void setRecord(View v){

//        cboxRecord.isChecked();

        txtTitle.setEnabled(cboxRecord.isChecked());
        btnPlay.setEnabled(cboxRecord.isChecked() ? ! txtTitle.getText().toString().equals("") : true);

    }
}
