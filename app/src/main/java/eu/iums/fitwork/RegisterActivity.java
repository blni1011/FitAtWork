package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView registerButton;
    EditText name;
    EditText lastName;
    EditText username;
    EditText email;
    EditText password;
    EditText repeatPassword;

    DBHelper dbhelper;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        dbhelper = new DBHelper("user");

        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Sie haben keinen Vornamen eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lastName.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Sie haben keinen Nachnamen eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Sie haben keinen Benutzernamen eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Sie haben keine E-Mail-Adresse eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(getBaseContext(), "Sie haben keine gültige E-Mail-Adresse eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Sie haben kein Passwort eingegeben.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repeatPassword.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(), "Bitte wiederholen Sie Ihr Passwort.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (repeatPassword.getText().toString().matches(password.getText().toString())) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    //User registrieren
                    if (user == null) {
                        registerUser(email.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Dieser Benutzer exisitert bereits!", Toast.LENGTH_SHORT).show();
                    }

                    Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(register);
                } else {
                    Toast.makeText(getBaseContext(), "Ihre Passwörter stimmen nicht überein.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username.getText().toString()).build();
                            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //dbhelper.writeNewUser(user.getUid(), user.getDisplayName(), name.getText().toString(), lastName.getText().toString(), user.getEmail(), 0);
                                    Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Fehler bei der Registrierung!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}