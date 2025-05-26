package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private View btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> authenticateUser());
    }

    private void authenticateUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d("Login", "Authentification démarrée avec email: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d("Login", "Connexion terminée, succès: " + task.isSuccessful());
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Log.d("Login", "Utilisateur connecté avec uid: " + uid);
                        checkUserRole(uid);
                    } else {
                        Toast.makeText(Login.this, "Authentification échouée", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void checkUserRole(String uid) {
        Log.d("Login", "Vérification du rôle pour l'UID: " + uid);

        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null && doc.exists()) {
                            String role = doc.getString("role");
                            Log.d("Login", "Rôle récupéré: " + role);
                            if ("prof".equalsIgnoreCase(role)) {
                                Log.d("Login", "Utilisateur est un professeur");
                                // Redirection vers Home (Prof)
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("isProf", true); // Ajout de l'extra pour Home
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("Login", "Utilisateur est un étudiant");
                                // Redirection vers ChoixGroupeActivity (Étudiant)
                                Intent intent = new Intent(Login.this, ChoixGroupeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Log.d("Login", "Document utilisateur non trouvé");
                            Intent intent = new Intent(Login.this, ChoixGroupeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("Login", "Erreur récupération rôle", task.getException());
                    }
                });
    }

}
