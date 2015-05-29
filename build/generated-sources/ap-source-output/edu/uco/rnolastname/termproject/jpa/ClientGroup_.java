package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.Groups;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(ClientGroup.class)
public class ClientGroup_ { 

    public static volatile SingularAttribute<ClientGroup, Integer> clientId;
    public static volatile SingularAttribute<ClientGroup, Integer> groupId;
    public static volatile SingularAttribute<ClientGroup, Date> modifiedDate;
    public static volatile SingularAttribute<ClientGroup, Client> client;
    public static volatile SingularAttribute<ClientGroup, Groups> group;

}