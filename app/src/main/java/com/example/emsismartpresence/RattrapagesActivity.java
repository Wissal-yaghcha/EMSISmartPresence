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

public class RattrapagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;

    private FirebaseFirestore db;

    private List<Seance> rattrapageList;
    private SeanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rattrapages);

        recyclerView = findViewById(R.id.recycler_rattrapages);
        progressBar = findViewById(R.id.progressBar);
        emptyText = findViewById(R.id.text_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rattrapageList = new ArrayList<>();
        adapter = new SeanceAdapter(rattrapageList, true);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadRattrapages();
    }

    private void loadRattrapages() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        db.collection("rattrapages").get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            rattrapageList.clear();

            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Seance seance = doc.toObject(Seance.class);
                    rattrapageList.add(seance);
                }

                adapter.notifyDataSetChanged();

                if (rattrapageList.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                emptyText.setVisibility(View.VISIBLE);
            }
        });
    }
}
