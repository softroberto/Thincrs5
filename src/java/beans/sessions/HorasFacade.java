/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Horas;
import entidades.Usuario;
import java.time.LocalDateTime;
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
public class HorasFacade extends AbstractFacade<Horas> {
    @PersistenceContext(unitName = "xdPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HorasFacade() {
        super(Horas.class);
    }
    
      public Horas autenticar(Usuario id){    
          
        Query q = em.createNamedQuery("Horas.findById",Horas.class).setParameter("id", id);
        List<Horas> list = q.getResultList();
        int tamano = list.size();
        if (!list.isEmpty()) {
            return list.get(tamano-1-1);
        }
        return null;     
    }
  public boolean usaurioExist(int id) {
       
            Query q = em.createNamedQuery("Horas.UsuarioFind", Horas.class).setParameter("id", id);
            List<Horas> list = q.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
            return false;
    
    }
  
}
