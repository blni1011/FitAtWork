package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity{
    /**
     * HistoryActivity:
     * Anzeige der persönlichen Übungs-History des Users
     * Anzeige als CardView mit Hilfe des Layouts aus der Übungsanzeige
     */

    Toolbar toolbar;
    HistoryRecyclerAdapter adapter;
    RecyclerView recyclerView;

    private TextView toolbarFitpointsField;

    private ArrayList<String> exerciseNames;
    private ArrayList<String> times;

    private FirebaseUser fbUser;
    private User user;
    private UserDBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        database = new UserDBHelper();


        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verlauf");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        //RecyclerView
        recyclerView = findViewById(R.id.history_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseNames = new ArrayList<>();
        times = new ArrayList<>();
        adapter = new HistoryRecyclerAdapter(this, exerciseNames, times);
        recyclerView.setAdapter(adapter);

        //Auslesen der getätigten Übungen aus der Datenbank
        if(fbUser != null) {
            user = new User(fbUser.getDisplayName());

            database.getDatabase().child(fbUser.getDisplayName()).child(database.DB_HISTORY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    exerciseNames.clear();
                    times.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String time = dataSnapshot.getKey();
                        String exercise = dataSnapshot.getValue(String.class);
                        exerciseNames.add(exercise);
                        times.add(time);
                    }
                    adapter.notifyDataSetChanged();
                    Log.i("HistoryActivity/getData", "Laden der History erfolgreich");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("HistoryActivity/getData", "Fehler beim laden der History");
                }
            });

        }
    }
}