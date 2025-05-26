package com.example.emsismartpresence;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private List<FicheDocument> documents;
    private List<String> documentIds;
    private Context context;

    public DocumentAdapter(List<FicheDocument> documents, List<String> documentIds, Context context) {
        this.documents = documents;
        this.documentIds = documentIds;
        this.context = context;
    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.ViewHolder holder, int position) {
        FicheDocument doc = documents.get(position);
        holder.groupe.setText("Groupe: " + doc.getGroupeNom());
        holder.matiere.setText("Matière: " + doc.getMatiere());
        holder.date.setText("Date: " + doc.getDate());
        holder.absents.setText("Absents: " + doc.getNombreAbsents());

        // Afficher remarque
        String remarqueText = doc.getRemarque();
        if (remarqueText != null && !remarqueText.isEmpty()) {
            holder.remarque.setText("Remarque : " + remarqueText);
        } else {
            holder.remarque.setText("Remarque : Aucune");
        }

        // Afficher liste des absents
        List<String> liste = doc.getEtudiantsAbsents();
        if (liste != null && !liste.isEmpty()) {
            StringBuilder noms = new StringBuilder();
            for (String nom : liste) {
                noms.append("• ").append(nom).append("\n");
            }
            holder.absentNames.setText(noms.toString().trim());
        } else {
            holder.absentNames.setText("Aucun nom listé");
        }

        // 🗑️ Bouton Supprimer
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Supprimer la fiche")
                    .setMessage("Voulez-vous vraiment supprimer cette fiche ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        String docId = documentIds.get(position);
                        FirebaseFirestore.getInstance().collection("documents")
                                .document(docId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Fiche supprimée", Toast.LENGTH_SHORT).show();
                                    documents.remove(position);
                                    documentIds.remove(position);
                                    notifyItemRemoved(position);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        // 📝 Bouton Modifier
        holder.btnEdit.setOnClickListener(v -> {
            String docId = documentIds.get(position);
            String ancienneRemarque = doc.getRemarque();

            final EditText input = new EditText(context);
            input.setText(ancienneRemarque != null ? ancienneRemarque : "");
            input.setHint("Nouvelle remarque");

            new AlertDialog.Builder(context)
                    .setTitle("Modifier la remarque")
                    .setView(input)
                    .setPositiveButton("Enregistrer", (dialog, which) -> {
                        String nouvelleRemarque = input.getText().toString().trim();

                        FirebaseFirestore.getInstance().collection("documents")
                                .document(docId)
                                .update("remarque", nouvelleRemarque)
                                .addOnSuccessListener(aVoid -> {
                                    doc.setRemarque(nouvelleRemarque);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Remarque mise à jour", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupe, matiere, date, absents, remarque, absentNames;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            groupe = itemView.findViewById(R.id.text_groupe);
            matiere = itemView.findViewById(R.id.text_matiere);
            date = itemView.findViewById(R.id.text_date);
            absents = itemView.findViewById(R.id.text_absents);
            remarque = itemView.findViewById(R.id.text_remarque);
            absentNames = itemView.findViewById(R.id.text_absents_names);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
