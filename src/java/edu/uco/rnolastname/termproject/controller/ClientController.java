package edu.uco.rnolastname.termproject.controller;

import edu.uco.rnolastname.jpautil.ClientService;
import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.jpautil.PaginationHelper;
import edu.uco.rnolastname.termproject.bean.ClientBean;

import edu.uco.rnolastname.termproject.ejb.ClientFacade;
import edu.uco.rnolastname.termproject.ejb.ClientGroupFacade;
import edu.uco.rnolastname.termproject.ejb.GroupsFacade;
import edu.uco.rnolastname.termproject.jpa.Bread;
import edu.uco.rnolastname.termproject.jpa.Butter;

import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import edu.uco.rnolastname.termproject.jpa.Groups;
import edu.uco.rnolastname.termproject.jpa.Milk;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named("clientController")
@SessionScoped
public class ClientController implements Serializable{  
    private boolean login = false;
    
    @Resource(name="mail/mailSession")
    private Session mailSession;

    @Inject
    private ItemController itemController;
    
    @Inject 
    private ShoppingCartController shoppingCartController;
    
    private static int itemPerPage = 5;
    
    /* Bean Object that stores Client's Information as long as the session valid 
        or during a client registration
    */    
    @Inject        
    private ClientBean clientBean;
        
    /* Represents List of Clients, all variables related to Client CRUD */
    private DataModel items;       
    private Groups selectedGroup;    
    private Client current;
    private Client activeClient;
    private PaginationHelper pagination;
    private int selectedItemIndex;
        
    /* EJB variables and Helper for Clients */    
    @EJB
    private ClientGroupFacade clientGroupFacade;
    @EJB
    private ClientFacade ejbFacade;
    @EJB
    private GroupsFacade groupFacade;
    
    private ClientService clientService = null;          
    
    public ClientController(){}
    
