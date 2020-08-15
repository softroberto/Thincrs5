/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Admin;
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
public class AdminFacade extends AbstractFacade<Admin> {
    @PersistenceContext(unitName = "xdPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminFacade() {
        super(Admin.class);
    }
    
    // Metodos especializados con query
    
    public Admin autenticar(int id){
        
        Query q = em.createNamedQuery("Admin.findById",Admin.class).setParameter("id", id);
        List<Admin> list = q.getResultList();
        if (!list.isEmpty()) {
            int tamano = list.size();
            return list.get(0);
        }
        return null;
    }
    
}
