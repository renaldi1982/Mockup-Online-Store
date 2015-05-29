package edu.uco.rnolastname.termproject.controller;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.termproject.ejb.ClientItemOrderFacade;
import edu.uco.rnolastname.termproject.ejb.ItemFacade;
import edu.uco.rnolastname.termproject.ejb.OrdersFacade;
import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Orders;
import edu.uco.rnolastname.termproject.utilities.MailUtilities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

@Named("shoppingCartController")
@SessionScoped
public class ShoppingCartController implements Serializable {
    
    @Resource(name="mail/mailSession")
    private Session mailSession;
    
    /* Shopping Cart properties */
    private List<ClientItemOrder> clientItemOrders = new ArrayList<>();        
    private List<ClientItemOrder> removeItemOrders = new ArrayList<>();
     
    
    @Inject 
    private ClientController clientController;    
    @Inject
    private ItemController itemController;
    @Inject OrderController orderController;
    
    private Client currentClient;
    private Item currentItem;
    private Orders currentOrder;    
    private ClientItemOrder currentItemOrder;
    
    private int quantity;
    private double total;            
        
    @EJB 
    private ItemFacade itemFacade;
    @EJB
    private ClientItemOrderFacade ejbFacade;
    @EJB
    private OrdersFacade orderFacade;            
    
    /*
        Add a single order to the cart with steps: 
            Create new instance of ClientItemOrder
            Set modified date, order quantity, order subtotal
            Add this order and set client id in ClientItemOrder for Client, Item, Order
            Add this ClientItemOrder to the List in ShoppingCartController
            Add subtotal to total
            Reduce item quantity
    */
        
    public ClientItemOrderFacade getFacade(){
        return ejbFacade;
    }
    
    public void prepareCart(){        
        currentClient = clientController.getActiveClient();
        currentItem = itemController.getSelected();            
    }
    
    public void addItem(){      
        if(quantity == 0){
            JsfUtil.addErrorMessage("Please enter valid quantity");            
            return;
        }
        
        if((checkItemExist()) != null){
            JsfUtil.addErrorMessage("Item already exists, please check your cart to update quantity");            
        }else{
            try{
                if(!checkOrderError()){
                    throw new Exception("Error in checkOrderError");
                }
                
                ClientItemOrder cio = new ClientItemOrder();
                cio.setModifiedDate(new Date());        
                cio.setOrderQuantity(quantity);        
                cio.setOrderSubTotal(quantity * currentItem.getPrice());                        
                cio.setItem(currentItem);                                                            

                currentItem.addOrder(cio);
                clientItemOrders.add(cio);                                                
                updateTotal(cio.getOrderSubTotal());                          
                JsfUtil.closeDialog("imageDialog");
                
                setQuantity(-(cio.getOrderQuantity()));
                updateItemQuantity();
                JsfUtil.addSuccessMessage("Successfuly added",currentItem.getName(),quantity); 
            }catch(Exception e){
                JsfUtil.printError(this.getClass().getSimpleName(),"addItem",",Error: " + e.getMessage());  
                JsfUtil.addErrorMessage("Failed to add Item to Cart");
            }finally{                                                  
                setQuantity(0);                    
                currentItem = null;
            }        
        }
        
    }                                                                                               
    
    /* Checking out Shopping Cart methods */
    public void checkout(){            
        try{           
            currentOrder = new Orders();
            currentOrder.setModifiedDate(new Date());              
            currentOrder.setOrderDate(new Date()); 
            currentOrder.setOrderName(currentClient.getUsername() + "-" + currentOrder.getOrderDate());
            currentOrder.setOrderTotal(total);                                                       
                       
            for(ClientItemOrder cio: clientItemOrders){
                cio.setClient(currentClient);
                currentClient.addOrder(cio);
                cio.setOrder(currentOrder);
                currentOrder.addOrder(cio);
            }             
            
            orderFacade.create(currentOrder);
            mergeChanges();        
            sendCheckoutEmail();
            JsfUtil.addSuccessMessage("Order has been placed "
                    + "and an Invoice has been sent to your Email");
            orderController.setClientOrder(clientItemOrders);
            showCheckoutDialog();
        }catch(Exception e){
            JsfUtil.addErrorMessage("Failed to place Order");
            JsfUtil.printError("Checkout, error: " + e.toString());
        }finally{                        
            if(quantity != 0){
                setQuantity(0);                                
            }            
            if(total != 0){
                setTotal(0);
            }           
            if(!clientItemOrders.isEmpty()){
                clientItemOrders.clear();
            }
            if(currentOrder != null){
                currentOrder = null;
            }
        }
                
    }                      
    
