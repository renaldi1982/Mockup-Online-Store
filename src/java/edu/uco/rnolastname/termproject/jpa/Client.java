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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(name="Client.findClientByUsername",query="SELECT c FROM Client c WHERE c.username = :username"),
        @NamedQuery(name="Client.findClientByUUID",query="SELECT c FROM Client c WHERE c.uuid = :uuid")
})
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableGenerator(name = "client_gen", table = "sequence", pkColumnName = "seq_name", valueColumnName = "seq_count", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "client_gen")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP) 
    private Date modifiedDate;
    
    @Column(name = "USERNAME", nullable = false, unique = true)    
    private String username;

    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;        
    private String phone;    
    private String email;   
            
    @Column(name = "CLIENT_STREET")
    private String street;
    
    @Column(name = "CLIENT_CITY")
    private String city;
    
    @Column(name = "CLIENT_STATE")
    private String state;

    private String uuid;
    
    /* Many To Many mapping */
//    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "CLIENT_GROUP", 
//            joinColumns = @JoinColumn(name = "USERNAME", referencedColumnName = "ID"), 
//            inverseJoinColumns = @JoinColumn(name = "GROUP_TYPE", referencedColumnName = "ID"))
    @OneToMany(mappedBy="client", cascade = CascadeType.ALL)
    private List<ClientGroup> groups;
    
    @OneToMany(mappedBy="client")
    private List<ClientItemOrder> clientItemOrders = new ArrayList<>();
                    
    public void addOrder(ClientItemOrder order){
        this.clientItemOrders.add(order);
        if(order.getClient().equals(this)){
            order.setClient(this);
        }
    }    
    
    public void setSingleGroup(Groups group){
        this.groups.get(0).setGroup(group);
    }   
    
    public Groups getSingleGroup(){
        return groups.get(0).getGroup();
    }
    
    public List<ClientItemOrder> getClientItemOrders() {
        return clientItemOrders;
    }

    public void setClientItemOrders(List<ClientItemOrder> clientItemOrders) {
        this.clientItemOrders = clientItemOrders;
    }        
    
    public List<ClientGroup> getGroups() {
        return groups;
    }        
    
    public void setGroups(List<ClientGroup> groups){
        this.groups = groups;
    }

    public void addGroup(ClientGroup group){
        groups.add(group);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uco.rnolastname.termproject.model.Client[ id=" + id + " ]";
    }
    
}
