/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Roberto Avelar
 */
@Entity
@Table(name = "horas")
@NamedQueries({
    @NamedQuery(name = "Horas.UsuarioFind", query = "SELECT u From Usuario u WHERE u.id =:id"),  
   @NamedQuery(name = "Horas.findById", query = "SELECT h FROM Horas h, Usuario u WHERE h.id =:id"),  
    @NamedQuery(name = "Horas.findAll", query = "SELECT h FROM Horas h"),
    @NamedQuery(name = "Horas.findByEntrada", query = "SELECT h FROM Horas h WHERE h.entrada = :entrada"),
    @NamedQuery(name = "Horas.findBySalida", query = "SELECT h FROM Horas h WHERE h.salida = :salida"),
    @NamedQuery(name = "Horas.findByNoDia", query = "SELECT h FROM Horas h WHERE h.noDia = :noDia")})
public class Horas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrada;
    @Basic(optional = false)
    @Column(name = "salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date salida;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "noDia")
    private Integer noDia;
    @JoinColumn(name = "id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario id;

    public Horas() {
    }

    public Horas(Integer noDia) {
        this.noDia = noDia;
    }

    public Horas(Integer noDia, Date entrada, Date salida) {
        this.noDia = noDia;
        this.entrada = entrada;
        this.salida = salida;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public Integer getNoDia() {
        return noDia;
    }

    public void setNoDia(Integer noDia) {
        this.noDia = noDia;
    }

    public Usuario getId() {
        return id;
    }

    public void setId(Usuario id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noDia != null ? noDia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horas)) {
            return false;
        }
        Horas other = (Horas) object;
        if ((this.noDia == null && other.noDia != null) || (this.noDia != null && !this.noDia.equals(other.noDia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Horas[ noDia=" + noDia + " ]";
    }
    
}
