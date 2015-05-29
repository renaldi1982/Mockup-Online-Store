/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.jpautil;

import java.io.Serializable;

public class ClientGroupAssociationId implements Serializable {
    
    private int clientId;
    private int groupId;

    public int getClientId() {
        return clientId;
    }

    public int getGroupId() {
        return groupId;
    }        
    
    @Override
    public int hashCode(){
        return (clientId + groupId);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof ClientGroupAssociationId){
            ClientGroupAssociationId otherId = (ClientGroupAssociationId)o;
            return (otherId.clientId == this.clientId) && (otherId.clientId == this.groupId);
        }
        
        return false;
    }
    
}
