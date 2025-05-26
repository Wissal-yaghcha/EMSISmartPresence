package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button btnProf;
    private Button btnEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        btnProf = findViewById(R.id.btn_prof);
        btnEtudiant = findViewById(R.id.btn_etudiant);

        // Si l'utilisateur choisit "Professeur", redirection vers login
        btnProf.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, Login.class);
            intent.putExtra("role", "prof");
            startActivity(intent);
            finish();
        });

        // Si l'utilisateur choisit "Étudiant", redirection vers ChoixGroupeActivity
        btnEtudiant.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, ChoixGroupeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
