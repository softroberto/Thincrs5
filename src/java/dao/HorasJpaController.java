/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Horas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Roberto Avelar
 */
public class HorasJpaController implements Serializable {

    public HorasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horas horas) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario id = horas.getId();
            if (id != null) {
                id = em.getReference(id.getClass(), id.getId());
                horas.setId(id);
            }
            em.persist(horas);
            if (id != null) {
                id.getHorasCollection().add(horas);
                id = em.merge(id);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Horas horas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horas persistentHoras = em.find(Horas.class, horas.getNoDia());
            Usuario idOld = persistentHoras.getId();
            Usuario idNew = horas.getId();
            if (idNew != null) {
                idNew = em.getReference(idNew.getClass(), idNew.getId());
                horas.setId(idNew);
            }
            horas = em.merge(horas);
            if (idOld != null && !idOld.equals(idNew)) {
                idOld.getHorasCollection().remove(horas);
                idOld = em.merge(idOld);
            }
            if (idNew != null && !idNew.equals(idOld)) {
                idNew.getHorasCollection().add(horas);
                idNew = em.merge(idNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = horas.getNoDia();
                if (findHoras(id) == null) {
                    throw new NonexistentEntityException("The horas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horas horas;
            try {
                horas = em.getReference(Horas.class, id);
                horas.getNoDia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horas with id " + id + " no longer exists.", enfe);
            }
           // Usuario id = horas.getId();
            if (id != null) {
           //     id.getHorasCollection().remove(horas);
                id = em.merge(id);
            }
            em.remove(horas);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Horas> findHorasEntities() {
        return findHorasEntities(true, -1, -1);
    }

    public List<Horas> findHorasEntities(int maxResults, int firstResult) {
        return findHorasEntities(false, maxResults, firstResult);
    }

    private List<Horas> findHorasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horas.class));
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

    public Horas findHoras(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horas.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horas> rt = cq.from(Horas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
