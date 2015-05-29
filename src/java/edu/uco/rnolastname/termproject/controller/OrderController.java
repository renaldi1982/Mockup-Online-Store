package edu.uco.rnolastname.termproject.controller;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.termproject.ejb.ClientItemOrderFacade;
import edu.uco.rnolastname.termproject.ejb.ItemFacade;
import edu.uco.rnolastname.termproject.ejb.OrdersFacade;
import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Orders;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

@Named("orderController")
@SessionScoped
public class OrderController implements Serializable{
    private static Map<Orders,List<Item>> orderItems = new HashMap<>();
    private static List<Orders> orders = new ArrayList<>();    
    private List<Orders> removeOrders = new ArrayList<>();
    
    private List<ClientItemOrder> clientOrders = new ArrayList<>();
    
    private Orders current;        
    private int selectedItemIndex;
    private Client currentClient;
    
    @Inject
    private ClientController clientController;
    
    @EJB
    private OrdersFacade ejbFacade;
    @EJB
    private ItemFacade itemFacade;
    @EJB
    private ClientItemOrderFacade cioFacade;
    
    
    
    
    @PostConstruct 
    public void init(){
        currentClient = clientController.getActiveClient();
    }
    
    public void setClientOrder(List<ClientItemOrder> itemOrder){
        this.clientOrders = itemOrder;
    }
    
    public List<ClientItemOrder> getClientOrders(){
        return this.clientOrders;
    }
    
    private OrdersFacade getFacade(){
        return ejbFacade;
    }
    
    public List<Orders> getOrders(){
        return getFacade().findAll();
    }
    
    public Orders getOrder(Orders order){
        return getFacade().find(order);
    }        
    
    public void prepareCancelOrder(Orders order){
        try{
            if(!removeOrders.contains(order)){            
                removeOrders.add(order);
                JsfUtil.addSuccessMessage(order.getOrderName() + " has been added for removal");            
            }else{
                removeOrders.remove(order);
                JsfUtil.addSuccessMessage(order.getOrderName() + " has been returned back to your list of Orders");
            }
        }catch(NullPointerException e){
            JsfUtil.printError(this.getClass().getSimpleName(),"prepareCancelOrder",",error: " + e.getMessage());
        }                
    }
    
    public boolean processCancelOrder(List<ClientItemOrder> cancelOrders){
        try{            
            int totalReturn = -1;
            totalReturn = cancelOrders.stream().mapToInt((x) -> x.getOrderQuantity()).sum();

            if(totalReturn != -1){
                for(ClientItemOrder i: cancelOrders){                    
                    Item item = i.getItem();
                    item.setQuantity(item.getQuantity() + i.getOrderQuantity());
                    itemFacade.edit(i.getItem());                    
                }
            }
            
            return true;
        }catch(Exception e){            
            JsfUtil.printError(this.getClass().getSimpleName(),"processCancelOrder",",error : "+ e.getMessage());
            return false;
        }                        
    }
    
    public void cancelOrder(){   
        boolean success = false;
        if(removeOrders.isEmpty()){
            JsfUtil.addErrorMessage("Please select Order that you want to remove");
        }
                
        for(Orders o: removeOrders){
            success = processCancelOrder(o.getClientItemOrders());
        }        
        
        if(success == true){
            JsfUtil.addSuccessMessage("Order has been cancelled");
        }else{
            JsfUtil.addErrorMessage("Failed to Cancel Order");
        }
    }       
    
    public void showOrderDialog(){
        if(!orders.isEmpty()){
            orders.clear();
        }
        
        if(!clientOrders.isEmpty()){
            clientOrders.clear();
        }
        
        if(currentClient == null){
            currentClient = clientController.getActiveClient();            
        }
        
        clientOrders = cioFacade.findOrderByClient(currentClient);
        JsfUtil.addErrorMessage(currentClient.getFirstName() + " Order size is: " + clientOrders.size());
        if(clientOrders.isEmpty()){
            JsfUtil.addErrorMessage(currentClient.getFirstName() + " never placed and Order");
            return;
        }                                
        
        for(ClientItemOrder cio: clientOrders){
//            Orders o = getFacade().find(cio.getOrder().getId());            
            if(!orders.contains(cio.getOrder())){
                JsfUtil.printError("Adding " + cio.getOrder().getId() + " To orders list in purchase");
                orders.add(cio.getOrder());                
            }            
        }                
        JsfUtil.addErrorMessage(currentClient.getFirstName() + " Actual Order size is: " + orders.size());
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);        
        
        RequestContext.getCurrentInstance()
                .openDialog("/views/protected/Purchase", options, null);        
    }        
    
    public void dialogClosed(CloseEvent event) {
        JsfUtil.addSuccessMessage("Dialog is closed");
    }

    public List<ClientItemOrder> getItemOrders(Orders order){
        if(order == null){
            JsfUtil.addErrorMessage("Failed to fetch Item in the Order");
            return null;
        }
        
        List<ClientItemOrder> itemOrders = cioFacade.findItemByOrder(order);
        JsfUtil.printError("return getitemorders: " + itemOrders.size());
        return itemOrders;
    }
    
    public Item getItem(Item item){                
        return itemFacade.find(item.getId());        
    }
    
    public List<Item> getItems(Orders order){
        if(order == null){
            JsfUtil.addErrorMessage("order null");
            return null;
        }
        List<ClientItemOrder> cios = cioFacade.findItemByOrder(order);
        
        List<Item> items = new ArrayList<>();
        for(ClientItemOrder cio: cios){
            Item i = itemFacade.find(cio.getItem().getId());
            if(!items.contains(i)){
                items.add(i);
            }
        }                
        
        return items;                
    }
    
    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }
    
    public String getTotalToString(double amount){
        return String.valueOf(amount);
    }
    
}
