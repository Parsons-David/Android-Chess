package com.example.david.chess16;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WatchChess extends Activity {

    private ListView matchListView;
    private String[] matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_list);

        matchListView = (ListView)findViewById(R.id.match_list);
        matches = getResources().getStringArray(R.array.menu_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_item, matches);
        matchListView.setAdapter(adapter);

        matchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast t = Toast.makeText(getApplicationContext(), "You want to Watch Match #" + String.valueOf(position) , Toast.LENGTH_SHORT);
                t.show();

                watchMatch();

            }});

    }

    public void watchMatch(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, WatchMatch.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
