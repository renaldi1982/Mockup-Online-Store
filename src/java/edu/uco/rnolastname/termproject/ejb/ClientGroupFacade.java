/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.ejb;

import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import edu.uco.rnolastname.termproject.jpa.Groups;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClientGroupFacade extends AbstractFacade<ClientGroup> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientGroupFacade() {
        super(ClientGroup.class);
    }
    
    public EntityManager getEntManager(){
        return em;
    }
        
    public ClientGroup setClientGroup(Client client, Groups group){
        ClientGroup association = new ClientGroup();
        association.setModifiedDate(new Date());
        association.setClient(client);
        association.setGroup(group);        
        association.setClientId(client.getId());
        association.setGroupId(group.getId());        
        
        client.getGroups().add(association);
        group.getClients().add(association);
        
        return association;
    }
        
    public ClientGroup findByClientId(int id){
        List<ClientGroup> cg = em.createNamedQuery("ClientGroup.findByClientId",ClientGroup.class)
                .setParameter("clientId", id).getResultList();
        
        if(!cg.isEmpty()){
            return cg.get(0);
        }
        return null;
    }
    
}
