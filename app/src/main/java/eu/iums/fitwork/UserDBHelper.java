package eu.iums.fitwork;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class UserDBHelper {
    /*
    UserDBHelper Klasse:
    verwaltet User Datenbank
     */

    private final FirebaseDatabase database;
    private final DatabaseReference mDatabase;
    private StorageReference storageReference;

    //"childs" in Datenbank, von allen anderen Klassen sichtbar und nutzbar
    public final String DB_FITPOINTS = "fitPoints";
    public final String DB_NAME = "name";
    public final String DB_LASTNAME = "lastName";
    public final String DB_EMAIL = "email";
    public final String DB_LEADERBOARD = "leaderboardActive";
    public final String DB_HISTORY = "history";

    private User user;

    private boolean userExists;


    public UserDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("user");

    }

    //Schreiben des Userobjekts in die Datenbank bei Registrierung
    public void writeNewUser(String username, String name, String lastname, String email) {
        user = new User(username, name, lastname, email);

        mDatabase.child(username).setValue(user);
    }

    //Laden des Profilbilds in ein übergebenes ShapeableImageView
    public void getProfilePicture(String userName, ShapeableImageView imageView) {
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + userName + "/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri)
                        .placeholder(R.drawable.logo_info_app)
                        .into(imageView);
                Log.i("Database", "ImageView für Leaderboard für Nutzer " + userName + " geladen");
            }
        });
    }
    //Zurückgeben der DatabaseReference um auch in anderen Klassen Realtime Daten abzufragen
    public DatabaseReference getDatabase() {
        return mDatabase;
    }
}
