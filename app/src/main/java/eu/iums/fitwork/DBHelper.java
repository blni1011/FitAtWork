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
    public void writeNewUser(String userId, String username, String name, String lastname, String email, int fitPoints) {
        User user = new User(username, name, lastname, email, fitPoints);

        mDatabase.child(userId).setValue(user);
    }
    public void deleteUser(String userId) {
        mDatabase.child("users").child(userId).removeValue();
    }
}
