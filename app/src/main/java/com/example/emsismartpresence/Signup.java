package com.example.emsismartpresence;

import android.content.Intent; // Import nécessaire pour l'intent
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Signup extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etFullName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        // Set OnClickListener for the Sign Up button
        findViewById(R.id.btn_signup).setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Check if any fields are empty
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, navigate to the login screen
                            Toast.makeText(Signup.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this, Login.class); // Go to Login Activity
                            startActivity(intent);
                        } else {
                            // If registration fails, show a message
                            Toast.makeText(Signup.this, "L'inscription a échoué. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
