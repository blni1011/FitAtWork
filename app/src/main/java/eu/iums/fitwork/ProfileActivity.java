package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private UserDBHelper userDBHelper;
    private FirebaseUser fbUser;

    private TextView usernameField;
    private TextView editProfile;
    private EditText nameField;
    private EditText lastNameField;
    private EditText emailField;
    private TextView fitpointsField;
    private ShapeableImageView changePicture;
    private ShapeableImageView profilePicture;

    private StorageReference storageReference;

    private boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        isEditable = false;

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        userDBHelper = new UserDBHelper();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + fbUser.getDisplayName() + "/profile.jpg");


        usernameField = findViewById(R.id.profile_username);
        nameField = findViewById(R.id.profile_vorname);
        lastNameField = findViewById(R.id.profile_nachname);
        emailField = findViewById(R.id.profile_email);
        fitpointsField = findViewById(R.id.profile_fitpoints);

        changePicture = findViewById(R.id.profile_changePicture);
        profilePicture = findViewById(R.id.profile_picture);
        editProfile = findViewById(R.id.profile_editProfileButton);

        //Sperren der EditTexts
        nameField.setEnabled(isEditable);
        lastNameField.setEnabled(isEditable);
        emailField.setEnabled(isEditable);

        //Ändern des Profilbilds
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });
        //Laden des Profilbilds
        userDBHelper.getProfilePicture(fbUser.getDisplayName(), profilePicture);

        //Laden der Daten
        getData();

        //EditProfile ClickListener
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    isEditable = false;
                    editProfile.setText(R.string.profile_editProfile);
                    updateUser(usernameField.getText().toString(),
                            nameField.getText().toString(),
                            lastNameField.getText().toString(),
                            emailField.getText().toString(),
                            fitpointsField.getText().toString()
                    );
                    nameField.setEnabled(isEditable);
                    lastNameField.setEnabled(isEditable);
                    emailField.setEnabled(isEditable);
                } else {
                    isEditable = true;
                    editProfile.setText(R.string.profile_saveChanges);
                    nameField.setEnabled(isEditable);
                    lastNameField.setEnabled(isEditable);
                    emailField.setEnabled(isEditable);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) ;
            Uri imageUri = data.getData();
            profilePicture.setImageURI(imageUri);

            uploadProfilePicture(imageUri);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameField.setText(fbUser.getDisplayName());
    }

    private void uploadProfilePicture(Uri imageuri) {
        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Profil", "Profilbild erfolgreich auf Firebase geladen!");
            }
        });
    }

    private void getData() {
        DatabaseReference database = userDBHelper.getDatabase();

        //Userdaten aus Firebase auslesen
        if (fbUser != null) {
            database.child(fbUser.getDisplayName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        nameField.setText(user.getName());
                        lastNameField.setText(user.getLastName());
                        emailField.setText(user.getEmail());
                        fitpointsField.setText(String.valueOf(user.getFitPoints()));
                        Log.i("ProfileActivity/getData", "Laden der Nutzerdaten aus Firebase erfolgreich!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("ProfileActivity/getData", "Fehler beim Laden der Nutzerdaten aus Firebase!");
                }
            });
        }
    }

    //Update der User Daten in der Firebase Realtime DB
    private void updateUser(String username, String name, String lastName, String email, String fitpoints) {
        DatabaseReference database = userDBHelper.getDatabase();

        Map<String, Object> updateChildren = new HashMap<>();
        updateChildren.put("/" + username + "/" + userDBHelper.DB_NAME, name);
        updateChildren.put("/" + username + "/" + userDBHelper.DB_LASTNAME, lastName);
        updateChildren.put("/" + username + "/" + userDBHelper.DB_EMAIL, email);
        updateChildren.put("/" + username + "/" + userDBHelper.DB_FITPOINTS, fitpoints);

        database.updateChildren(updateChildren).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this, "Änderung des Profils erfolgreich!", Toast.LENGTH_SHORT).show();
                Log.i("ProfileActivity/updateUser", "Profil erfolgreich geändert!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Fehler beim Ändern des Profils!", Toast.LENGTH_SHORT).show();
                Log.i("ProfileActivity/updateUser", "Fehler beim ändern des Profils!");
            }
        });
    }
}