    private void mergeChanges(){
        try{
            for(ClientItemOrder cio: clientItemOrders){
                itemFacade.edit(cio.getItem());            
            }            
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getSimpleName(),"itemMergeChanges",e.getMessage());
            JsfUtil.addErrorMessage("Failed to place the order");
        }
    }
    
    /* Shopping Cart Item removal methods */
    public void prepareRemoveItemOrder(ClientItemOrder itemOrder){     
        if(itemOrder != null){
            if(!removeItemOrders.contains(itemOrder)){
                removeItemOrders.add(itemOrder);                            
                JsfUtil.addSuccessMessage(itemOrder.getItem().getName() + " added for removal");            
            }else{
                removeItemOrders.add(itemOrder);
                JsfUtil.addSuccessMessage(itemOrder.getItem().getName() + " has been put back in the Cart");            
            }
        }                        
    }
    
    public void removeItemOrder(){        
        if(removeItemOrders.isEmpty()){
            JsfUtil.addErrorMessage("Please select the item you want to remove");
        }
        for(ClientItemOrder cio: removeItemOrders){
            currentItem = cio.getItem();    
            currentItemOrder = cio;
            removeItem();
            updateTotal(-(cio.getOrderSubTotal()));
        }
        JsfUtil.addSuccessMessage("Selected Items have been removed");
        clientItemOrders.removeAll(removeItemOrders);        
        removeItemOrders.clear();        
    }

    public void removeItem(){
        try{
            setQuantity(currentItemOrder.getOrderQuantity());         
            updateItemQuantity();                        
        }catch(Exception e){
            JsfUtil.addErrorMessage("Failed to empty Shopping Cart");
            JsfUtil.printError(this.getClass().getSimpleName(),"removeItem",",Error: " + e.getMessage());
        }                
    }        
    
    public void emptyCart(){        
        for(ClientItemOrder cio: clientItemOrders){
            currentItem = cio.getItem();    
            currentItemOrder = cio;
            removeItem();
        }
        if(!clientItemOrders.isEmpty()){
            clientItemOrders.clear();
        }
        if(getTotal() > 0){
            setTotal(0);
        }
        if(getQuantity() > 0){
            setQuantity(0);
        }
        JsfUtil.closeDialog("imageDialog");
        JsfUtil.addSuccessMessage("Your Cart is empty");
    }        
    
    /* Update item in the Shopping Cart methods */
    public void updateQuantityShopCart(CellEditEvent event){          
        if(event == null || ((currentItemOrder = clientItemOrders.get(event.getRowIndex())) == null)){
            JsfUtil.addErrorMessage("Failed to update Quantity, Please call System Administrator");
            return;
        }
        int oldVal = Integer.valueOf(String.valueOf(event.getOldValue()));
        int newVal = Integer.valueOf(String.valueOf(event.getNewValue()));                
        
        if(newVal != oldVal){            
            setQuantity(oldVal - newVal);            
            currentItem = currentItemOrder.getItem();
            updateTotal((newVal - oldVal) * currentItem.getPrice());            
            updateItemQuantity();
            
            if(checkOrderError()){
                currentItemOrder.setOrderQuantity(newVal);
                currentItemOrder.setOrderSubTotal(currentItemOrder.getOrderQuantity() * currentItem.getPrice());
                setQuantity(0);
                currentItem = null;
                currentItemOrder = null;
                JsfUtil.addSuccessMessage("Quantity has been updated");
            }            
        }else{
            JsfUtil.addErrorMessage("You need to enter New Quantity");
        }
    }     
    
    
    
    /* Helpers to manipulate Quantity and Total */
    private void updateItemQuantity(){        
        currentItem.setQuantity(currentItem.getQuantity() + (getQuantity()));
    }
    
    private void updateTotal(double amount){
        setTotal(getTotal() + (amount));
    }
    
    private boolean itemAvailable(int amount){
        return (currentItem.getQuantity() >= amount);
    }  
    
    private boolean checkOrderError(){                
        if(!itemAvailable(Math.abs(getQuantity()))){
            JsfUtil.addErrorMessage("There is only " 
                    + currentItem.getQuantity() + " " +currentItem.getName() + " available in my fridge");
            return false;
        }        
        return true;
    }  
    
    private Item checkItemExist(){
        Item item = null;
        for(ClientItemOrder cio: clientItemOrders){
            if(currentItem.equals(cio.getItem())){
                item = cio.getItem();
            }
        }
        return item;
    }        
    
    public void showCheckoutDialog(){
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);                
        
        RequestContext.getCurrentInstance()
                .openDialog("/views/protected/checkout", options, null);    
    }
    
    public void sendCheckoutEmail(){
        String to = currentClient.getEmail();        
        String msg = "Hi " + currentClient.getFirstName() + ",\n\n"
                + "Thank you for your purchase! Please find below the Order Details\n\n"
                + "Order Details";
        for(ClientItemOrder cio: clientItemOrders){
            msg += cio.getItem().getName() + " - " + cio.getOrderQuantity() + " - "+ cio.getOrderSubTotal() + "\n";                        
        }
        msg += "\n Total: $" + getTotal();
        msg += "\n\nSincerely yours, \n\nBarnes and Toddler";
        
        try{
            MailUtilities.sendEmail(mailSession,to,"Barnes and Toddler: Order Invoice",currentClient.getFirstName(),msg);            
        }catch(AddressException e){
            System.out.println("REY LOG: Address Exception from ClientService SendEmailRegistrationSuccess"
                    + ", Error: " + e.getMessage());            
        }catch(MessagingException e){
            System.out.println("REY LOG: MessagingException from ClientService SendEmailRegistrationSuccess"
                    + " Error: " + e.getMessage());        
        }
        
    }
    
    
    
    
    /* Properties Setters and Getters */

    public List<ClientItemOrder> getClientItemOrders() {
        return clientItemOrders;
    }

    public void setClientItemOrders(List<ClientItemOrder> clientItemOrders) {
        this.clientItemOrders = clientItemOrders;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public Orders getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Orders currentOrder) {
        this.currentOrder = currentOrder;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ClientItemOrder getCurrentItemOrder() {
        return currentItemOrder;
    }

    public void setCurrentItemOrder(ClientItemOrder currentItemOrder) {
        this.currentItemOrder = currentItemOrder;
    }
          
    
}
