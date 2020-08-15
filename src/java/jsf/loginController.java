/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import entidades.Admin;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Roberto Avelar
 */

@Named
@ViewScoped
public class loginController implements Serializable{
   
    public void verificar(){
        try {
         Admin us =   (Admin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin");
            if(us == null){
                FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/admin/principal");
            }
        } catch (Exception e) {
        }
    }
}
