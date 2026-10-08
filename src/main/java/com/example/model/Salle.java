package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "salles")
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le libellé de la salle est obligatoire")
    @Size(min = 2, max = 100, message = "Le libellé doit comporter entre 2 et 100 caractères")
    @Column(nullable = false)
    private String nom;

    @NotNull(message = "La capacité d'accueil est requise")
    @Min(value = 1, message = "La capacité minimale autorisée est de 1 personne")
    @Max(value = 1000, message = "La capacité maximale ne peut excéder 1000 personnes")
    @Column(nullable = false)
    private Integer capacite;

    @Size(max = 500, message = "Le descriptif ne doit pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @NotNull(message = "L'indicateur de disponibilité est requis")
    @Column(nullable = false)
    private Boolean disponible = Boolean.TRUE;

    @Min(value = 0, message = "Le numéro d'étage doit être supérieur ou égal à zéro")
    private Integer etage;

    public Salle() {
    }

    public Salle(String nom, Integer capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    public Salle(String nom, Integer capacite, String description, Integer etage, Boolean disponible) {
        this.nom = nom;
        this.capacite = capacite;
        this.description = description;
        this.etage = etage;
        this.disponible = disponible != null ? disponible : Boolean.TRUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getEtage() {
        return etage;
    }

    public void setEtage(Integer etage) {
        this.etage = etage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salle)) return false;
        Salle salle = (Salle) o;
        return Objects.equals(id, salle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", capacite=" + capacite +
                ", description='" + description + '\'' +
                ", disponible=" + disponible +
                ", etage=" + etage +
                '}';
    }
}