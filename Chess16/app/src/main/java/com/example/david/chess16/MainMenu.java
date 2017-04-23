package com.example.david.chess16;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

public class MainMenu extends Activity {

    private ListView menuListView;
    private String[] menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        menuListView = (ListView)findViewById(R.id.menu_items);
        menuItems = getResources().getStringArray(R.array.menu_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_item, menuItems);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast t = Toast.makeText(getApplicationContext(), (position == 0 ? "You want to Play" : (position == 1 ? "You want to Watch" : "I don't know what you want to do this is an error.")), Toast.LENGTH_SHORT);
                t.show();

                if(position == 0){ // User wants to play
                    showPlay();
                } else if (position == 1){ // User wants to watch
                    showWatch();
                } else {
                    t = Toast.makeText(getApplicationContext(), "Whatcha Doing?", Toast.LENGTH_SHORT);
                    t.show();
                }

            }});

        Toast t = Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT);
        t.show();
    }

    private void showPlay() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, PlayChess.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showWatch(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, WatchChess.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
