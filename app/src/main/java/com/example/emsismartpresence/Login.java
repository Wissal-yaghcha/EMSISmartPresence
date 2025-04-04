package com.example.emsismartpresence;

import android.content.Intent; // Import nécessaire pour l'intent
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private View btnLogin;
    private final String validEmail = "user@example.com";
    private final String validPassword = "123456";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // Utilisation correcte de btnLogin sans redéclaration
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
            }
        });
    }

    private void authenticateUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Success authentification", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Login.this, "Authentification failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}









