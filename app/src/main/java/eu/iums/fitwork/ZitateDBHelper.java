package eu.iums.fitwork;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ZitateDBHelper {
    /*
    ZitateDBHelper Klasse:
    Verwaltet ZitateDB
     */

    private final FirebaseDatabase database;
    private final DatabaseReference mDatabase;


    //Weisheit auf Startseite
    public ZitateDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("zitate");
    }

    //Zurückgeben der DatabaseReference um von anderen Klassen auf die Zitate zugreifen zu können
    public DatabaseReference getDatabase() {
        return mDatabase;
    }
}
