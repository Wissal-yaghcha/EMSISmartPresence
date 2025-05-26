package com.example.emsismartpresence;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

public class MarquerAbsencesActivity extends AppCompatActivity {

    private TextView textGroupe, textDate, textMatiere, emptyView;
    private RecyclerView recyclerView;
    private EditText editRemarque;
    private android.widget.Button btnSave;
    private ProgressBar progressBar;
    private ImageView backButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private String groupeId, groupeNom, matiere, seanceId;
    private Date selectedDate;
    private List<Etudiant> etudiantsList;
    private EtudiantAbsenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquer_absences);

        // Récupération des données de l'Intent
        if (getIntent().getExtras() != null) {
            groupeId = getIntent().getStringExtra("groupeId");
            groupeNom = getIntent().getStringExtra("groupeNom");
            selectedDate = new Date(getIntent().getLongExtra("date", System.currentTimeMillis()));
            matiere = getIntent().getStringExtra("matiere");
            Log.d("DEBUG", "groupeId reçu = " + groupeId);
        } else {
            Toast.makeText(this, "Erreur: données manquantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialisation Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Générer un ID unique pour la séance
        seanceId = db.collection("seances").document().getId();

        // Initialisation de l'UI
        textGroupe = findViewById(R.id.text_groupe);
        textDate = findViewById(R.id.text_date);
        textMatiere = findViewById(R.id.text_matiere);
        recyclerView = findViewById(R.id.recycler_etudiants);
        editRemarque = findViewById(R.id.edit_remarque);
        btnSave = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progress_bar);
        emptyView = findViewById(R.id.empty_view);
        backButton = findViewById(R.id.back_button);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textGroupe.setText("Groupe: " + groupeNom);
        textDate.setText("Date: " + sdf.format(selectedDate));
        textMatiere.setText("Matière: " + matiere);

        etudiantsList = new ArrayList<>();
        adapter = new EtudiantAbsenceAdapter(etudiantsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (adapter.hasAbsences()) {
                showConfirmationDialog();
            } else {
                Toast.makeText(this, "Aucune absence à enregistrer", Toast.LENGTH_SHORT).show();
            }
        });

        loadEtudiants();
    }

    private void loadEtudiants() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("etudiants")
                .whereEqualTo("groupeId", groupeId)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    etudiantsList.clear();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Etudiant etudiant = document.toObject(Etudiant.class);
                            etudiant.setId(document.getId());
                            etudiantsList.add(etudiant);
                        }

                        adapter = new EtudiantAbsenceAdapter(etudiantsList);
                        recyclerView.setAdapter(adapter);

                        if (etudiantsList.isEmpty()) {
                            Toast.makeText(this, "Aucun étudiant trouvé pour le groupe : " + groupeId, Toast.LENGTH_LONG).show();
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

                    } else {
                        Exception e = task.getException();
                        Toast.makeText(this, "Erreur de chargement : " + (e != null ? e.getMessage() : "inconnue"), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });
    }

    private void showConfirmationDialog() {
        List<Etudiant> absents = adapter.getAbsentEtudiants();

        new AlertDialog.Builder(this)
                .setTitle("Confirmer les absences")
                .setMessage("Vous allez marquer " + absents.size() + " étudiant(s) absent(s). Continuer ?")
                .setPositiveButton("Oui", (dialog, which) -> saveAbsences(absents))
                .setNegativeButton("Non", null)
                .show();
    }

    private void saveAbsences(List<Etudiant> absents) {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        String remarque = editRemarque.getText().toString().trim();
        Timestamp date = new Timestamp(selectedDate);

        db.runBatch(batch -> {
            for (Etudiant etudiant : absents) {
                Absence absence = new Absence(
                        etudiant.getId(),
                        seanceId,
                        date,
                        remarque,
                        currentUserId,
                        groupeId
                );
                batch.set(db.collection("absences").document(), absence);
            }
        }).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                // 🆕 Générer la liste des noms
                List<String> nomsAbsents = new ArrayList<>();
                for (Etudiant e : absents) {
                    nomsAbsents.add(e.getNom() + " " + e.getPrenom());
                }

                // Résumé à enregistrer dans "documents"
                Map<String, Object> document = new HashMap<>();
                document.put("groupeNom", groupeNom);
                document.put("matiere", matiere);
                document.put("date", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate));
                document.put("nombreAbsents", absents.size());
                document.put("remarque", remarque);
                document.put("etudiantsAbsents", nomsAbsents); // 🆕

                db.collection("documents")
                        .add(document)
                        .addOnSuccessListener(docRef -> {
                            Toast.makeText(this, "Absences enregistrées + fiche créée", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Absences OK, mais erreur fiche : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            finish();
                        });

            } else {
                btnSave.setEnabled(true);
                Toast.makeText(this, "Erreur: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
