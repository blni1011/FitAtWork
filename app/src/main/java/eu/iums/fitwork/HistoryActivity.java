package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;


    private TextView toolbarFitpointsField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verlauf");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        ListView history_list = (ListView) findViewById(R.id.listView);

        String[] modes = new String[] {"Fuß", "Fahrrad", "ÖPNV", "E-Scooter", "MIV-Fahrer", "MIV-Mitfahrer", "Sonstiges"};
        ArrayList<String> modesList = new ArrayList<>();
        modesList.addAll(Arrays.asList(modes));

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.textView, modesList);

        history_list.setAdapter(listAdapter);

        history_list.setOnItemClickListener(this);
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