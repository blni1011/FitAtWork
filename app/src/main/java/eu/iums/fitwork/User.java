package eu.iums.fitwork;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private int fitPoints;
    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private DatabaseReference dbReference;

    public User(String username, String name, String lastName, String email, int fitPoints) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.fitPoints = fitPoints;
    }
    public User(String userID) {
        dbReference = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app").getReference("user");

        this.userID = userID;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if(this.userID.equals(mAuth.getCurrentUser().getUid())) {
            this.username = firebaseUser.getDisplayName();
            this.email = firebaseUser.getEmail();
        } else {
            Log.d("FirebaseAuth", "Fehler beim erstellen des User-Objekts, UIDs stimmen nicht überein!");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFitPoints() {
        dbReference.child(userID).child("fitPoints").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    //fitPoints = Integer.parseInt(task.getResult().getValue().toString());
                } else {
                    Log.d("Firebase", "Error beim Laden der Fitpoints aus der Datenbank!");
                }
            }
        });
        return fitPoints;
    }
    public void addFitPoints(int additionalFitPoints) {
        this.fitPoints += additionalFitPoints;
    }
}
