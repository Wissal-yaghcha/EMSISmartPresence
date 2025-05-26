package com.example.emsismartpresence;

import java.util.List;

public class FicheDocument {
    private String groupeNom;
    private String matiere;
    private String date;
    private int nombreAbsents;
    private String remarque;
    private List<String> etudiantsAbsents;

    public FicheDocument() {}

    public FicheDocument(String groupeNom, String matiere, String date, int nombreAbsents, String remarque, List<String> etudiantsAbsents) {
        this.groupeNom = groupeNom;
        this.matiere = matiere;
        this.date = date;
        this.nombreAbsents = nombreAbsents;
        this.remarque = remarque;
        this.etudiantsAbsents = etudiantsAbsents;
    }

    public String getGroupeNom() { return groupeNom; }
    public String getMatiere() { return matiere; }
    public String getDate() { return date; }
    public int getNombreAbsents() { return nombreAbsents; }
    public String getRemarque() { return remarque; }
    public List<String> getEtudiantsAbsents() { return etudiantsAbsents; }

    public void setRemarque(String nouvelleRemarque) {
    }
}
