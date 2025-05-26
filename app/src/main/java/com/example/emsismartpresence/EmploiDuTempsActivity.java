package com.example.emsismartpresence;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmploiDuTempsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;

    private FirebaseFirestore db;

    private List<Seance> emploiList;
    private SeanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploi_du_temps);

        recyclerView = findViewById(R.id.recycler_emploi);
        progressBar = findViewById(R.id.progressBar);
        emptyText = findViewById(R.id.text_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        emploiList = new ArrayList<>();
        adapter = new SeanceAdapter(emploiList, false);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadEmploi();
    }

    private void loadEmploi() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        db.collection("emplois").get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            emploiList.clear();

            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Seance seance = doc.toObject(Seance.class);
                    emploiList.add(seance);
                }

                adapter.notifyDataSetChanged();

                if (emploiList.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Erreur lors du chargement", Toast.LENGTH_SHORT).show();
                emptyText.setVisibility(View.VISIBLE);
            }
        });
    }
}
