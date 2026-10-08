package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {

    protected final EntityManagerFactory emf;
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractCrudService(EntityManagerFactory emf) {
        this.emf = emf;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public AbstractCrudService(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    protected void runInTransaction(Consumer<EntityManager> action) {
        executeInTransaction(em -> {
            action.accept(em);
            return null;
        });
    }

    protected <R> R executeReadOnly(Function<EntityManager, R> query) {
        EntityManager em = emf.createEntityManager();
        try {
            return query.apply(em);
        } finally {
            em.close();
        }
    }

    @Override
    public T save(T entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Optional<T> findById(ID id) {
        return executeReadOnly(em -> Optional.ofNullable(em.find(entityClass, id)));
    }

    @Override
    public List<T> findAll() {
        return executeReadOnly(em -> {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        });
    }

    @Override
    public void update(T entity) {
        runInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(T entity) {
        runInTransaction(em -> {
            T managed = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managed);
        });
    }

    @Override
    public void deleteById(ID id) {
        runInTransaction(em -> {
            T target = em.find(entityClass, id);
            if (target != null) {
                em.remove(target);
            }
        });
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}