package eu.iums.fitwork;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ExerciseDBHelper {

    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference database;

    public final String DB_EXERCISETITLE = "title";
    public final String DB_EXERCISECATEGORY = "category";
    public final String DB_EXERCISEDESCRIPTION = "description";
    public final String DB_EXERCISEFITPOINTS = "fitpoints";
    public final String DB_EXERCISEURL = "url";

    public ExerciseDBHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        database = firebaseDatabase.getReference("exercises");
    }

    public DatabaseReference getExerciseDatabase() {
        return database;
    }
}
