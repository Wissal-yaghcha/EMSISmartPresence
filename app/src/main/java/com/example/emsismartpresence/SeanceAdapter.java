package com.example.emsismartpresence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeanceAdapter extends RecyclerView.Adapter<SeanceAdapter.ViewHolder> {

    private List<Seance> seances;
    private boolean isRattrapage;

    public SeanceAdapter(List<Seance> seances, boolean isRattrapage) {
        this.seances = seances;
        this.isRattrapage = isRattrapage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Seance s = seances.get(position);
        holder.jourOuDate.setText(isRattrapage ? "Date : " + s.getDate() : "Jour : " + s.getJour());
        holder.heure.setText("Heure : " + s.getHeure());
        holder.matiere.setText("Matière : " + s.getMatiere());
        holder.groupe.setText("Groupe : " + s.getGroupe());
        holder.salle.setText("Salle : " + s.getSalle());
    }

    @Override
    public int getItemCount() {
        return seances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jourOuDate, heure, matiere, groupe, salle;

        public ViewHolder(View itemView) {
            super(itemView);
            jourOuDate = itemView.findViewById(R.id.text_jour_ou_date);
            heure = itemView.findViewById(R.id.text_heure);
            matiere = itemView.findViewById(R.id.text_matiere);
            groupe = itemView.findViewById(R.id.text_groupe);
            salle = itemView.findViewById(R.id.text_salle);
        }
    }
}
