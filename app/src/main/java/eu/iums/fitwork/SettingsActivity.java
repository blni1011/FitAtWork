package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.DateFormat;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private NotificationManagerCompat notificationManager;
    private Toolbar toolbar;
    private Button select_time;
    private TextView selected_time;
    private String time;
    private Switch switch_break;
    private boolean isVisible_break;
    private boolean break_state;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;

    private TextView toolbarFitpointsField;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SELECTED_TIME = "time";
    public static final String SWITCH_ALARM = "switch_alarm";



    //TODO: Entfernen des Leaderboard-Teilnahme-Switch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationManager = NotificationManagerCompat.from(this);
        selected_time = (TextView) findViewById(R.id.selected_time);
        selected_time.setVisibility(View.INVISIBLE);
        createNotificationChannel();

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Einstellungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        //Zeit auswählen
        select_time = (Button) findViewById(R.id.select_time);
        select_time.setVisibility(View.INVISIBLE);
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "time picker");

                saveData();
            }
        });


        //Alarm an und aus stellen
        switch_break = (Switch) findViewById(R.id.switch_alert);
        switch_break.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isVisible_break){
                    isVisible_break = true;
                    select_time.setVisibility(View.VISIBLE);
                    selected_time.setVisibility(View.VISIBLE);
                } else {
                    isVisible_break = false;
                    cancelAlarm();
                    select_time.setVisibility(View.INVISIBLE);
                    selected_time.setVisibility(View.INVISIBLE);
                }
                saveData();
            }
        });

        loadData();
        updateViews();

        //Feedback
        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);

        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    //Benachrichtigung erstellen
    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "breakReminderChannel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel1 = new NotificationChannel("break_alert", name, importance);
            channel1.setDescription("This is the Break Alert Channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }

    }

    //Zeit einstellen
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Kalender
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        if(switch_break.isChecked() && isVisible_break){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(this, "Pausenalarm erfolgreich gesetzt", Toast.LENGTH_SHORT).show();
            updateTimeText(c);
        }
    }

    //Benachrichtigungen stoppen
    private void cancelAlarm() {
        Intent intent = new Intent(this, AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Pausenalarm ausgeschaltet", Toast.LENGTH_SHORT).show();
    }

    private void updateTimeText(Calendar c) {
        String timeText;
        timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        selected_time.setText(timeText);
        saveData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SELECTED_TIME, selected_time.getText().toString());
        editor.putBoolean(SWITCH_ALARM, switch_break.isChecked());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        time = sharedPreferences.getString(SELECTED_TIME, "");
        break_state = sharedPreferences.getBoolean(SWITCH_ALARM, false);
    }


    public void updateViews() {
        selected_time.setText(time);
        switch_break.setChecked(break_state);
    }

    //Email als Feedback senden
    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Wähle eine Email Anwendung"));
    }
}