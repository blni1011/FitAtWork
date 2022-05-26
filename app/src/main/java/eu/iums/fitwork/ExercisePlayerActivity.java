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

        videoView.setVideoPath("https://bwsyncandshare.kit.edu/s/9KtXiFYF64feYJn/download/%C3%9Cbung%203%20-%20gek%C3%BCrzte%20Version.mp4");
        videoView.canPause();
        videoView.canSeekBackward();
        videoView.canSeekForward();
        videoView.start();
        videoView.setMediaController(new MediaController(this));
    }
}