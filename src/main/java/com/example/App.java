package com.example;

import com.example.model.Salle;
import com.example.model.Utilisateur;
import com.example.service.SalleService;
import com.example.service.UtilisateurService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App {

    private final UtilisateurService utilisateurService;
    private final SalleService salleService;

    public App(EntityManagerFactory emf) {
        this.utilisateurService = new UtilisateurService(emf);
        this.salleService = new SalleService(emf);
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-salles");
        try {
            App app = new App(emf);
            app.demarrer();
        } finally {
            emf.close();
        }
    }

    public void demarrer() {
        executerModuleUtilisateurs();
        executerModuleSalles();
    }

    private void executerModuleUtilisateurs() {
        System.out.println("\n======================================================");
        System.out.println("       EXÉCUTION DU MODULE UTILISATEURS               ");
        System.out.println("======================================================");

        System.out.println("\n>>> [1.1] Insertion des profils utilisateurs en base :");
        Utilisateur user1 = new Utilisateur("Benali", "Karim", "k.benali@emsi.ma");
        user1.setDateNaissance(LocalDate.of(1993, 4, 18));
        user1.setTelephone("+212611223344");

        Utilisateur user2 = new Utilisateur("El Amrani", "Yasmine", "y.elamrani@emsi.ma");
        user2.setDateNaissance(LocalDate.of(1996, 9, 25));
        user2.setTelephone("+212655667788");

        utilisateurService.save(user1);
        utilisateurService.save(user2);

        System.out.println("\n>>> [1.2] Récupération globale du répertoire des utilisateurs :");
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        utilisateurs.forEach(System.out::println);

        System.out.println("\n>>> [1.3] Sélection d'un profil via son identifiant (ID = 1) :");
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findById(1L);
        utilisateurOpt.ifPresent(System.out::println);

        System.out.println("\n>>> [1.4] Recherche d'un profil par adresse email (y.elamrani@emsi.ma) :");
        Optional<Utilisateur> utilisateurParEmail = utilisateurService.findByEmail("y.elamrani@emsi.ma");
        utilisateurParEmail.ifPresent(System.out::println);

        System.out.println("\n>>> [1.5] Mise à jour des coordonnées téléphoniques de l'utilisateur :");
        utilisateurOpt.ifPresent(u -> {
            u.setTelephone("+212699887700");
            utilisateurService.update(u);
            System.out.println("Fiche mise à jour : " + u);
        });

        System.out.println("\n>>> [1.6] Suppression du profil utilisateur identifié par l'ID = 2 :");
        utilisateurService.deleteById(2L);
        System.out.println("Utilisateur ID #2 retiré avec succès.");

        System.out.println("\n>>> [1.7] Synthèse des profils existants post-suppression :");
        utilisateurService.findAll().forEach(System.out::println);
    }

    private void executerModuleSalles() {
        System.out.println("\n======================================================");
        System.out.println("       EXÉCUTION DU MODULE SALLES                     ");
        System.out.println("======================================================");

        System.out.println("\n>>> [2.1] Insertion des salles et espaces pédagogiques :");
        Salle salle1 = new Salle("Laboratoire CyberSec", 28);
        salle1.setDescription("Laboratoire dédié aux travaux pratiques de sécurité");
        salle1.setEtage(1);

        Salle salle2 = new Salle("Auditorium Al Idrissi", 160);
        salle2.setDescription("Espace de conférences avec équipement audiovisuel");
        salle2.setEtage(0);

        Salle salle3 = new Salle("Salle Séminaire Beta", 15);
        salle3.setDescription("Salle restreinte pour sessions d'études et soutenances");
        salle3.setEtage(2);
        salle3.setDisponible(false);

        salleService.save(salle1);
        salleService.save(salle2);
        salleService.save(salle3);

        System.out.println("\n>>> [2.2] Affichage exhaustif du catalogue des salles :");
        List<Salle> salles = salleService.findAll();
        salles.forEach(System.out::println);

        System.out.println("\n>>> [2.3] Recherche ciblée par identifiant de salle (ID = 2) :");
        Optional<Salle> salleOpt = salleService.findById(2L);
        salleOpt.ifPresent(System.out::println);

        System.out.println("\n>>> [2.4] Recherche des salles opérationnelles et disponibles :");
        List<Salle> sallesDisponibles = salleService.findByDisponible(true);
        sallesDisponibles.forEach(System.out::println);

        System.out.println("\n>>> [2.5] Recherche des espaces d'une capacité minimale de 50 personnes :");
        List<Salle> sallesGrandes = salleService.findByCapaciteMinimum(50);
        sallesGrandes.forEach(System.out::println);

        System.out.println("\n>>> [2.6] Modification de la capacité maximale pour la salle sélectionnée :");
        salleOpt.ifPresent(s -> {
            s.setCapacite(220);
            salleService.update(s);
            System.out.println("Salle actualisée : " + s);
        });

        System.out.println("\n>>> [2.7] Suppression de la salle enregistrée avec l'ID = 3 :");
        salleService.deleteById(3L);
        System.out.println("Salle ID #3 retirée du référentiel.");

        System.out.println("\n>>> [2.8] Inventaire des salles disponibles après suppression :");
        salleService.findAll().forEach(System.out::println);
    }
}