    public ClientBean getClientBean() {
        return clientBean;
    }

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }            
        
    
    /* Check as Client entering username, if given username is already exists */
    public void inputUsernameListener(){       
        JsfUtil.printError(JsfUtil.getRequestPath());
        switch(JsfUtil.getRequestPath()){
            case "/views/protected/admin/assets/client/Create.xhtml":
                if(current != null && !current.getUsername().equals("")){            
                    String username = current.getUsername();            
                    if(getFacade().findClientByUsername(username) != null){                
                        FacesContext.getCurrentInstance().addMessage("signupform:username",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username is already exists",null));
                        current.setUsername("");
                    }
                }
                break;
            case "/views/authentication/registration.xhtml":
                if(clientBean!= null && !clientBean.getUsername().equals("")){            
                    String username = clientBean.getUsername();            
                    if(getFacade().findClientByUsername(username) != null){                
                        FacesContext.getCurrentInstance().addMessage("signupform:username",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username is already exists",null));
                        clientBean.setUsername("");
                    }
                }
                break;
            default :
                if(clientBean!= null && !clientBean.getUsername().equals("")){            
                    String username = clientBean.getUsername();            
                    if(getFacade().findClientByUsername(username) != null){                
                        FacesContext.getCurrentInstance().addMessage("signupform:username",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username is already exists",null));
                        clientBean.setUsername("");
                    }
                }
                break;
        }                                
    }        
    
    /* Takes care of Client related request 
        and/or Interaction with UI, as well as navigation */
    public void eventClientController(){        
        String path = FacesContext.getCurrentInstance().getViewRoot().getViewId();                
        
        switch(path){
            case "/views/authentication/registration.xhtml":
                
                if(clientService == null){
                    clientService = new ClientService(getFacade(),groupFacade,clientGroupFacade);    

                }            

                if(clientBean == null){
                    clientBean = new ClientBean();
                }
                
                break;
            case "/views/protected/client/update_profile.xhtml":
                if(clientService == null){
                    clientService = new ClientService(getFacade(),groupFacade,clientGroupFacade);    

                }
                if(activeClient != null){
                    clientBean = clientToClientBean(activeClient);
                    JsfUtil.printError("Active Client USERNAME: " + activeClient.getUsername());
                    JsfUtil.printError("Client Bean USERNAME: " + clientBean.getUsername());
                }
                break;
            case "/views/authentication/validation.xhtml":                                
                String uuid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
                if(uuid == null){
                    JsfUtil.addErrorMessage("Activation Failed, Link is expired<br/>");
                }
                JsfUtil.printError("", "", "Key: " + uuid);
                Client c = getFacade().findClientByUUID(uuid);
                if(c == null){
                    JsfUtil.addErrorMessage("Activation Failed! Please make sure you put the correct email address");
                }else{
                    c.setUuid(null);
                    JsfUtil.addSuccessMessage("Your Account Has been Activated, please proceed to Login");
                }
                                
                getFacade().edit(c);
                
                break;
            case "/views/protected/redirect.xhtml":
                redirect(FacesContext.getCurrentInstance().getExternalContext());           
                break;
                
            case "/views/pages/products.xhtml":                
                String category = FacesContext.getCurrentInstance()
                        .getExternalContext().getRequestParameterMap().get("category");
                    if(category == null || category.equals("")){
                        return;
                    }
                    switch(category){
                        case "Bread" : 
                            itemController.setSelected(new Bread());
                            break;
                        case "Butter" :
                            itemController.setSelected(new Butter());
                            break;
                        case "Milk" : 
                            itemController.setSelected(new Milk());
                            break;
                        default :
                            itemController.setSelected(null);
                            break;
                    }
                break;            
            case "/views/protected/client/client.xhtml":
                if(activeClient == null){
                    setActiveClient(getFacade().findClientByUsername(FacesContext.
                        getCurrentInstance().getExternalContext().getRemoteUser())); 
                }  
                if(activeClient != null && activeClient.getUuid() != null){
                    doLogout("Please activate your account by clicking the link in your email");
                }                
                break;
            case "/views/protected/admin/admin.xhtml":  
                if(activeClient == null){
                    setActiveClient(getFacade().findClientByUsername(FacesContext.
                        getCurrentInstance().getExternalContext().getRemoteUser())); 
                }
                break;                        
        }                        
    }        
    
    
    /* RESERVED FOR SPACE */
    
    
    
    /* CLIENT CRUD OPERATIONS */    
    private ClientFacade getFacade(){
        return ejbFacade;
    }
    
    public Client getCurrent(){
        return current;
    }
    
    public Client getClient(int id){
        return getFacade().find(id);
    }

    public Client getActiveClient() {
        return activeClient;
    }

    public void setActiveClient(Client updateClient) {
        this.activeClient = updateClient;
    }
            
    public Client getSelected(){
        if(current == null){
            current = new Client();
            selectedItemIndex = -1;
        }
        return current;
    }        
    
    public PaginationHelper getPagination(){
        if(pagination == null){
            pagination = new PaginationHelper(5){
                
                @Override
                public int getItemsCount(){
                    return getFacade().count();
                }
                
                @Override 
                public DataModel createPageDataModel(){
                    ListDataModel model = new ListDataModel(getFacade()
                            .findRange(new int[]{getPageFirstItem(),getPageFirstItem() 
                            + getPageSize() - 1}));                                         
                    return model;
                }
            };
        }
        return pagination;
    }
    
    public DataModel getItems() {        
        if(items == null){
            items = getPagination().createPageDataModel();
        }
        return items;
    }   
    
    public String prepareList(){
        recreateModel();              
        return "List";
    }
    
    public String prepareView(){
        current = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        setSelectedGroup(null);
        return "View";
    }
    
    public String prepareCreate(){
        current = new Client();   
        setSelectedGroup(null);
        current.setModifiedDate(new Date());
        selectedItemIndex = -1;
        return "Create";
    }
            
    public String create(){
        try{
            if(selectedGroup == null){
                JsfUtil.addErrorMessage("Please Select Group");
                return null;
            }                   
            ClientGroup cg = getFacade().createClient(current, selectedGroup);
            if(cg == null){
                JsfUtil.addErrorMessage("Client insertion failed");                
            }

            JsfUtil.addSuccessMessage("Client has been successfuly created");
            return prepareCreate();
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(),"create()",e.getLocalizedMessage());
            JsfUtil.addErrorMessage("Failed to create Client");
            return null;
        }
    }
    
    public String prepareEdit(){
        current =  (Client)getItems().getRowData();                                    
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
               
        /* Get selected Group */
        Groups g = current.getGroups().get(0).getGroup();                      
        
        this.setSelectedGroup(g);       
        return "Edit";
    }
    
    public String update(){
        try{                     
            if(selectedGroup == null){
                JsfUtil.addErrorMessage("Please Select Group");
                return null;
            }                                                                                    
            
            current = getFacade().updateClient(current,selectedGroup);                                    
            
            JsfUtil.addSuccessMessage("Client has been updated");
            return "View";                       
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(),"edit()",e.getMessage());
            JsfUtil.addErrorMessage("Failed Updating client");                         
            return null;
        }
    }
    
    public String destroy(){
        current = (Client) getItems().getRowData();    
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();  
     
        performDestroy();
        recreatePagination();
        recreateModel();         
        
        return "List";
    }
    
    public void performDestroy(){
        try{
            if(getFacade().deleteClient(current)){
                JsfUtil.addSuccessMessage("Client has been deleted");
            }                       
            
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(), "performDestroy()", e.getMessage());
        }        
    }
    
    public String destroyAndView(){
        current = (Client) getItems().getRowData();
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        }else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }        
    }
    
    private void updateCurrentItem() {
        /* Get the Count of the item in the Database */
        int count = getFacade().count();
        /* Check if the item that we selected is at least equal to 
            count. If it is larger or equal to that means we pass the 
            number of records, and that's imporssible.
        */
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    private void recreateModel(){
        items = null;
    }
    
    private void recreatePagination(){
        pagination = null;
    }
    
    public String next(){
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous(){
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public SelectItem[] getItemsAvailableSelectMany(){
        return JsfUtil.getSelectItems(getFacade().findAll(), false);
    }
    
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }        
    
    public Groups getGroup(int id){
        return groupFacade.find(id);
    }
    
    public void setSelectedGroup(Groups selectedGroup){
        this.selectedGroup = selectedGroup;                                        
    }
    
    public Groups getSelectedGroup(){        
        return selectedGroup;        
    }
    
    public String getGroupType(int id){                
        return groupFacade.findGroupTypeById(id);
    }
        
    public List<Groups> getAllGroups(){
        return groupFacade.findAll();
    }        
    
    /* RESERVED FOR SPACE */
    
    
    /* NAVIGATION */ 
    
    public String redirect(String path){        
        return path;
    }
    
    private void redirect(ExternalContext context){        
        String navTo = null;
        HttpServletRequest request = (HttpServletRequest) context.getRequest();        
        
        if(request.isUserInRole("admin")){
            navTo = "admin/admin.xhtml";              
        }else if(request.isUserInRole("client")){
            navTo = "client/client.xhtml";            
        }                    
        if(navTo != null && !navTo.equals("")){
            try{                
                context.redirect(navTo);  
                setLogin(true);
                setActiveClient(getFacade().findClientByUsername(FacesContext.
                        getCurrentInstance().getExternalContext().getRemoteUser())); 
            }catch(IOException e){
                System.out.println("REY LOG: ClientController redirect, "
                        + "Error: " + e.getMessage());
            }            
        }                
    }
    
    public Date getDateTime(){
        return new Date();
    }
    
    public void doLogout(String message){             
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);                
        
        if(session != null && isLogin()){
            if(message != null && message.equals("")){
                message = "Logged out success!<br /><br />"
                        + "See you " + activeClient.getFirstName() + " and come visit us again";
            }
            session.invalidate();
            setLogin(false); 
            setActiveClient(null);            
            
            try{
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/termproject/faces/views/authentication/logout.xhtml?message=" + message);
            }catch(IOException e){
                JsfUtil.printError(this.getClass().getSimpleName(), 
                        "doLogout()", "redirection to logout page error: " + e.getMessage());
            }            
        }else{            
            try{
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/termproject/faces/views/protected/redirect.xhtml");
            }catch(IOException e){
                JsfUtil.printError(this.getClass().getSimpleName(), 
                        "doLogout()", "redirection to redirect page error: " + e.getMessage());
            }
        }
        
    }
    
    public String submitRegularClient(){
        
        if(clientBean != null){                                                                      
            
            String uuid;
            /* Send Confirmation Email */                        
            if((uuid = clientService.sendEmailRegistrationSuccess(mailSession,clientBean.getEmail(), clientBean.getFirstName())) == null){
                FacesContext.getCurrentInstance().addMessage("signupform:submitClient", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Sign Up Failed, "
                            + "make sure you provide a valid Email Address",null));                                   
                return null;
            }else{
                /* Insert new Regular Client */
                Client c;                                                                
                c = clientService.addClient(clientBean,uuid);                                   
                               
                if(c != null){
                    FacesContext.getCurrentInstance().addMessage("signupform:submitClient", 
                        new FacesMessage(FacesMessage.SEVERITY_INFO,"Sign Up Successful, "
                                + "an Email has been sent to you to confirm the registration",null));                                   
                }
            }

        }
        return null;
    }                             
    
    public String updateRegularClient(){
              
        if(activeClient != null && clientBean != null){
            if(!clientBean.getEmail().equals(activeClient.getEmail())){
                String uuid = clientService.sendEmailRegistrationSuccess(mailSession,clientBean.getEmail(), clientBean.getFirstName());                
                activeClient.setModifiedDate(clientBean.getModifiedDate());
                activeClient.setFirstName(clientBean.getFirstName());
                activeClient.setLastName(clientBean.getLastName());
                activeClient.setPhone(clientBean.getPhone());
                activeClient.setEmail(clientBean.getEmail());
                activeClient.setStreet(clientBean.getStreet());
                activeClient.setCity(clientBean.getCity());
                activeClient.setState(clientBean.getState());
                activeClient.setUuid(uuid);
                                
                getFacade().edit(activeClient);
                JsfUtil.addSuccessMessage("Your profile has been updated and "
                        + "a new activation link has been sent to your email address");            
            }else{
                activeClient.setModifiedDate(clientBean.getModifiedDate());
                activeClient.setFirstName(clientBean.getFirstName());
                activeClient.setLastName(clientBean.getLastName());
                activeClient.setPhone(clientBean.getPhone());
                activeClient.setEmail(clientBean.getEmail());
                activeClient.setStreet(clientBean.getStreet());
                activeClient.setCity(clientBean.getCity());
                activeClient.setState(clientBean.getState());
                
                getFacade().edit(activeClient);
                JsfUtil.addSuccessMessage("Your profile has been updated");            
            }                        
        }
        return "update_profile";
    }
    
    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }        

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        ClientController.itemPerPage = itemPerPage;
    }
        
    public boolean validate(String key){
        boolean success = false;
        Client c = getFacade().findClientByUUID(key);
        
        if(c != null && c.getUuid().equals(key)){
            c.setUuid("");
            success = true;            
        }
                
        return success;
    }               
    
    public ClientBean clientToClientBean(Client c){
        ClientBean bean = new ClientBean();
        bean.setId(c.getId());
        bean.setModifiedDate(c.getModifiedDate());
        bean.setUsername(c.getUsername());
        bean.setPassword(c.getPassword());
        bean.setFirstName(c.getFirstName());
        bean.setLastName(c.getLastName());
        bean.setPhone(c.getPhone());
        bean.setEmail(c.getEmail());
        bean.setStreet(c.getStreet());
        bean.setCity(c.getCity());
        bean.setState(c.getState());
        return bean;
    }
        
}

