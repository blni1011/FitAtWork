package eu.iums.fitwork;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {

    /*
    User Klasse:
    Enthält User Objekt
    Parcelable um das User Objekt mit einem Intent an eine andere Activity zu übergeben.
     */

    private String username;
    private String name;
    private String lastName;
    private String email;
    private int fitPoints;
    private boolean leaderboardActive;

    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseUser fbUser;

    private UserDBHelper userDBHelper;

    public User(String username, String name, String lastName, String email) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.fitPoints = 0;
        this.leaderboardActive = false;
    }

    public User() {
    }

    public User(String username) {
        database = FirebaseDatabase.getInstance("https://fitatwork-6adb0-default-rtdb.europe-west1.firebasedatabase.app").getReference("user");
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + fbUser.getUid() + "/profile.jpg");
        this.username = username;

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child(username).child("name").getValue(String.class);
                lastName = snapshot.child(username).child("lastName").getValue(String.class);
                email = snapshot.child(username).child("email").getValue(String.class);
                fitPoints = snapshot.child(username).child("fitPoints").getValue(Integer.class);
                leaderboardActive = snapshot.child(username).child("leaderboardActive").getValue(Boolean.class);
                Log.i("User/user", "Erstellen des User-Objekts aus der Datenbank erfolgreich");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("User/user", "Fehler beim Erstellen des Userobjekts aus der Datenbank");
            }
        });
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        lastName = in.readString();
        email = in.readString();
        fitPoints = in.readInt();
        leaderboardActive = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getFitPoints() {
        return fitPoints;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLeaderboardActive() {
        return leaderboardActive;
    }

    public void addFitPoints(int pointsToAdd, Context context) {
        userDBHelper = new UserDBHelper();
        DatabaseReference database = userDBHelper.getDatabase();

        fitPoints +=pointsToAdd;

        Map<String, Object> updateChildren = new HashMap<>();
        updateChildren.put("/" + username + "/" + userDBHelper.DB_FITPOINTS, fitPoints);
        database.updateChildren(updateChildren).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("User/addFitpoints", "Fitpoints erfolgreich hinzugefügt! Neuer Stand: " + fitPoints);

                Toast.makeText(context, R.string.success_addFitpoints, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("User/addFitpoints", "Fehler beim hinzufügen der Fitpoints!");
                Toast.makeText(context, R.string.failure_addFitpoints, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeInt(fitPoints);
        parcel.writeByte((byte) (leaderboardActive ? 1 : 0));
    }
}