package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeEtudiantActivity extends AppCompatActivity {

    private Button btnEmploi;
    private Button btnRattrapages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_etudiant);

        btnEmploi = findViewById(R.id.btn_emploi);
        btnRattrapages = findViewById(R.id.btn_rattrapages);

        String groupe = getIntent().getStringExtra("groupe");

        btnEmploi.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, EmploiDuTempsActivity.class);
            intent.putExtra("isProf", false);
            intent.putExtra("groupe", groupe);
            startActivity(intent);
        });

        btnRattrapages.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, RattrapagesActivity.class);
            intent.putExtra("isProf", false);
            intent.putExtra("groupe", groupe);
            startActivity(intent);
        });
    }
}
