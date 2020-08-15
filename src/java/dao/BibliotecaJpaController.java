/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Biblioteca;
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
public class BibliotecaJpaController implements Serializable {

    public BibliotecaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Biblioteca biblioteca) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario id = biblioteca.getId();
            if (id != null) {
                id = em.getReference(id.getClass(), id.getId());
                biblioteca.setId(id);
            }
            em.persist(biblioteca);
            if (id != null) {
                id.getBibliotecaCollection().add(biblioteca);
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

    public void edit(Biblioteca biblioteca) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Biblioteca persistentBiblioteca = em.find(Biblioteca.class, biblioteca.getNoEntrada());
            Usuario idOld = persistentBiblioteca.getId();
            Usuario idNew = biblioteca.getId();
            if (idNew != null) {
                idNew = em.getReference(idNew.getClass(), idNew.getId());
                biblioteca.setId(idNew);
            }
            biblioteca = em.merge(biblioteca);
            if (idOld != null && !idOld.equals(idNew)) {
                idOld.getBibliotecaCollection().remove(biblioteca);
                idOld = em.merge(idOld);
            }
            if (idNew != null && !idNew.equals(idOld)) {
                idNew.getBibliotecaCollection().add(biblioteca);
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
                Integer id = biblioteca.getNoEntrada();
                if (findBiblioteca(id) == null) {
                    throw new NonexistentEntityException("The biblioteca with id " + id + " no longer exists.");
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
            Biblioteca biblioteca;
            try {
                biblioteca = em.getReference(Biblioteca.class, id);
                biblioteca.getNoEntrada();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The biblioteca with id " + id + " no longer exists.", enfe);
            }
            //Usuario id = biblioteca.getId();
            if (id != null) {
             //   id.getBibliotecaCollection().remove(biblioteca);
                id = em.merge(id);
            }
            em.remove(biblioteca);
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

    public List<Biblioteca> findBibliotecaEntities() {
        return findBibliotecaEntities(true, -1, -1);
    }

    public List<Biblioteca> findBibliotecaEntities(int maxResults, int firstResult) {
        return findBibliotecaEntities(false, maxResults, firstResult);
    }

    private List<Biblioteca> findBibliotecaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Biblioteca.class));
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

    public Biblioteca findBiblioteca(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Biblioteca.class, id);
        } finally {
            em.close();
        }
    }

    public int getBibliotecaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Biblioteca> rt = cq.from(Biblioteca.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
