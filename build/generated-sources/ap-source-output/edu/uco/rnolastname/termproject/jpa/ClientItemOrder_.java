package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Orders;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(ClientItemOrder.class)
public class ClientItemOrder_ { 

    public static volatile SingularAttribute<ClientItemOrder, Double> orderSubtotal;
    public static volatile SingularAttribute<ClientItemOrder, Item> item;
    public static volatile SingularAttribute<ClientItemOrder, Date> modifiedDate;
    public static volatile SingularAttribute<ClientItemOrder, Client> client;
    public static volatile SingularAttribute<ClientItemOrder, Integer> id;
    public static volatile SingularAttribute<ClientItemOrder, Integer> orderQuantity;
    public static volatile SingularAttribute<ClientItemOrder, Orders> order;

}