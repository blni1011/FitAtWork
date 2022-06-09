package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class ExercisePlayerActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private VideoView videoView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView toolbarFitpointsField;

    private MediaController mediaController;

    private ExerciseDBHelper exHelper;

    /*TODO: Anzeige der zu erreichenden Fitpoints, verbuchen der Fitpoints, Ladekreis, während Video bufferd?
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_player);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView = findViewById(R.id.exerciseSport_videoView);
        titleView = findViewById(R.id.exerciseSport_title);
        descriptionView = findViewById(R.id.exerciseSport_description);
        mediaController = new MediaController(this);
        exHelper = new ExerciseDBHelper();

        getExercise();

        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }

    private void getExercise() {
        videoView.setVideoPath(getIntent().getExtras().getString(exHelper.DB_EXERCISEURL));
        titleView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISETITLE));
        descriptionView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISEDESCRIPTION));
    }
}