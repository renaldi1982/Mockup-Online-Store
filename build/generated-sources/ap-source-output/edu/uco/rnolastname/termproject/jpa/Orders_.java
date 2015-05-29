package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, Date> modifiedDate;
    public static volatile SingularAttribute<Orders, Integer> id;
    public static volatile SingularAttribute<Orders, Date> orderDate;
    public static volatile ListAttribute<Orders, ClientItemOrder> clientItemOrders;
    public static volatile SingularAttribute<Orders, Double> orderTotal;
    public static volatile SingularAttribute<Orders, String> orderName;

}