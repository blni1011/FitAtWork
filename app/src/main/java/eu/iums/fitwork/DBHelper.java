package eu.iums.fitwork;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DBHelper {

    private DatabaseReference mDatabase;

    public DBHelper(String dbReference) {
        mDatabase = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app").getReference(dbReference);
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
}
