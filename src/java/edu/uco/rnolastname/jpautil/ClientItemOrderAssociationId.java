
package edu.uco.rnolastname.jpautil;

import java.io.Serializable;

public class ClientItemOrderAssociationId implements Serializable{
    
    private int clientId;
    private int itemId;
    private int orderId;

    public int getClientId() {
        return clientId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOrderId() {
        return orderId;
    }
          
    @Override
    public int hashCode(){
        return (clientId + itemId + orderId);                
    }
    
    @Override
    public boolean equals(Object o){
        if((o instanceof ClientItemOrderAssociationId)){
            ClientItemOrderAssociationId otherId = (ClientItemOrderAssociationId) o;
            
            return ((otherId.clientId == this.clientId) && (otherId.itemId == this.itemId) 
                    && (otherId.orderId == this.orderId));
        }                                
        return false;
    }    
}
