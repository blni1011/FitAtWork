package eu.iums.fitwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConnected(this)) {
            showAlertDialog();
        } else {
            getExercise();
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }

    private void getExercise() {
        videoView.setVideoPath(getIntent().getExtras().getString(exHelper.DB_EXERCISEURL));
        titleView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISETITLE));
        descriptionView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISEDESCRIPTION));
    }

    private boolean isConnected(ExercisePlayerActivity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiConn != null && wifiConn.isConnected()) {
            Log.i("ExercisePlayerActivity/isConnected", "WLAN Verbindung aktiv.");
            return true;
        } else {
            Log.i("ExercisePlayerActivity/isConnected", "Keine WLAN Verbindung aktiv.");
            return false;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connection_connectToWifi)
                .setCancelable(true)
                .setPositiveButton("Einstellungen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Weiter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getExercise();
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.start();
                    }
                }).show();
    }
}