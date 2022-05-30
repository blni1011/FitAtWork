package eu.iums.fitwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class UserDBHelper {

    private final FirebaseDatabase database;
    private final DatabaseReference mDatabase;
    private StorageReference storageReference;

    private String name;
    private String lastName;
    private int fitpoints;
    private Bitmap profilePicture;

    public final String db_fitpoints = "fitPoints";
    public final String db_name = "name";
    public final String db_lastname = "lastName";
    public final String db_email = "email";

    private User user;


    public UserDBHelper() {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app");
        mDatabase = database.getReference("user");

    }

    public void writeNewUser(String username, String name, String lastname, String email) {
        user = new User(username, name, lastname, email);

        mDatabase.child(username).setValue(user);
    }
    public Integer getFitpoints(String username) {
        mDatabase.child(username).child(db_fitpoints).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fitpoints = snapshot.getValue(Integer.class);
                    Log.i("Datenbank", "Fitpoints geladen! Aktueller Stand " + fitpoints);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Datenbank", "Abfrage der Fitpoints aus der Datenbank fehlerhaft!");
            }
        });
        return fitpoints;
    }

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

    public DatabaseReference getDatabase() {
        return mDatabase;
    }
}
