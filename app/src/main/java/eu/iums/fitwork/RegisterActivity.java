package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    /*
    RegisterActivity:
    Activity zum registrieren neuer User
    Abfrage diverser Daten (ua gewünschter Username, Email, Passwort, Vor und NAchname)
    Überprüfung ob Email Adresse existiert
    Doppelte Abfrage des Passworts um eine falsche Eingabe abzufangen
     */

    TextView registerButton;
    EditText name;
    EditText lastName;
    EditText username;
    EditText email;
    EditText password;
    EditText repeatPassword;

    UserDBHelper dbhelper;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private boolean userExists;

    private ArrayList<String> userNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //User DB
        dbhelper = new UserDBHelper();

        //Initialisierung TextViews
        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);

        //ClickListener RegisterButton
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            //Fehlermeldungen
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
                    //User registrieren
                    if (firebaseUser == null) {
                        //Registrieren in FirebaseAuthentication und anlegen des Users in Firebase RealtimeDatabase
                        registerUser(email.getText().toString(), password.getText().toString());
                        Log.i("RegisterActivity", "Anlegen des Users in der Datenbank erfolgreich!");

                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.failure_userAlreadyExistst, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Ihre Passwörter stimmen nicht überein.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //Registrieren des Users in FirebaseAuthentication
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username.getText().toString()).build();
                            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("Firebase", "Registrierung auf Firebase und setzen des Username erfolgreich!");
                                    dbhelper.writeNewUser(username.getText().toString(), name.getText().toString(), lastName.getText().toString(), email);
                                    Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich, " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                    Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                                    register.putExtra("registered", true);
                                    register.putExtra("username", username.getText().toString());
                                    startActivity(register);
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Fehler bei der Registrierung!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}