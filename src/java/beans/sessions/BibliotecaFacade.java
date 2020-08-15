/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Biblioteca;
import entidades.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Roberto Avelar
 */
@Stateless
public class BibliotecaFacade extends AbstractFacade<Biblioteca> {

    @PersistenceContext(unitName = "xdPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BibliotecaFacade() {
        super(Biblioteca.class);
    }

     public Biblioteca autenticar(Usuario id){    
          
        Query q = em.createNamedQuery("Biblioteca.findById",Biblioteca.class).setParameter("id", id);
        List<Biblioteca> list = q.getResultList();
        int tamano = list.size();
        if (!list.isEmpty()) {
            return list.get(tamano-1);
        }
        return null;     
    }

    public boolean usaurioExist(int id) {
       
            Query q = em.createNamedQuery("Biblioteca.UsuarioFind", Biblioteca.class).setParameter("id", id);
            List<Biblioteca> list = q.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
            return false;
    
    }

}
