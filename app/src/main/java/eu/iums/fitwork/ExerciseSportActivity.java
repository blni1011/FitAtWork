package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ExerciseSportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button testButton;

    private ArrayList<Exercise> exercises;

    private ExerciseDBHelper exHelper;
    private DatabaseReference database;

    SportExerciseRecyclerAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_sport);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView
        recyclerView = findViewById(R.id.exerciseSports_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exercises = new ArrayList<>();
        adapter = new SportExerciseRecyclerAdapter(this, exercises);
        recyclerView.setAdapter(adapter);

        exHelper = new ExerciseDBHelper();
        database = exHelper.getExerciseDatabase();

        //Get ExerciseList from DB
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercises.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}