package edu.uco.rnolastname.termproject.jpa;

import edu.uco.rnolastname.termproject.jpa.Item;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-29T13:24:11")
@StaticMetamodel(Image.class)
public class Image_ { 

    public static volatile SingularAttribute<Image, Item> owner;
    public static volatile SingularAttribute<Image, String> content_type;
    public static volatile SingularAttribute<Image, Date> modifiedDate;
    public static volatile SingularAttribute<Image, String> name;
    public static volatile SingularAttribute<Image, Integer> id;
    public static volatile SingularAttribute<Image, byte[]> content;

}