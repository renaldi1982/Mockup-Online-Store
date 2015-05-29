package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Bread")
public class Bread extends Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Integer id = super.getId();
    
    private double netContent;
    private String texture;
    
    public Bread(){
        super();        
        super.setCategory("Bread");
    }
    
    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public double getNetContent() {
        return netContent;
    }

    public void setNetContent(double netContent) {
        this.netContent = netContent;
    }
    
    @Override
    public void setUnique1(String val){
        setNetContent(Double.valueOf(val));        
    }
    
    @Override
    public void setUnique2(String val){
        setTexture(val);
    }
    
    @Override
    public String getUnique1(){
        return String.valueOf(getNetContent());
    }
    
    @Override
    public String getUnique2(){
        return getTexture();
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
        if (!(object instanceof Bread)) {
            return false;
        }
        Bread other = (Bread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }       
}
