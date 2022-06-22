package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;


public class ExercisePlayerActivity extends AppCompatActivity {
    /*
    Abspielen der Übungen, diese werden als Intent übergeben
     Wenn Video beendet wird ein Alert-Dialog angezeigt, in dem das Video erneut gestartet oder beendet wird.
     Wenn keine WLAN-Verbindung besteht, wird ein AlertDialog angezeigt, in dem die Nachricht ignoriert oder in die Systemeinstellungen gewechselt werden kann.
     */

    private Toolbar toolbar;

    private VideoView videoView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView toolbarFitpointsField;

    private MediaController mediaController;

    private ExerciseDBHelper exHelper;

    private User user;

    private UserDBHelper userDBHelper;

    private String exName;

    private int fitpointsToAdd;
    private final String ALERT_WIFI = "wifi";
    private final String ALERT_FINISHEDVIDEO = "finish";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_player);

        user = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        userDBHelper = new UserDBHelper();

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        //Initialisieren Layoutelemente
        videoView = findViewById(R.id.exerciseSport_videoView);
        titleView = findViewById(R.id.exerciseSport_title);
        descriptionView = findViewById(R.id.exerciseSport_description);
        mediaController = new MediaController(this);

        //Datenbank
        exHelper = new ExerciseDBHelper();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Wenn keine WLAN Verbindung besteht, wir ein Alert-Dialog angezeigt (Video wird gestramt -> hoher Datenverbrauch)
        if (!isConnected(this)) {
            showAlertDialog(ALERT_WIFI);
        } else {
            getExercise();
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    showAlertDialog(ALERT_FINISHEDVIDEO);
                }
            });
        }
    }

    //Auslesen der Übung aus dem Intent
    private void getExercise() {
        videoView.setVideoPath(getIntent().getExtras().getString(exHelper.DB_EXERCISEURL));
        titleView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISETITLE));
        descriptionView.setText(getIntent().getExtras().getString(exHelper.DB_EXERCISEDESCRIPTION));
        fitpointsToAdd = getIntent().getExtras().getInt(exHelper.DB_EXERCISEFITPOINTS);

        exName = getIntent().getExtras().getString(exHelper.DB_EXERCISETITLE);
    }

    //Abfrage, ob WLAN Verbindung besteht
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

    //Alert-Dialoge
    private void showAlertDialog(String alertDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //WIFI Alert-Dialog
        if(alertDialog.equals(ALERT_WIFI)) {
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
        //Video beendet Alert-Dialog
        if(alertDialog.equals(ALERT_FINISHEDVIDEO)) {
            builder.setMessage(R.string.alert_videoEnd)
                    .setCancelable(true)
                    .setPositiveButton("Video erneut abspielen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            videoView.start();
                        }
                    }).setNegativeButton("Zurück zur Übersicht", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ExercisePlayerActivity.this, ExerciseSportActivity.class);
                            intent.putExtra("user", user);
                            user.addFitPoints(fitpointsToAdd, getBaseContext());
                            setExerciseInHistory();
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    //Wenn Video beendet und dies in Alert-Dialog bestätigt, wird das Video in der History des jeweiligen Nutzers gespeichert.
    private void setExerciseInHistory() {
        Map<String, String> exerciseToHistory = new HashMap<>();
        DatabaseReference database = userDBHelper.getDatabase();

        exerciseToHistory.put("/" + user.getUsername() + "/" + userDBHelper.DB_HISTORY + "/" + String.valueOf(System.currentTimeMillis()), exName);
        database.child(user.getUsername())
                .child(userDBHelper.DB_HISTORY)
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(exName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("ExercisePlayerActivity/setExerciseInHistory", "Hinzufügen der Übung zur History erfolgreich");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ExercisePlayerActivity/setExerciseInHistory", "Fehler beim hinzufügen der Übung zur History");
            }
        });
    }
}