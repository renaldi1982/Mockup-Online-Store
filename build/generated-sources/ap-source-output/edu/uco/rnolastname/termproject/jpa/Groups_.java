package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.ClientGroup;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(Groups.class)
public class Groups_ { 

    public static volatile SingularAttribute<Groups, String> groupType;
    public static volatile ListAttribute<Groups, ClientGroup> clients;
    public static volatile SingularAttribute<Groups, String> groupDescription;
    public static volatile SingularAttribute<Groups, Date> modifiedDate;
    public static volatile SingularAttribute<Groups, Integer> id;

}