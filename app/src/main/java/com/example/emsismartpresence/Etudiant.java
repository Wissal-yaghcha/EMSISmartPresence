package com.example.emsismartpresence;

public class Etudiant {
    private String id;
    private String nom;
    private String prenom;
    private String groupeId;
    private boolean absent;

    public Etudiant() {}

    public Etudiant(String id, String nom, String prenom, String groupeId) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.groupeId = groupeId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getGroupeId() { return groupeId; }
    public void setGroupeId(String groupeId) { this.groupeId = groupeId; }

    public boolean isAbsent() { return absent; }
    public void setAbsent(boolean absent) { this.absent = absent; }
}
