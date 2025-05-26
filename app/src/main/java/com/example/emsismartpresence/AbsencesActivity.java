package com.example.emsismartpresence;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

public class AbsencesActivity extends AppCompatActivity {

    private Spinner spinnerGroupes;
    private android.widget.Button btnSelectDate, btnContinue;
    private EditText editMatiere;
    private ProgressBar progressBar;
    private String selectedDate = "";

    private FirebaseFirestore db;
    private List<String> groupesList = new ArrayList<>();
    private List<String> groupesIdList = new ArrayList<>();
    private ArrayAdapter<String> groupesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absences);

        // Initialiser Firebase
        db = FirebaseFirestore.getInstance();

        // Liaison avec les éléments UI
        spinnerGroupes = findViewById(R.id.spinner_groupe);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnContinue = findViewById(R.id.btn_continue);
        editMatiere = findViewById(R.id.edit_matiere);
        progressBar = findViewById(R.id.progress_bar);

        // Adapter simple pour les noms de groupes
        groupesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupesList);
        groupesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupes.setAdapter(groupesAdapter);

        // Charger les groupes Firestore
        loadGroupes();

        // Initialiser la date du jour
        Calendar calendar = Calendar.getInstance();
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        btnSelectDate.setText(selectedDate);

        // Sélecteur de date
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        // Continuer vers marquage
        btnContinue.setOnClickListener(v -> {
            int selectedIndex = spinnerGroupes.getSelectedItemPosition();

            if (groupesList.isEmpty() || groupesIdList.isEmpty() || selectedIndex < 0 || selectedIndex >= groupesList.size()) {
                Toast.makeText(this, "Veuillez sélectionner un groupe valide", Toast.LENGTH_SHORT).show();
                return;
            }

            String groupeId = groupesIdList.get(selectedIndex);
            String groupeNom = groupesList.get(selectedIndex);
            String matiere = editMatiere.getText().toString().trim();

            if (matiere.isEmpty()) {
                editMatiere.setError("Veuillez entrer une matière");
                return;
            }

            // Démarrer MarquerAbsencesActivity
            Intent intent = new Intent(this, MarquerAbsencesActivity.class);
            intent.putExtra("groupeId", groupeId);
            intent.putExtra("groupeNom", groupeNom);
            intent.putExtra("matiere", matiere);
            intent.putExtra("date", Calendar.getInstance().getTimeInMillis());
            startActivity(intent);
        });
    }

    private void loadGroupes() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("groupes").get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            groupesList.clear();
            groupesIdList.clear();

            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String nom = doc.getString("nom");
                    if (nom != null && !nom.isEmpty()) {
                        groupesList.add(nom);
                        groupesIdList.add(doc.getId());
                    }
                }

                groupesAdapter.notifyDataSetChanged();

                if (groupesList.isEmpty()) {
                    Toast.makeText(this, "Aucun groupe trouvé.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Exception e = task.getException();
                Log.e("GROUPES", "Erreur Firestore : ", e);
                Toast.makeText(this, "Erreur : " + (e != null ? e.getMessage() : "inconnue"), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            btnSelectDate.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
