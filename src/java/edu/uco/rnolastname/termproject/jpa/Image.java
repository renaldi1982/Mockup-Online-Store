/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Image.findImageByOwnerID",query="SELECT i FROM Image i WHERE i.owner = :ownerid"),
    @NamedQuery(name="Image.findById",query="SELECT i FROM Image i WHERE i.id = :id"),
    @NamedQuery(name="Image.getContentById",query="SELECT i.content FROM Image i WHERE i.id = :id")
})
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableGenerator(name = "image_gen", table = "sequence", pkColumnName = "seq_name", valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "image_gen")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
        
    @ManyToOne
    @JoinColumn(name="ownerId",referencedColumnName="id")
    private Item owner;    
    
    private String name;
    private String content_type;
    
    @Lob    
    @Basic(fetch=LAZY)
    private byte[] content;
            
    public Image(){
        modifiedDate = new Date();
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Item getOwner() {
        return owner;
    }

    public void setOwner(Item owner) {
        this.owner = owner;
        if(!owner.getImages().contains(this)){
            owner.getImages().add(this);
        }
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
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
        if (!(object instanceof Image)) {
            return false;
        }
        Image other = (Image) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uco.rnolastname.termproject.jpa.Image[ id=" + id + " ]";
    }
    
}
