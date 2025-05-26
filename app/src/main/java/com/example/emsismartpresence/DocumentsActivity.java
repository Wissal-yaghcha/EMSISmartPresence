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

public class DocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;

    private List<FicheDocument> documentList;
    private List<String> documentIdList;
    private DocumentAdapter adapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        recyclerView = findViewById(R.id.recycler_documents);
        progressBar = findViewById(R.id.progress_bar);
        emptyView = findViewById(R.id.text_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        documentList = new ArrayList<>();
        documentIdList = new ArrayList<>();
        adapter = new DocumentAdapter(documentList, documentIdList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadDocuments();
    }

    private void loadDocuments() {
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        documentList.clear();
        documentIdList.clear();

        db.collection("documents")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            FicheDocument document = doc.toObject(FicheDocument.class);
                            documentList.add(document);
                            documentIdList.add(doc.getId());
                        }

                        adapter.notifyDataSetChanged();

                        if (documentList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(this, "Erreur de chargement des documents", Toast.LENGTH_SHORT).show();
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
    }
}
