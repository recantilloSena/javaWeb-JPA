/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sena.webapp.controlador;

import com.sena.webapp.controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sena.webapp.modelo.Deportes;
import com.sena.webapp.modelo.Personas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author RICARDO
 */
public class PersonasDao implements Serializable {

    public PersonasDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Personas personas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deportes idDeporte = personas.getIdDeporte();
            if (idDeporte != null) {
                idDeporte = em.getReference(idDeporte.getClass(), idDeporte.getId());
                personas.setIdDeporte(idDeporte);
            }
            em.persist(personas);
            if (idDeporte != null) {
                idDeporte.getPersonasList().add(personas);
                idDeporte = em.merge(idDeporte);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personas personas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas persistentPersonas = em.find(Personas.class, personas.getId());
            Deportes idDeporteOld = persistentPersonas.getIdDeporte();
            Deportes idDeporteNew = personas.getIdDeporte();
            if (idDeporteNew != null) {
                idDeporteNew = em.getReference(idDeporteNew.getClass(), idDeporteNew.getId());
                personas.setIdDeporte(idDeporteNew);
            }
            personas = em.merge(personas);
            if (idDeporteOld != null && !idDeporteOld.equals(idDeporteNew)) {
                idDeporteOld.getPersonasList().remove(personas);
                idDeporteOld = em.merge(idDeporteOld);
            }
            if (idDeporteNew != null && !idDeporteNew.equals(idDeporteOld)) {
                idDeporteNew.getPersonasList().add(personas);
                idDeporteNew = em.merge(idDeporteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personas.getId();
                if (findById(id) == null) {
                    throw new NonexistentEntityException("The personas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas personas;
            try {
                personas = em.getReference(Personas.class, id);
                personas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personas with id " + id + " no longer exists.", enfe);
            }
            Deportes idDeporte = personas.getIdDeporte();
            if (idDeporte != null) {
                idDeporte.getPersonasList().remove(personas);
                idDeporte = em.merge(idDeporte);
            }
            em.remove(personas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personas> findAll() {
        return findPersonasEntities(true, -1, -1);
    }

    public List<Personas> findPersonasEntities(int maxResults, int firstResult) {
        return findPersonasEntities(false, maxResults, firstResult);
    }

    private List<Personas> findPersonasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Personas findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personas> rt = cq.from(Personas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
