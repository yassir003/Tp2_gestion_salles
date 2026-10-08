package com.example.service;

import com.example.model.Utilisateur;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class UtilisateurService extends AbstractCrudService<Utilisateur, Long> {

    public UtilisateurService(EntityManagerFactory emf) {
        super(emf);
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return executeReadOnly(em -> {
            List<Utilisateur> results = em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        });
    }
}