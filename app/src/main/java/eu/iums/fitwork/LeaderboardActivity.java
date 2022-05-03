package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class LeaderboardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaderboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView ranks_list = (ListView) findViewById(R.id.listView);

        String[] modes = new String[] {"Fuß", "Fahrrad", "ÖPNV", "E-Scooter", "MIV-Fahrer", "MIV-Mitfahrer", "Sonstiges"};
        ArrayList<String> modesList = new ArrayList<>();
        modesList.addAll(Arrays.asList(modes));

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.textView, modesList);

        ranks_list.setAdapter(listAdapter);

        ranks_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedMode = parent.getItemAtPosition(position).toString();
        Intent setMode = new Intent(TransportationMode.this, MainActivity.class);
        setMode.putExtra(KEY, selectedMode);
        startActivity(setMode);
    }*/
}