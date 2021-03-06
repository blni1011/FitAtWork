package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity{

    /*
    LeaderboardActivity:
    Auslesen der User als Userobjekt aus Datenbank -> RealTime
    Übergeben an Adapter als ArrayList
     */

    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<User> users;
    DatabaseReference database;

    private TextView toolbarFitpointsField;

    private User dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaderboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(MainActivity.getFitPoints()));

        dbUser = getIntent().getParcelableExtra("user");

        //Abfrage: Nimmt Nutzer an Leaderboard teil? Wenn nein, wird AlertDialog angezeigt
        if(!getIntent().getBooleanExtra("leaderboardActive", false)) {
            showAlertDialog();
        } else {

            //RecyclerView
            recyclerView = findViewById(R.id.leaderboard_list);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app").getReference("user");
            users = new ArrayList<>();
            recyclerAdapter = new RecyclerAdapter(this, users);
            recyclerView.setAdapter(recyclerAdapter);


            //Auslesen der User aus der Datenbank und sortieren (höchste Punktzahl vorne)
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.isLeaderboardActive()) {
                            users.add(user);
                        }
                        Collections.sort(users, new Comparator<User>() {
                            @Override
                            public int compare(User user1, User user2) {
                                return Integer.valueOf(user2.getFitPoints()).compareTo(user1.getFitPoints());
                            }
                        });
                    }
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    //AlertDialog wenn die Teilnahme am Leaderboard nicht aktiviert
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_leaderboardInactive)
                .setCancelable(true)
                .setPositiveButton("Profileinstellungen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LeaderboardActivity.this, ProfileActivity.class);
                        intent.putExtra("user", dbUser);
                        startActivity(intent);
                    }
                }).setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(LeaderboardActivity.this, MainActivity.class));
                    }
                }).show();
    }
}