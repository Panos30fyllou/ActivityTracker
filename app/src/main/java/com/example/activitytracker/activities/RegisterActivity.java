package com.example.activitytracker.activities;

import static com.example.activitytracker.validators.Validators.isEditTextFilled;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitytracker.R;
import com.example.activitytracker.models.User;
import com.example.activitytracker.navigators.Navigator;
import com.example.activitytracker.validators.Validators;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    ArrayList<EditText> registerFormEditTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFormEditTexts = new ArrayList<>();

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        registerFormEditTexts.add(fullNameEditText);
        registerFormEditTexts.add(emailEditText);
        registerFormEditTexts.add(usernameEditText);
        registerFormEditTexts.add(passwordEditText);
        registerFormEditTexts.add(confirmPasswordEditText);

        for (EditText editText : registerFormEditTexts) {
            editText.setOnFocusChangeListener((view, b) -> {
                if (!b && isEditTextFilled((EditText) view)) {
                    if (view.getId() == emailEditText.getId()) {
                        if (!Validators.isValidEmail(emailEditText.getText().toString())) {
                            emailEditText.setError(getString(R.string.email_invalid));
                            Toast.makeText(RegisterActivity.this, getString(R.string.email_invalid), Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == usernameEditText.getId()) {
                        if (!Validators.isValidUsername(usernameEditText.getText().toString())) {
                            usernameEditText.setError(getString(R.string.username_invalid));
                            Toast.makeText(RegisterActivity.this, getString(R.string.username_invalid_detailed), Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == passwordEditText.getId()) {
                        if (!Validators.isValidPassword(passwordEditText.getText().toString())) {
                            passwordEditText.setError(getString(R.string.password_invalid));
                            Toast.makeText(RegisterActivity.this, getString(R.string.password_invalid_detailed), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    public void registerClicked(View v) {
        String fullName = isEditTextFilled(fullNameEditText) ? fullNameEditText.getText().toString() : null;
        if (fullName == null) return;
        String email = isEditTextFilled(emailEditText) ? emailEditText.getText().toString() : null;
        if (email == null) return;
        String username = isEditTextFilled(usernameEditText) ? usernameEditText.getText().toString() : null;
        if (username == null) return;
        String password = isEditTextFilled(passwordEditText) ? passwordEditText.getText().toString() : null;
        if (password == null) return;
        String confirmation = isEditTextFilled(confirmPasswordEditText) ? confirmPasswordEditText.getText().toString() : null;
        if (confirmation == null) return;

        if (validateAccountInfo(email, username, password)) {
            if (Objects.requireNonNull(password).equals(confirmation))
                createUserInFirebase(new User(username, email, fullName), password);
            else {
                confirmPasswordEditText.setError(getString(R.string.passwords_dont_match));
                confirmPasswordEditText.requestFocus();
            }
        }
    }

    private void createUserInFirebase(User user, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(userCreationTask -> {
            if (userCreationTask.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUsername()).setValue(user).addOnCompleteListener(addUserToDbTask -> {
                    if (addUserToDbTask.isSuccessful())
                        Navigator.goToLogin(RegisterActivity.this);
                    Toast.makeText(RegisterActivity.this, addUserToDbTask.isSuccessful() ? getString(R.string.registration_success) : getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
                });
            } else
                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(Objects.requireNonNull(userCreationTask.getException()).getMessage()), Toast.LENGTH_SHORT).show();
        });
    }

    public boolean validateAccountInfo(String email, String username, String password) {
        if (!Validators.isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.email_invalid), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validators.isValidUsername(username)) {
            Toast.makeText(this, getString(R.string.username_invalid_detailed), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validators.isValidPassword(password)) {
            Toast.makeText(this, getString(R.string.password_invalid_detailed), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void goToLogin(View v) {
        Navigator.goToLogin(this);
    }
}