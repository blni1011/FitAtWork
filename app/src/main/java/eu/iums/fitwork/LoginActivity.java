package eu.iums.fitwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class LoginActivity extends AppCompatActivity {
    /*
    LoginActivity:
    Activity zum einloggen in das Nutzerkonto.
    Button zum Passwort zurücksetzen und zum Registrieren, wenn kein Profil besteht
     */

    Toolbar toolbar;
    private FirebaseAuth mAuth;

    TextView signIn;
    EditText username;
    EditText password;

    TextView forgot_password;
    TextView register;

    private TextView toolbarFitpointsField;

    private boolean notregistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarFitpointsField = findViewById(R.id.toolbar2_fitpoints);
        toolbarFitpointsField.setVisibility(View.INVISIBLE);

        //FirebaseAuthentication
        mAuth = FirebaseAuth.getInstance();

        //EditTexts
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //Einloggen-Button
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Einloggen mit EMail Adresse und Passwort
                loginUser(username.getText().toString(), password.getText().toString());

            }
        });

        //Passwort vegessen Button -> Versand einer E-Mail an hinterlegte Adresse zum zurücksetzen des Passworts
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText() == null) {
                    Toast.makeText(LoginActivity.this, R.string.login_enterEmailAdress, Toast.LENGTH_SHORT).show();
                }
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {

                    mAuth.sendPasswordResetEmail(username.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(LoginActivity.this, R.string.login_sendPasswortResetMail, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, R.string.failure_emailadresswrong, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, R.string.failure_emailAdressDoesntExist, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button Registrieren, RegisterActivity wird geöffnet
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    //Einloggen des Users mit Hilfe von FirebaseAuthentication
    private void loginUser(String email, String password) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Einloggen nur möglich, wenn nicht bereits eingeloggt
        if (currentUser == null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Eingeloggt als " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 300);
                            } else {
                                Toast.makeText(LoginActivity.this, "Fehler beim Einloggen!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Schon eingeloggt!", Toast.LENGTH_LONG).show();
        }
    }
}