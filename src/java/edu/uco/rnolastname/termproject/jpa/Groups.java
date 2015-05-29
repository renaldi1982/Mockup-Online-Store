package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(name="Groups.findGroupByGroupType",query="SELECT g FROM Groups g WHERE g.groupType = :groupType"),
        @NamedQuery(name="Groups.findGroupTypeById",query="SELECT g.groupType FROM Groups g WHERE g.id = :id")
})
public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableGenerator(name = "groups_gen", table = "SEQUENCE", pkColumnName = "seq_name", 
            valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "groups_gen")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP) 
    private Date modifiedDate;    
    
    @Column(name = "GROUP_TYPE",nullable = false, unique = true)
    private String groupType;
    
    @Column(name = "GROUP_DESCRIPTION")
    private String groupDescription;    

    /* Many to Many relationship */    
    @OneToMany(mappedBy = "group")
    private List<ClientGroup> clients = new ArrayList<>();                    
    
    public void setClients(List<ClientGroup> clients){
        this.clients = clients;
    }
    
    public List<ClientGroup> getClients(){
        return clients;
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
    
    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
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
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uco.rnolastname.termproject.model.Groups[ id=" + id + " ]";
    }
    
}
