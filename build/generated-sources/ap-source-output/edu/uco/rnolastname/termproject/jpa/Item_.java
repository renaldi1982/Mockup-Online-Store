package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import edu.uco.rnolastname.termproject.jpa.Image;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(Item.class)
public abstract class Item_ { 

    public static volatile ListAttribute<Item, Image> images;
    public static volatile SingularAttribute<Item, Integer> quantity;
    public static volatile SingularAttribute<Item, Double> price;
    public static volatile SingularAttribute<Item, Date> modifiedDate;
    public static volatile SingularAttribute<Item, String> name;
    public static volatile SingularAttribute<Item, Integer> id;
    public static volatile SingularAttribute<Item, String> category;
    public static volatile ListAttribute<Item, ClientItemOrder> clientItemOrders;

}