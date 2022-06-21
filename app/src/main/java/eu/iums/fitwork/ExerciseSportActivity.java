package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ExerciseSportActivity extends AppCompatActivity {
    /**
    ExerciseSportActivity:
     Anzeige der einzelnen Übungen in einer CardView.
     Übungen werden in Echtzeit aus Datenbank ausgelesen und angezeigt.
     */

    private Toolbar toolbar;

    private ArrayList<Exercise> exercises;

    private ExerciseDBHelper exHelper;
    private DatabaseReference database;

    SportExerciseRecyclerAdapter adapter;
    RecyclerView recyclerView;

    private TextView toolbarFitpointsField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_sport);


        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        //RecyclerView
        recyclerView = findViewById(R.id.exerciseSports_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exercises = new ArrayList<>();
        adapter = new SportExerciseRecyclerAdapter(this, exercises);
        recyclerView.setAdapter(adapter);

        //Database
        exHelper = new ExerciseDBHelper();
        database = exHelper.getExerciseDatabase();

        //Liste der Übungen aus Datenbank auslesen
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercises.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                }
                adapter.notifyDataSetChanged();
                Log.i("ExerciseSportActivity", "Auslesen der Übungen aus der Datenbank erfolgreich!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ExerciseSportActivity", "Fehler beim Auslesen der Übungen aus der Datenbank!");

            }
        });
    }
}