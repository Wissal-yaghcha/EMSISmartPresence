package com.example.emsismartpresence;

public class Seance {
    private String profId;
    private String jour;       // pour emploi
    private String date;       // pour rattrapage
    private String heure;
    private String matiere;
    private String groupe;
    private String salle;

    public Seance() {}

    public String getProfId() { return profId; }
    public String getJour() { return jour; }
    public String getDate() { return date; }
    public String getHeure() { return heure; }
    public String getMatiere() { return matiere; }
    public String getGroupe() { return groupe; }
    public String getSalle() { return salle; }

    public void setProfId(String profId) { this.profId = profId; }
    public void setJour(String jour) { this.jour = jour; }
    public void setDate(String date) { this.date = date; }
    public void setHeure(String heure) { this.heure = heure; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public void setGroupe(String groupe) { this.groupe = groupe; }
    public void setSalle(String salle) { this.salle = salle; }
}
