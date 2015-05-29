/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.ejb;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import edu.uco.rnolastname.termproject.jpa.Groups;
import edu.uco.rnolastname.termproject.utilities.SHA256Encryption;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClientFacade extends AbstractFacade<Client> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }
    
    public EntityManager getEntManager(){
        return em;
    }
    
    public boolean deleteClient(Client client){
        boolean success = false;
        try{
            /* Find and Remove Client, since we set cascade to ALL
                this implies that Client deletion will also delete the corresponding 
                ClientGroup record. However we still need to remove ClientGroup 
                from Group. 
            */
            Client c = em.find(Client.class,client.getId());            
            ClientGroup cg = c.getGroups().get(0);
            Groups g = em.find(Groups.class,cg.getGroup().getId());
            
            g.getClients().remove(cg);
            
            em.remove(c);  
            em.merge(g);
            em.flush();
            success = true;
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getSimpleName(), "createClient()", e.getMessage());                        
        }
        return success;
    }
    
    public Client updateClient(Client current, Groups selectedGroup){
        boolean success = false;
        
        try{           
            /* 
            Steps:
                    * Get ClientGroup from Client 
                    * Find exact match record in the selectedGroup list of Clients
                    * If it's not exists then we need to set a new relationship 
                        and assign client to the group
                    * Next we need to find the old Group from the client and remove 
                        ClientGroup record from its list of Clients
                    * Finally we can merge the Client, since cascade type is set to ALL
                        the relationship in ClientGroup table will also get updated
            */                        
            current.setModifiedDate(new Date());            
            ClientGroup cg = current.getGroups().get(0);
            Groups g = cg.getGroup();
            
            if(!selectedGroup.getClients().contains(cg)){
                g.getClients().remove(cg);
                current.getGroups().remove(cg);
                
                ClientGroup clientGroup = new ClientGroup();
                clientGroup.setModifiedDate(new Date());
                clientGroup.setClient(current);
                clientGroup.setClientId(current.getId());
                clientGroup.setGroup(selectedGroup);
                clientGroup.setGroupId(selectedGroup.getId());
                
                current.getGroups().add(clientGroup);
                selectedGroup.getClients().add(clientGroup);
                
            }                                                
            
            JsfUtil.printError(null, null, "Updating -> group before merge "
                    + ": " 
                    + current.getGroups().get(0).getGroup().getGroupType());                                    
            current = em.merge(current);            
            em.flush();
            JsfUtil.printError(null, null, "Updating -> group after merge and commit is : " 
                    + current.getGroups().get(0).getGroup().getGroupType());
//            success = true;
                        
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getSimpleName(), "updateClient()", e.getMessage());                        
        }
        return current;
    }        
    
    public ClientGroup createClient(Client client,Groups group){
        ClientGroup cg = new ClientGroup();
        try{
            client.setPassword(SHA256Encryption.encrypt(client.getPassword()));
            em.persist(client);
                        
            cg.setModifiedDate(new Date());
            cg.setClient(client);
            cg.setClientId(client.getId());        
            cg.setGroup(group);
            cg.setGroupId(group.getId());

            client.getGroups().add(cg);
            group.getClients().add(cg);
            
            em.flush();
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getSimpleName(), "createClient()", e.getMessage());            
            return null;
        }
        return cg;                                
    }
    
    public Client findClientByUsername(String username){
        Client client = null;        
        List<Client> clients = em.createNamedQuery("Client.findClientByUsername",Client.class)
                .setParameter("username", username).getResultList();
        
        if(!clients.isEmpty()){
            client = clients.get(0);
        }
        
        return client;
    }    
    
    public Client findClientByUUID(String key){
        Client c = null;
        List<Client> client = em.createNamedQuery("Client.findClientByUUID",Client.class)
                .setParameter("uuid", key).getResultList();
        
        if(!client.isEmpty()){
            c = client.get(0);
        }
        return c;
    }
}
