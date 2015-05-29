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
@DiscriminatorValue("Butter")
public class Butter extends Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Integer id = super.getId();
    
    private String fatType;
    private String variety; /* Sweet Cream, Cultured, Whey */

    public Butter(){
        super();
        super.setCategory("Butter");
    }
    
    public String getFatType() {
        return fatType;
    }

    public void setFatType(String fatType) {
        this.fatType = fatType;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
    
    @Override
    public void setUnique1(String val){
        setFatType(val);        
    }
    
    @Override
    public void setUnique2(String val){
        setVariety(val);
    }
    
    @Override
    public String getUnique1(){
        return getFatType();
    }
    
    @Override
    public String getUnique2(){
        return getVariety();
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
        if (!(object instanceof Butter)) {
            return false;
        }
        Butter other = (Butter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
