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
@Table(name = "biblioteca")
@NamedQueries({
    @NamedQuery(name = "Biblioteca.UsuarioFind", query = "SELECT u From Usuario u WHERE u.id =:id"),
   @NamedQuery(name = "Biblioteca.findById", query = "SELECT h FROM Biblioteca h, Usuario u WHERE h.id =:id"),
    @NamedQuery(name = "Biblioteca.findAll", query = "SELECT b FROM Biblioteca b"),
    @NamedQuery(name = "Biblioteca.findByEntrada", query = "SELECT b FROM Biblioteca b WHERE b.entrada = :entrada"),
    @NamedQuery(name = "Biblioteca.findBySalida", query = "SELECT b FROM Biblioteca b WHERE b.salida = :salida"),
    @NamedQuery(name = "Biblioteca.findByNoEntrada", query = "SELECT b FROM Biblioteca b WHERE b.noEntrada = :noEntrada")})
public class Biblioteca implements Serializable {
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
    @Column(name = "noEntrada")
    private Integer noEntrada;
    @JoinColumn(name = "id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario id;

    public Biblioteca() {
    }

    public Biblioteca(Integer noEntrada) {
        this.noEntrada = noEntrada;
    }

    public Biblioteca(Integer noEntrada, Date entrada, Date salida) {
        this.noEntrada = noEntrada;
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

    public Integer getNoEntrada() {
        return noEntrada;
    }

    public void setNoEntrada(Integer noEntrada) {
        this.noEntrada = noEntrada;
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
        hash += (noEntrada != null ? noEntrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Biblioteca)) {
            return false;
        }
        Biblioteca other = (Biblioteca) object;
        if ((this.noEntrada == null && other.noEntrada != null) || (this.noEntrada != null && !this.noEntrada.equals(other.noEntrada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Biblioteca[ noEntrada=" + noEntrada + " ]";
    }
    
}
