package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private TextView fitpointsTextView;
    private TextView usernameTextView;
    private TextView headerGreetingsTextView;
    private UserDBHelper userDB;
    private ZitateDBHelper zitateDB;

    private User dbUser;

    private String zitat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        fitpointsTextView = findViewById(R.id.fitpoints);
        headerGreetingsTextView = findViewById(R.id.header_greetings);

        // init header text view
        View headerView = navigationView.getHeaderView(0);
        usernameTextView = (TextView) headerView.findViewById(R.id.NameHeader);
        headerGreetingsTextView = (TextView) headerView.findViewById(R.id.header_greetings);

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //DBs
        userDB = new UserDBHelper();
        zitateDB = new ZitateDBHelper();



        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navdrawer_open, R.string.navdrawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    //Wenn BackButton benutzt wird, schließt sich Drawer und nicht App
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Auswahl eines Items im NavigationDrawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_exercises:
                Intent intent_exercises = new Intent(this, ExerciseActivity.class);
                if(dbUser != null) {
                    intent_exercises.putExtra("user", dbUser);
                }
                startActivity(intent_exercises);
                break;
            case R.id.nav_history:
                Intent intent_history = new Intent(this, HistoryActivity.class);
                if(dbUser != null) {
                    intent_history.putExtra("user", dbUser);
                }
                startActivity(intent_history);
                break;
            case R.id.nav_stats:
                Intent intent_stats = new Intent(this, LeaderboardActivity.class);
                if(dbUser != null) {
                    intent_stats.putExtra("user", dbUser);
                }
                startActivity(intent_stats);
                break;
            case R.id.nav_profile:
                Intent intent_profile = new Intent(this, ProfileActivity.class);
                if(dbUser != null) {
                    intent_profile.putExtra("user", dbUser);
                }
                startActivity(intent_profile);
                break;
            case R.id.nav_login:
                Intent intent_login = new Intent(this, LoginActivity.class);
                startActivity(intent_login);
                break;
            case R.id.nav_logout:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                }
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                if(dbUser != null) {
                    intent.putExtra("user", dbUser);
                }
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Auslesen diverser Daten aus den Datenbanken
        getData();

        FirebaseUser fbUser = mAuth.getCurrentUser();
        if (fbUser != null) {
            headerGreetingsTextView.setText(R.string.header_greetingLoggedIn);
            usernameTextView.setText(fbUser.getDisplayName());
        } else {
            Log.d("Firebase", "Problem beim automatischen einloggen oder Nutzer nicht registriert!");
            fitpointsTextView.setVisibility(View.INVISIBLE);
            usernameTextView.setVisibility(View.INVISIBLE);
            headerGreetingsTextView.setText(R.string.header_greetingLoggedOut);
        }
        //Ausblenden von Items im Nav-Drawer.
        Menu menu = navigationView.getMenu();
        if (mAuth.getCurrentUser() == null) {
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_stats).setVisible(false);
        } else {
            menu.findItem(R.id.nav_login).setVisible(false);
        }
    }

    private void getData() {
        DatabaseReference database = userDB.getDatabase();
        FirebaseUser fbUser = mAuth.getCurrentUser();

        //Fitpoints
        if (fbUser != null) {
            database.child(fbUser.getDisplayName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        fitpointsTextView.setText(String.valueOf(user.getFitPoints()));
                        Log.i("MainActivity/getData", "Ausgelesene FitPoints: " + user.getFitPoints());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("MainActivity/getData", "Fehler beim Auslesen der FitPoints aus der Datenbank!");

                }
            });
        }

        //Zitate
        //TODO: Backup wenn keine Internetverbindung besteht, case überlegen.
        ArrayList<String> zitateList = new ArrayList<>();
        Random random = new Random();

        zitateDB.getDatabase().get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    zitateList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String test = snapshot.getValue(String.class);
                        zitateList.add(test);
                    }
                    Log.i("MainActivity/getData", "Auslesen der Zitate aus der Datenbank erfolgreich!");

                    int iterator = random.nextInt(zitateList.size()) + 1;
                    zitat = zitateList.get(iterator);
                    Log.i("MainActivity/getData", "Zitat: " + zitat);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("MainActivity/getData", "Fehler beim Auslesen der Zitate aus der Datenbank!");
            }
        });

        //Erstellen User-Objekt
        if(fbUser != null) {
            dbUser = new User(fbUser.getDisplayName());
        }
    }
}