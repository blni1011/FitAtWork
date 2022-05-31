package eu.iums.fitwork;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ZitateDBHelper {

    private final FirebaseDatabase database;
    private final DatabaseReference mDatabase;

    private ArrayList<String> zitate;


    public ZitateDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("zitate");
        zitate = new ArrayList<>();
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }
}
