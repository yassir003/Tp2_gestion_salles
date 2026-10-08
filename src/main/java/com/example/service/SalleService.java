package com.example.service;

import com.example.model.Salle;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SalleService extends AbstractCrudService<Salle, Long> {

    public SalleService(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Salle> findByDisponible(boolean disponible) {
        return executeReadOnly(em -> em.createQuery(
                "SELECT s FROM Salle s WHERE s.disponible = :disponible", Salle.class)
                .setParameter("disponible", disponible)
                .getResultList());
    }

    public List<Salle> findByCapaciteMinimum(int capaciteMin) {
        return executeReadOnly(em -> em.createQuery(
                "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin", Salle.class)
                .setParameter("capaciteMin", capaciteMin)
                .getResultList());
    }
}
