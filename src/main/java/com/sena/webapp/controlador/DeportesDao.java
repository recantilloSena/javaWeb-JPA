package com.sena.webapp.controlador;

import com.sena.webapp.controlador.exceptions.IllegalOrphanException;
import com.sena.webapp.controlador.exceptions.NonexistentEntityException;
import com.sena.webapp.controlador.exceptions.PreexistingEntityException;
import com.sena.webapp.modelo.Deportes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sena.webapp.modelo.Personas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author RICARDO
 */
public class DeportesDao implements Serializable {

    public DeportesDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Deportes deportes) throws PreexistingEntityException, Exception {
        if (deportes.getPersonasList() == null) {
            deportes.setPersonasList(new ArrayList<Personas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personas> attachedPersonasList = new ArrayList<Personas>();
            for (Personas personasListPersonasToAttach : deportes.getPersonasList()) {
                personasListPersonasToAttach = em.getReference(personasListPersonasToAttach.getClass(), personasListPersonasToAttach.getId());
                attachedPersonasList.add(personasListPersonasToAttach);
            }
            deportes.setPersonasList(attachedPersonasList);
            em.persist(deportes);
            for (Personas personasListPersonas : deportes.getPersonasList()) {
                Deportes oldIdDeporteOfPersonasListPersonas = personasListPersonas.getIdDeporte();
                personasListPersonas.setIdDeporte(deportes);
                personasListPersonas = em.merge(personasListPersonas);
                if (oldIdDeporteOfPersonasListPersonas != null) {
                    oldIdDeporteOfPersonasListPersonas.getPersonasList().remove(personasListPersonas);
                    oldIdDeporteOfPersonasListPersonas = em.merge(oldIdDeporteOfPersonasListPersonas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findById(deportes.getId()) != null) {
                throw new PreexistingEntityException("Deportes " + deportes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deportes deportes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deportes persistentDeportes = em.find(Deportes.class, deportes.getId());
            List<Personas> personasListOld = persistentDeportes.getPersonasList();
            List<Personas> personasListNew = deportes.getPersonasList();
            List<String> illegalOrphanMessages = null;
            for (Personas personasListOldPersonas : personasListOld) {
                if (!personasListNew.contains(personasListOldPersonas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personas " + personasListOldPersonas + " since its idDeporte field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personas> attachedPersonasListNew = new ArrayList<Personas>();
            for (Personas personasListNewPersonasToAttach : personasListNew) {
                personasListNewPersonasToAttach = em.getReference(personasListNewPersonasToAttach.getClass(), personasListNewPersonasToAttach.getId());
                attachedPersonasListNew.add(personasListNewPersonasToAttach);
            }
            personasListNew = attachedPersonasListNew;
            deportes.setPersonasList(personasListNew);
            deportes = em.merge(deportes);
            for (Personas personasListNewPersonas : personasListNew) {
                if (!personasListOld.contains(personasListNewPersonas)) {
                    Deportes oldIdDeporteOfPersonasListNewPersonas = personasListNewPersonas.getIdDeporte();
                    personasListNewPersonas.setIdDeporte(deportes);
                    personasListNewPersonas = em.merge(personasListNewPersonas);
                    if (oldIdDeporteOfPersonasListNewPersonas != null && !oldIdDeporteOfPersonasListNewPersonas.equals(deportes)) {
                        oldIdDeporteOfPersonasListNewPersonas.getPersonasList().remove(personasListNewPersonas);
                        oldIdDeporteOfPersonasListNewPersonas = em.merge(oldIdDeporteOfPersonasListNewPersonas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = deportes.getId();
                if (findById(id) == null) {
                    throw new NonexistentEntityException("The deportes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deportes deportes;
            try {
                deportes = em.getReference(Deportes.class, id);
                deportes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deportes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personas> personasListOrphanCheck = deportes.getPersonasList();
            for (Personas personasListOrphanCheckPersonas : personasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Deportes (" + deportes + ") cannot be destroyed since the Personas " + personasListOrphanCheckPersonas + " in its personasList field has a non-nullable idDeporte field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(deportes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deportes> findAll() {
        return findDeportesEntities(true, -1, -1);
    }

    public List<Deportes> findDeportesEntities(int maxResults, int firstResult) {
        return findDeportesEntities(false, maxResults, firstResult);
    }

    private List<Deportes> findDeportesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deportes.class));
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

    public Deportes findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deportes.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeportesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deportes> rt = cq.from(Deportes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
