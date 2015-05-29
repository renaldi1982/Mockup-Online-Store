/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@NamedQueries({
    @NamedQuery(name="ClientItemOrder.findOrderByClient",query="SELECT cio FROM ClientItemOrder cio WHERE cio.client = :client"),
    @NamedQuery(name="ClientItemOrder.findItemByOrder",query="SELECT cio FROM ClientItemOrder cio WHERE cio.order = :order")
})
public class ClientItemOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "client_item_order_gen", table = "sequence", pkColumnName = "seq_name", valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "client_item_order_gen")
    private Integer id;            

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;       
    
    private int orderQuantity;
    
    @Column(columnDefinition = "Decimal(6,2)")
    private double orderSubtotal;
        
    @ManyToOne
    @JoinColumn(name="orderId",referencedColumnName="id")
    private Orders order;
        
    @ManyToOne
    @JoinColumn(name="itemId",referencedColumnName="id")
    private Item item;    
        
    @ManyToOne 
    @JoinColumn(name="clientId",referencedColumnName="id")
    private Client client;
                        
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

    public Orders getOrder() {
        return order;
    }    

    public void setOrder(Orders order) {
        this.order = order;
        if(!order.getClientItemOrders().contains(this)){
            order.getClientItemOrders().add(this);
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if(!item.getClientItemOrders().contains(this)){
            item.getClientItemOrders().add(this);
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        if(!client.getClientItemOrders().contains(this)){
            client.getClientItemOrders().add(this);
        }
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public double getOrderSubTotal() {
        return orderSubtotal;
    }

    public void setOrderSubTotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }
    
       
    
}
