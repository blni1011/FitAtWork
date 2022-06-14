package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
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

    private TextView toolbarFitpointsField;
    private TextView usernameField;
    private TextView fitpointsField;
    private TextView editProfile;
    private TextView forgotPasswordField;

    private EditText nameField;
    private EditText lastNameField;
    private EditText emailField;
    private ShapeableImageView changePicture;
    private ShapeableImageView profilePicture;
    private Switch rankingSwitch;

    private StorageReference storageReference;

    private boolean isEditable;

    private User dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        isEditable = false;

        dbUser = new User();
        dbUser = getIntent().getParcelableExtra("user");

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setText(String.valueOf(dbUser.getFitPoints()));


        //Firebase
        userDBHelper = new UserDBHelper();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + fbUser.getDisplayName() + "/profile.jpg");


        usernameField = findViewById(R.id.profile_username);
        nameField = findViewById(R.id.profile_vorname);
        lastNameField = findViewById(R.id.profile_nachname);
        emailField = findViewById(R.id.profile_email);
        fitpointsField = findViewById(R.id.profile_fitpoints);
        forgotPasswordField = findViewById(R.id.profile_forgotPassword);

        changePicture = findViewById(R.id.profile_changePicture);
        profilePicture = findViewById(R.id.profile_picture);
        editProfile = findViewById(R.id.profile_editProfileButton);
        rankingSwitch = findViewById(R.id.profile_rankingSwitch);

        //Sperren der EditTexts
        nameField.setEnabled(isEditable);
        lastNameField.setEnabled(isEditable);
        emailField.setEnabled(isEditable);
        changePicture.setEnabled(isEditable);
        changePicture.setVisibility(View.INVISIBLE);

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
                            rankingSwitch.isChecked()
                    );
                    nameField.setEnabled(isEditable);
                    nameField.setBackgroundColor(getResources().getColor(R.color.white));
                    nameField.setTextColor(getResources().getColor(R.color.darker_grey));
                    lastNameField.setEnabled(isEditable);
                    lastNameField.setBackgroundColor(getResources().getColor(R.color.white));
                    lastNameField.setTextColor(getResources().getColor(R.color.darker_grey));
                    emailField.setEnabled(isEditable);
                    emailField.setBackgroundColor(getResources().getColor(R.color.white));
                    emailField.setTextColor(getResources().getColor(R.color.darker_grey));
                    changePicture.setVisibility(View.INVISIBLE);
                    changePicture.setEnabled(isEditable);
                } else {
                    isEditable = true;
                    editProfile.setText(R.string.profile_saveChanges);
                    nameField.setEnabled(isEditable);
                    nameField.setBackgroundColor(getResources().getColor(R.color.darker_grey));
                    nameField.setTextColor(getResources().getColor(R.color.black));
                    lastNameField.setEnabled(isEditable);
                    lastNameField.setBackgroundColor(getResources().getColor(R.color.darker_grey));
                    lastNameField.setTextColor(getResources().getColor(R.color.black));
                    emailField.setEnabled(isEditable);
                    emailField.setBackgroundColor(getResources().getColor(R.color.darker_grey));
                    emailField.setTextColor(getResources().getColor(R.color.black));
                    changePicture.setVisibility(View.VISIBLE);
                    changePicture.setEnabled(isEditable);
                }
            }
        });

        //Passwortvergessen Button
        //TODO: Passwort-Update activity einbauen?
        forgotPasswordField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    //Laden des Profilbilds
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
        nameField.setText(dbUser.getName());
                        lastNameField.setText(dbUser.getLastName());
                        emailField.setText(dbUser.getEmail());
                        fitpointsField.setText(String.valueOf(dbUser.getFitPoints()));
                        rankingSwitch.setChecked(dbUser.isLeaderboardActive());
    }

    //Update der User Daten in der Firebase Realtime DB
    private void updateUser(String username, String name, String lastName, String email, boolean leaderboard) {
        DatabaseReference database = userDBHelper.getDatabase();

        //TODO: EMail auch in FBAuth ändern.

        Map<String, Object> updateChildren = new HashMap<>();
        updateChildren.put("/" + username + "/" + userDBHelper.DB_NAME, name);
        updateChildren.put("/" + username + "/" + userDBHelper.DB_LASTNAME, lastName);
        //updateChildren.put("/" + username + "/" + userDBHelper.DB_EMAIL, email);
        updateChildren.put("/" + username + "/" + userDBHelper.DB_LEADERBOARD, leaderboard);

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