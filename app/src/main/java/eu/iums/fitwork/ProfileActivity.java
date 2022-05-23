package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private UserDBHelper userDBHelper;
    private FirebaseUser user;

    private TextView usernameField;
    private TextView nameField;
    private TextView lastNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDBHelper = new UserDBHelper();
        user = FirebaseAuth.getInstance().getCurrentUser();

        usernameField = findViewById(R.id.profile_username);
        nameField = findViewById(R.id.profile_vorname);
        lastNameField = findViewById(R.id.profile_nachname);
    }

    @Override
    protected void onStart() {
        super.onStart();
        usernameField.setText(user.getDisplayName());
        nameField.setText(userDBHelper.getName(user.getDisplayName()));
        lastNameField.setText(userDBHelper.getLastName(user.getDisplayName()));
    }
}