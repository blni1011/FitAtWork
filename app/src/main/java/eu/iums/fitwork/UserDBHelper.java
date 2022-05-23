package eu.iums.fitwork;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDBHelper {

    private final FirebaseDatabase database;
    private final DatabaseReference mDatabase;
    private String name = "Vorname";
    private String lastName = "Nachname";
    private int fitpoints;

    public UserDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("user");
    }

    //User
    public void writeNewUser(String username, String name, String lastname, String email) {
        mDatabase.child(username).child("Name").setValue(name);
        mDatabase.child(username).child("Nachname").setValue(lastname);
        mDatabase.child(username).child("Email").setValue(email);
        mDatabase.child(username).child("Fitpoints").setValue(0);
    }
    public void deleteUser(String email) {
        mDatabase.child("users").child(email).removeValue();
    }

    public void writeTest() {
        mDatabase.child("TestChild").setValue("TestValue");
    }

    public boolean checkUserExists(String username) {

        return false;
    }
    public String getName(String username) {
        mDatabase.child(username).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    name = snapshot.getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Datenbank", "Abfrage des Namen aus der Datenbank fehlerhaft!");
            }
        });
        return name;
    }
    public Integer getFitpoints(String username){
        mDatabase.child(username).child("Fitpoints").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    fitpoints = snapshot.getValue(Integer.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Datenbank", "Abfrage des Namen aus der Datenbank fehlerhaft!");
            }
        });

        return fitpoints;
    }
    public FirebaseDatabase getDatabase() {
        return database;
    }
    public void initDatabase() {

    }
}