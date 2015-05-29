/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.jpautil.ClientGroupAssociationId;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@IdClass(ClientGroupAssociationId.class)
@NamedQueries({
    @NamedQuery(name="ClientGroup.findByClientId",query="SELECT cg FROM ClientGroup cg WHERE cg.clientId = :clientId")
//    @NamedQuery(name="ClientGroup.findClientGroup",query="SELECT c, cg.group.groupType FROM ClientGroup cg ")
})
public class ClientGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id   
    private int clientId;
    @Id
    private int groupId;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)    
    private Date modifiedDate;
    
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "clientId", updatable = false, insertable = false, referencedColumnName = "id")
    private Client client;
        
    @ManyToOne
    @JoinColumn(name = "groupId", updatable = false, insertable = false, referencedColumnName = "id")
    private Groups group;

    public ClientGroup(){}
    
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
               
    
}
