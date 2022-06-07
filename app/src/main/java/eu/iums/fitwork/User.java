package eu.iums.fitwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private int fitPoints;
    private boolean leaderboardActive;

    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseUser fbUser;


    public User(String username, String name, String lastName, String email) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.fitPoints = 0;
        this.leaderboardActive = false;
    }

    public User() {
    }

    public User(String username) {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app").getReference("user");
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + fbUser.getUid() + "/profile.jpg");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child(username).child("name").getValue(String.class);
                lastName = snapshot.child(username).child("lastName").getValue(String.class);
                email = snapshot.child(username).child("email").getValue(String.class);
                fitPoints = snapshot.child(username).child("fitPoints").getValue(Integer.class);
                leaderboardActive = snapshot.child(username).child("leaderboardActive").getValue(Boolean.class);
                Log.i("User/user", "Erstellen des User-Objekts aus der Datenbank erfolgreich");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("User/user", "Fehler beim Erstellen des Userobjekts aus der Datenbank");
            }
        });
    }

    public int getFitPoints() {
        return fitPoints;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLeaderboardActive() {
        return leaderboardActive;
    }
}