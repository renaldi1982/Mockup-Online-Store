/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
    @NamedQuery(name="Item.findItemById",query="SELECT i FROM Item i WHERE i.id = :itemId"),
    @NamedQuery(name="Item.findItemByCategory",query="SELECT i FROM Item i WHERE i.category = :category")
})
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableGenerator(name = "item_gen", table = "sequence", pkColumnName = "seq_name", valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "item_gen")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)    
    private Date modifiedDate;
    
    private String category;
    private String name;
    private double price;
    private int quantity;
    
    @OneToMany(mappedBy="owner",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();
    
    @OneToMany(mappedBy="item", cascade = CascadeType.MERGE)
    private List<ClientItemOrder> clientItemOrders = new ArrayList<>();
    
    public Item(){
        this.modifiedDate = new Date();        
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }     

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<ClientItemOrder> getClientItemOrders() {
        return clientItemOrders;
    }

    public void setClientItemOrders(List<ClientItemOrder> clientItemOrders) {
        this.clientItemOrders = clientItemOrders;
    }
    
    public void addImage(Image image){
        this.images.add(image);
        if(image.getOwner() != this){
            image.setOwner(this);
        }
    }
        
    public void addOrder(ClientItemOrder order){
        this.clientItemOrders.add(order);
        if(order.getItem().equals(this)){
            order.setItem(this);
        }
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }        
    
    /* Abstract methods to be independently implemented in the subclass */
    public abstract void setUnique1(String val);
    public abstract void setUnique2(String val);
    public abstract String getUnique1();
    public abstract String getUnique2(); 
}
