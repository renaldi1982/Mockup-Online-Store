package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "order_gen", table = "sequence", pkColumnName = "seq_name", valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "order_gen")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)    
    private Date modifiedDate;
        
    @Temporal(TemporalType.TIMESTAMP)    
    private Date orderDate;            
    
    private String orderName;
    
    @Column(columnDefinition = "DECIMAL(6,2)")
    private double orderTotal;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<ClientItemOrder> clientItemOrders = new ArrayList<>();
    
    public Orders(){}
    
    public void addOrder(ClientItemOrder order){
        this.clientItemOrders.add(order);
        if(order.getOrder().equals(this)){
            order.setOrder(this);
        }
    }
    
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }    

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }    
    
    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<ClientItemOrder> getClientItemOrders() {
        return clientItemOrders;
    }

    public void setClientItemOrders(List<ClientItemOrder> clientItemOrders) {
        this.clientItemOrders = clientItemOrders;
    }
        

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uco.rnolastname.termproject.jpa.Order[ id=" + id + " ]";
    }
    
}
