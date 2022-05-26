package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

public class ExerciseActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Button1 - Sport
        ShapeableImageView btnexercisesport = findViewById(R.id.button_exercise_sport);
        btnexercisesport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            openExerciseSportActivity();
            }
        });



        //Button2 - Brain
        ShapeableImageView btnexercisebrain = findViewById(R.id.button_exercise_brain);
        btnexercisebrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExerciseBrainTrainingActivity();
            }
        });
    }
    public void openExerciseSportActivity() {
        Intent intent = new Intent(this, ExerciseSportActivity.class);
        startActivity(intent);
    }
    public void openExerciseBrainTrainingActivity() {
        Intent intent2 = new Intent(this, ExerciseBrainTrainingActivity.class);
        startActivity(intent2);
    }
}