package eu.iums.fitwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    TextView registerButton;
    EditText name;
    EditText lastName;
    EditText username;
    EditText email;
    EditText password;
    EditText repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
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
                } else if (repeatPassword.getText().toString().matches(password.getText().toString())){
                    Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(register);
                } else {
                    Toast.makeText(getBaseContext(), "Ihre Passwörter stimmen nicht überein.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}