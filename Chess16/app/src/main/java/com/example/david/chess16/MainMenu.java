package com.example.david.chess16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    }
}
