package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

public class ExerciseActivity extends AppCompatActivity {

    Toolbar toolbar;

    private User dbUser;

    private TextView toolbarFitpointsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        dbUser = new User();
        dbUser = getIntent().getParcelableExtra("user");

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(dbUser.getFitPoints()));

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
        intent.putExtra("user", dbUser);
        startActivity(intent);
    }
    public void openExerciseBrainTrainingActivity() {
        Intent intent = new Intent(this, ExerciseBrainTrainingActivity.class);
        intent.putExtra("user", dbUser);
        startActivity(intent);
    }
}