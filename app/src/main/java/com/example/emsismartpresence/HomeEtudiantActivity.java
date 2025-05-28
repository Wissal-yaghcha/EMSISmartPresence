package com.example.emsismartpresence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeEtudiantActivity extends AppCompatActivity {

    private String selectedGroup;
    private TextView tvGroupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_etudiant);

        // Récupérer le groupe sélectionné
        if (getIntent().hasExtra("groupe")) {
            selectedGroup = getIntent().getStringExtra("groupe");
        } else {
            // Si pas dans l'intent, vérifier dans les préférences
            SharedPreferences sharedPreferences = getSharedPreferences(ChoixGroupeActivity.PREFS_NAME, MODE_PRIVATE);
            selectedGroup = sharedPreferences.getString(ChoixGroupeActivity.KEY_GROUPE, "G1"); // G1 par défaut
        }

        // Initialiser les vues
        tvGroupInfo = findViewById(R.id.tv_group_info);
        tvGroupInfo.setText("Groupe sélectionné : " + selectedGroup);

        // Flèche de retour pour changer de groupe
        ImageView back = findViewById(R.id.ic_arrow_back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, ChoixGroupeActivity.class);
            startActivity(intent);
            finish();  // Ferme cette activité pour éviter de revenir ici par erreur
        });

        // Configuration des cartes de fonctionnalités
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        // Emploi du temps
        CardView cardEmploi = findViewById(R.id.card_emploi);
        cardEmploi.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, EmploiDuTempsActivity.class);
            intent.putExtra("groupe", selectedGroup);
            startActivity(intent);
        });

        // Rattrapages
        CardView cardRattrapages = findViewById(R.id.card_rattrapages);
        cardRattrapages.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, RattrapagesActivity.class);
            intent.putExtra("groupe", selectedGroup);
            startActivity(intent);
        });

        // Changer de profil (retour à la sélection de rôle)
        CardView cardChangeProfil = findViewById(R.id.card_change_profile);
        cardChangeProfil.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEtudiantActivity.this, RoleSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();  // Ferme cette activité pour éviter de revenir ici par erreur
        });
    }
}
