package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile SingularAttribute<Client, String> lastName;
    public static volatile SingularAttribute<Client, String> city;
    public static volatile ListAttribute<Client, ClientGroup> groups;
    public static volatile SingularAttribute<Client, String> uuid;
    public static volatile ListAttribute<Client, ClientItemOrder> clientItemOrders;
    public static volatile SingularAttribute<Client, String> firstName;
    public static volatile SingularAttribute<Client, String> password;
    public static volatile SingularAttribute<Client, String> phone;
    public static volatile SingularAttribute<Client, String> street;
    public static volatile SingularAttribute<Client, Date> modifiedDate;
    public static volatile SingularAttribute<Client, Integer> id;
    public static volatile SingularAttribute<Client, String> state;
    public static volatile SingularAttribute<Client, String> email;
    public static volatile SingularAttribute<Client, String> username;

}