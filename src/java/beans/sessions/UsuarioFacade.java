/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Biblioteca;
import entidades.Usuario;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jsf.util.JsfUtil;

/**
 *
 * @author Roberto Avelar
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "xdPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
       public boolean usaurioExist(int id){     
           JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString(":id"+id));
          try {
         Query q = em.createNamedQuery("Usuario.findById",Usuario.class).setParameter("id", id);
        List<Usuario> list = q.getResultList();
        if (!list.isEmpty()) {
            return true;
        }
        return false;
          } catch (Exception e) {
              throw e;
          }
      }
    
}
