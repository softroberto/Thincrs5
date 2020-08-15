/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Horas;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Biblioteca;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Roberto Avelar
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getHorasCollection() == null) {
            usuario.setHorasCollection(new ArrayList<Horas>());
        }
        if (usuario.getBibliotecaCollection() == null) {
            usuario.setBibliotecaCollection(new ArrayList<Biblioteca>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Horas> attachedHorasCollection = new ArrayList<Horas>();
            for (Horas horasCollectionHorasToAttach : usuario.getHorasCollection()) {
                horasCollectionHorasToAttach = em.getReference(horasCollectionHorasToAttach.getClass(), horasCollectionHorasToAttach.getNoDia());
                attachedHorasCollection.add(horasCollectionHorasToAttach);
            }
            usuario.setHorasCollection(attachedHorasCollection);
            Collection<Biblioteca> attachedBibliotecaCollection = new ArrayList<Biblioteca>();
            for (Biblioteca bibliotecaCollectionBibliotecaToAttach : usuario.getBibliotecaCollection()) {
                bibliotecaCollectionBibliotecaToAttach = em.getReference(bibliotecaCollectionBibliotecaToAttach.getClass(), bibliotecaCollectionBibliotecaToAttach.getNoEntrada());
                attachedBibliotecaCollection.add(bibliotecaCollectionBibliotecaToAttach);
            }
            usuario.setBibliotecaCollection(attachedBibliotecaCollection);
            em.persist(usuario);
            for (Horas horasCollectionHoras : usuario.getHorasCollection()) {
                Usuario oldIdOfHorasCollectionHoras = horasCollectionHoras.getId();
                horasCollectionHoras.setId(usuario);
                horasCollectionHoras = em.merge(horasCollectionHoras);
                if (oldIdOfHorasCollectionHoras != null) {
                    oldIdOfHorasCollectionHoras.getHorasCollection().remove(horasCollectionHoras);
                    oldIdOfHorasCollectionHoras = em.merge(oldIdOfHorasCollectionHoras);
                }
            }
            for (Biblioteca bibliotecaCollectionBiblioteca : usuario.getBibliotecaCollection()) {
                Usuario oldIdOfBibliotecaCollectionBiblioteca = bibliotecaCollectionBiblioteca.getId();
                bibliotecaCollectionBiblioteca.setId(usuario);
                bibliotecaCollectionBiblioteca = em.merge(bibliotecaCollectionBiblioteca);
                if (oldIdOfBibliotecaCollectionBiblioteca != null) {
                    oldIdOfBibliotecaCollectionBiblioteca.getBibliotecaCollection().remove(bibliotecaCollectionBiblioteca);
                    oldIdOfBibliotecaCollectionBiblioteca = em.merge(oldIdOfBibliotecaCollectionBiblioteca);
                }
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Collection<Horas> horasCollectionOld = persistentUsuario.getHorasCollection();
            Collection<Horas> horasCollectionNew = usuario.getHorasCollection();
            Collection<Biblioteca> bibliotecaCollectionOld = persistentUsuario.getBibliotecaCollection();
            Collection<Biblioteca> bibliotecaCollectionNew = usuario.getBibliotecaCollection();
            List<String> illegalOrphanMessages = null;
            for (Horas horasCollectionOldHoras : horasCollectionOld) {
                if (!horasCollectionNew.contains(horasCollectionOldHoras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Horas " + horasCollectionOldHoras + " since its id field is not nullable.");
                }
            }
            for (Biblioteca bibliotecaCollectionOldBiblioteca : bibliotecaCollectionOld) {
                if (!bibliotecaCollectionNew.contains(bibliotecaCollectionOldBiblioteca)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Biblioteca " + bibliotecaCollectionOldBiblioteca + " since its id field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Horas> attachedHorasCollectionNew = new ArrayList<Horas>();
            for (Horas horasCollectionNewHorasToAttach : horasCollectionNew) {
                horasCollectionNewHorasToAttach = em.getReference(horasCollectionNewHorasToAttach.getClass(), horasCollectionNewHorasToAttach.getNoDia());
                attachedHorasCollectionNew.add(horasCollectionNewHorasToAttach);
            }
            horasCollectionNew = attachedHorasCollectionNew;
            usuario.setHorasCollection(horasCollectionNew);
            Collection<Biblioteca> attachedBibliotecaCollectionNew = new ArrayList<Biblioteca>();
            for (Biblioteca bibliotecaCollectionNewBibliotecaToAttach : bibliotecaCollectionNew) {
                bibliotecaCollectionNewBibliotecaToAttach = em.getReference(bibliotecaCollectionNewBibliotecaToAttach.getClass(), bibliotecaCollectionNewBibliotecaToAttach.getNoEntrada());
                attachedBibliotecaCollectionNew.add(bibliotecaCollectionNewBibliotecaToAttach);
            }
            bibliotecaCollectionNew = attachedBibliotecaCollectionNew;
            usuario.setBibliotecaCollection(bibliotecaCollectionNew);
            usuario = em.merge(usuario);
            for (Horas horasCollectionNewHoras : horasCollectionNew) {
                if (!horasCollectionOld.contains(horasCollectionNewHoras)) {
                    Usuario oldIdOfHorasCollectionNewHoras = horasCollectionNewHoras.getId();
                    horasCollectionNewHoras.setId(usuario);
                    horasCollectionNewHoras = em.merge(horasCollectionNewHoras);
                    if (oldIdOfHorasCollectionNewHoras != null && !oldIdOfHorasCollectionNewHoras.equals(usuario)) {
                        oldIdOfHorasCollectionNewHoras.getHorasCollection().remove(horasCollectionNewHoras);
                        oldIdOfHorasCollectionNewHoras = em.merge(oldIdOfHorasCollectionNewHoras);
                    }
                }
            }
            for (Biblioteca bibliotecaCollectionNewBiblioteca : bibliotecaCollectionNew) {
                if (!bibliotecaCollectionOld.contains(bibliotecaCollectionNewBiblioteca)) {
                    Usuario oldIdOfBibliotecaCollectionNewBiblioteca = bibliotecaCollectionNewBiblioteca.getId();
                    bibliotecaCollectionNewBiblioteca.setId(usuario);
                    bibliotecaCollectionNewBiblioteca = em.merge(bibliotecaCollectionNewBiblioteca);
                    if (oldIdOfBibliotecaCollectionNewBiblioteca != null && !oldIdOfBibliotecaCollectionNewBiblioteca.equals(usuario)) {
                        oldIdOfBibliotecaCollectionNewBiblioteca.getBibliotecaCollection().remove(bibliotecaCollectionNewBiblioteca);
                        oldIdOfBibliotecaCollectionNewBiblioteca = em.merge(oldIdOfBibliotecaCollectionNewBiblioteca);
                    }
                }
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
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Horas> horasCollectionOrphanCheck = usuario.getHorasCollection();
            for (Horas horasCollectionOrphanCheckHoras : horasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Horas " + horasCollectionOrphanCheckHoras + " in its horasCollection field has a non-nullable id field.");
            }
            Collection<Biblioteca> bibliotecaCollectionOrphanCheck = usuario.getBibliotecaCollection();
            for (Biblioteca bibliotecaCollectionOrphanCheckBiblioteca : bibliotecaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Biblioteca " + bibliotecaCollectionOrphanCheckBiblioteca + " in its bibliotecaCollection field has a non-nullable id field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
