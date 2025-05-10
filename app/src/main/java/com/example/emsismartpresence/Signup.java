package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import com.google.firebase.Timestamp;

public class Signup extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Firestore instance
    private String fullName, email, password, confirmPassword, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etFullName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        // OnClickListener for SignUp button
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });
    }

    public void redirectToLogin(View view) {
        Intent intent = new Intent(Signup.this, Login.class); // Redirect to Login
        startActivity(intent);
    }

    private void handleSignUp() {
        // Get input values from the EditTexts
        fullName = etFullName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        // Check if any field is empty
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, store user in Firestore
                            String userId = mAuth.getCurrentUser().getUid();
                            storeUserInFirestore(userId, email, fullName);

                            // Navigate to Login screen
                            Toast.makeText(Signup.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this, Login.class);
                            startActivity(intent);
                        } else {
                            // If registration fails, show message
                            Toast.makeText(Signup.this, "L'inscription a échoué. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to store user information in Firestore
    private void storeUserInFirestore(String uid, String email, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("user_email", email);
        user.put("date_inscription", new Timestamp(new Date()));
        user.put("name", name);

        // Save user data to Firestore
        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Signup.this, "Utilisateur créé", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Signup.this, "Échec de la création de l'utilisateur", Toast.LENGTH_LONG).show();
                });
    }
}
