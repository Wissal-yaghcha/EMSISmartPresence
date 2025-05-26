package com.example.emsismartpresence;

public class Absence {

    private String id;
    private String etudiantId;
    private String seanceId;
    private String date; // Format "yyyy-MM-dd"
    private String remarque;
    private boolean justifiee;
    private String professeurId;
    private String groupeId;

    // Constructeur vide requis pour Firestore
    public Absence() {}

    public Absence(String etudiantId, String seanceId, com.google.firebase.Timestamp date, String remarque, String professeurId, String groupeId) {
        this.etudiantId = etudiantId;
        this.seanceId = seanceId;
        this.date = date.toDate().toString(); // Tu peux aussi formater proprement si besoin
        this.remarque = remarque;
        this.professeurId = professeurId;
        this.groupeId = groupeId;
        this.justifiee = false;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(String etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(String seanceId) {
        this.seanceId = seanceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public boolean isJustifiee() {
        return justifiee;
    }

    public void setJustifiee(boolean justifiee) {
        this.justifiee = justifiee;
    }

    public String getProfesseurId() {
        return professeurId;
    }

    public void setProfesseurId(String professeurId) {
        this.professeurId = professeurId;
    }

    public String getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(String groupeId) {
        this.groupeId = groupeId;
    }
}
