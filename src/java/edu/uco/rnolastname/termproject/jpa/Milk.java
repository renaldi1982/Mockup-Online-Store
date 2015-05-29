/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Milk")
public class Milk extends Item implements Serializable {
    private static final long serialVersionUID = 1L;    
    
    private final Integer id = super.getId();
    private String taste;    
    private String type;
    
    public Milk(){
        super();
        super.setCategory("Milk");
    }
    
    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }    
    
    @Override
    public void setUnique1(String val){
        setTaste(val);
    }
    
    @Override
    public void setUnique2(String val){
        setType(val);
    }
    
    @Override
    public String getUnique1(){
        return getTaste();
    }
    
    @Override
    public String getUnique2(){
        return getType();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Milk)) {
            return false;
        }
        Milk other = (Milk) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }        
    
}
