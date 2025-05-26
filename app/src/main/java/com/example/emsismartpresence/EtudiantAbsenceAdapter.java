package com.example.emsismartpresence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EtudiantAbsenceAdapter extends RecyclerView.Adapter<EtudiantAbsenceAdapter.ViewHolder> {

    private List<Etudiant> etudiants;

    public EtudiantAbsenceAdapter(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etudiant_absence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Etudiant etudiant = etudiants.get(position);
        String nomComplet = etudiant.getPrenom() + " " + etudiant.getNom();
        holder.nomText.setText(nomComplet);
        holder.absentCheckBox.setChecked(etudiant.isAbsent());

        holder.absentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etudiant.setAbsent(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public List<Etudiant> getAbsentEtudiants() {
        List<Etudiant> absents = new java.util.ArrayList<>();
        for (Etudiant e : etudiants) {
            if (e.isAbsent()) {
                absents.add(e);
            }
        }
        return absents;
    }

    public boolean hasAbsences() {
        for (Etudiant e : etudiants) {
            if (e.isAbsent()) {
                return true;
            }
        }
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomText;
        CheckBox absentCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            nomText = itemView.findViewById(R.id.nom_etudiant);
            absentCheckBox = itemView.findViewById(R.id.checkbox_absent);
        }
    }
}
