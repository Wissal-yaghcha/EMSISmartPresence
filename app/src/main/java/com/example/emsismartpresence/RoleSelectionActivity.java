package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Button btnProfessor = findViewById(R.id.btn_professor);
        Button btnStudent = findViewById(R.id.btn_student);

        btnProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'interface professeur (login)
                Intent intent = new Intent(RoleSelectionActivity.this, Login.class);
                startActivity(intent);
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'interface étudiant (sélection de groupe)
                Intent intent = new Intent(RoleSelectionActivity.this, ChoixGroupeActivity.class);
                startActivity(intent);
            }
        });
    }
}
