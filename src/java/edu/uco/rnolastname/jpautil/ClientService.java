package edu.uco.rnolastname.jpautil;

import edu.uco.rnolastname.termproject.bean.ClientBean;
import edu.uco.rnolastname.termproject.ejb.ClientFacade;
import edu.uco.rnolastname.termproject.ejb.ClientGroupFacade;
import edu.uco.rnolastname.termproject.ejb.GroupsFacade;
import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import edu.uco.rnolastname.termproject.jpa.Groups;
import edu.uco.rnolastname.termproject.utilities.SHA256Encryption;
import edu.uco.rnolastname.termproject.utilities.MailUtilities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;

@ApplicationScoped
public class ClientService implements Serializable {
    private EntityManager clientEM;
    private EntityManager clientGroupEM;
    private EntityManager groupEM;
         
    private ClientFacade clientFacade;        
    private GroupsFacade groupFacade;        
    private ClientGroupFacade clientGroupFacade;
    
    public ClientService(ClientFacade clientFacade){
        this.clientFacade = clientFacade;
    }
    
    public ClientService(ClientFacade clientFacade,GroupsFacade groupFacade,ClientGroupFacade clientGroupFacade){
        this.clientFacade = clientFacade;
        this.clientGroupFacade = clientGroupFacade;
        this.groupFacade = groupFacade;
        
        clientEM = clientFacade.getEntManager();
        clientGroupEM = clientGroupFacade.getEntManager();
        groupEM = groupFacade.getEntManager();
    }
    
    public List<Client> findAllClient(){
        List<Client> clients = clientFacade.findAll();
        
        return clients;
    }                
    
    public Client addAdmin(ClientBean clientBean){
        /* In Order to be an ADMIN a client 
            has to be created as regular CLIENT first 
        */
        Client c = new Client();
        c.setModifiedDate(new Date());
        c.setPassword(SHA256Encryption.encrypt(clientBean.getPassword()));
        c.setUsername(clientBean.getUsername());
        c.setFirstName(clientBean.getFirstName());
        c.setLastName(clientBean.getLastName());
        c.setPhone(clientBean.getPhone());
        c.setEmail(clientBean.getEmail());
        c.setStreet(clientBean.getStreet());
        c.setCity(clientBean.getCity());
        c.setState(clientBean.getState());
        
        
        /* Find ADMIN Group Type */
        Groups g;
        if((g = findGroupByType("ADMIN")) == null){
            g = addGroup("ADMIN","Client Administrator Account");
            groupFacade.create(g);
        }        

        clientFacade.create(c);
        
        ClientGroup cg = setClientGroup(c,g);
        clientGroupFacade.create(cg);                
                
        return c;        
    }
    
    public Client addClient(ClientBean clientBean, String uuid){
        Client c = new Client();
        c.setModifiedDate(new Date());
        c.setPassword(SHA256Encryption.encrypt(clientBean.getPassword()));
        c.setUsername(clientBean.getUsername());
        c.setFirstName(clientBean.getFirstName());
        c.setLastName(clientBean.getLastName());
        c.setPhone(clientBean.getPhone());
        c.setEmail(clientBean.getEmail());
        c.setStreet(clientBean.getStreet());
        c.setCity(clientBean.getCity());
        c.setState(clientBean.getState());
        c.setUuid(uuid);
        
        /* Find CLIENT Group Type */
        Groups g;
        if((g = findGroupByType("CLIENT")) == null){
            g = addGroup("CLIENT","Regular Client Account");
            groupFacade.create(g);
        }
        
        clientFacade.create(c);
        
        ClientGroup cg = setClientGroup(c,g);               
        clientGroupFacade.create(cg);                
        
        return c;
    }
    
    public Client findClientByUsername(String username){
        Client client = null;        
        List<Client> clients = clientEM.createNamedQuery("Client.findClientByUsername",Client.class)
                .setParameter("username", username).getResultList();
        
        if(!clients.isEmpty()){
            client = clients.get(0);
        }
        
        return client;
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
    
    public List<Groups> findAllGroup(){
        List<Groups> groups = groupFacade.findAll();
        
        return groups;
    }
    
    public Groups addGroup(String groupType,String groupDescription){
        Groups g = new Groups();
        g.setModifiedDate(new Date());
        g.setGroupType(groupType);
        g.setGroupDescription(groupDescription);        
        
        return g;
    }
    
    public Groups findGroupByType(String groupType){
        Groups g = null;
        
        List<Groups> groups = groupEM.createNamedQuery("Groups.findGroupByGroupType",Groups.class)
                .setParameter("groupType", groupType).getResultList();
        
        if(!groups.isEmpty()){
            g = groups.get(0);
        }
        
        return g;
    }
    
    public String sendEmailUpdateEmailAddress(Session mailSession, String to, String firstName){
        String uuid;
        uuid = MailUtilities.generateUUID();
        String confirmationLink = "http://localhost:8080/termproject/faces/views/authentication/validation.xhtml?key=" + uuid;
        String text = "Dear " + firstName + ",\n" 
                + "\nThis Email Confirmation is to verified that you "
                + " have changed your email address.\nPlease verify"
                + " your account by clicking the new verification link below!\n\n"                
                + confirmationLink;
        try{
            MailUtilities.sendEmail(mailSession,to,"Barnes and Toddler: Registration Success",firstName,text);            
        }catch(AddressException e){
            System.out.println("REY LOG: Address Exception from ClientService SendEmailRegistrationSuccess"
                    + ", Error: " + e.getMessage());            
        }catch(MessagingException e){
            System.out.println("REY LOG: MessagingException from ClientService SendEmailRegistrationSuccess"
                    + " Error: " + e.getMessage());        
        }    
                
        return uuid;
    }
    
    public String sendEmailRegistrationSuccess(Session mailSession, String to, String firstName){        
        String uuid;
        uuid = MailUtilities.generateUUID();
        String confirmationLink = "http://localhost:8080/termproject/faces/views/authentication/validation.xhtml?key=" + uuid;
        String text = "Dear " + firstName + ",\n" 
                + "\nThis Email Confirmation is to verified that you "
                + "are part of Barnes and Toddlers family now.\nWelcome and don't forget"
                + " to order the three items we have available!\n\n"
                + "Now before you enjoying all the goodies, please verify your registration by click on the link below\n\n"
                + confirmationLink;
        try{
            MailUtilities.sendEmail(mailSession,to,"Barnes and Toddler: Registration Success",firstName,text);            
        }catch(AddressException e){
            System.out.println("REY LOG: Address Exception from ClientService SendEmailRegistrationSuccess"
                    + ", Error: " + e.getMessage());            
        }catch(MessagingException e){
            System.out.println("REY LOG: MessagingException from ClientService SendEmailRegistrationSuccess"
                    + " Error: " + e.getMessage());        
        }    
                
        return uuid;
    }
}
