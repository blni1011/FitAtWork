package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private UserDBHelper userDBHelper;
    private FirebaseUser user;

    private TextView usernameField;
    private TextView nameField;
    private TextView lastNameField;
    private ShapeableImageView changePicture;
    private ShapeableImageView profilePicture;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        userDBHelper = new UserDBHelper();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("users/" + user.getDisplayName() + "/profile.jpg");


        usernameField = findViewById(R.id.profile_username);
        nameField = findViewById(R.id.profile_vorname);
        lastNameField = findViewById(R.id.profile_nachname);
        changePicture = findViewById(R.id.profile_changePicture);
        profilePicture = findViewById(R.id.profile_picture);

        //Ändern des Profilbilds
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        //Laden des Profilbilds
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicture.setImageBitmap(bitmap);
                        }
                    });
        } catch (IOException e) {
            Log.i("Profil", "Fehler beim Laden des Profilbilds");
        }
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
    protected void onStart() {
        super.onStart();
        usernameField.setText(user.getDisplayName());
        nameField.setText(userDBHelper.getName(user.getDisplayName()));
        lastNameField.setText(userDBHelper.getLastName(user.getDisplayName()));
    }

    private void uploadProfilePicture(Uri imageuri) {
        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Profil", "Profilbild erfolgreich auf Firebase geladen!");
            }
        });
    }
}