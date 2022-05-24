package eu.iums.fitwork;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    public final String db_fitpoints = "fitPoints";
    public final String db_name = "name";
    public final String db_lastname = "lasName";
    public final String db_email = "email";

    private User user;


    public UserDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("user");

    }

    //User
    public void writeNewUser(String username, String name, String lastname, String email) {
        user = new User(username, name, lastName, email);

        mDatabase.child(username).setValue(user);
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
                    lastName = snapshot.getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Datenbank", "Abfrage des Namen aus der Datenbank fehlerhaft!");
            }
        });
        return lastName;
    }
    public String getLastName(String username) {
        mDatabase.child(username).child("lastName").addValueEventListener(new ValueEventListener() {
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
        mDatabase.child(username).child(db_fitpoints).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    fitpoints = snapshot.getValue(Integer.class);
                    Log.i("Datenbank", "Fitpoints geladen! Aktueller Stand " + fitpoints);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Datenbank", "Abfrage des Namen aus der Datenbank fehlerhaft!");
            }
        });

        return fitpoints;
    }
}
