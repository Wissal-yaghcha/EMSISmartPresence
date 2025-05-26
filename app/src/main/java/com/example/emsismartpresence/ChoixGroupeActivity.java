package com.example.emsismartpresence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChoixGroupeActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String KEY_GROUPE = "groupe_etudiant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_groupe);

        Button btnG1 = findViewById(R.id.btn_g1);
        Button btnG2 = findViewById(R.id.btn_g2);

        btnG1.setOnClickListener(v -> enregistrerGroupeEtLancer("G1"));
        btnG2.setOnClickListener(v -> enregistrerGroupeEtLancer("G2"));
    }

    private void enregistrerGroupeEtLancer(String groupe) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_GROUPE, groupe).apply();

        Toast.makeText(this, "Groupe " + groupe + " sélectionné", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HomeEtudiantActivity.class);
        intent.putExtra("groupe", groupe);
        startActivity(intent);
        finish();
    }
}
