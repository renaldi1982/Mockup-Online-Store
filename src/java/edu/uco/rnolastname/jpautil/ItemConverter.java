package edu.uco.rnolastname.jpautil;

import edu.uco.rnolastname.termproject.controller.ItemController;
import edu.uco.rnolastname.termproject.jpa.Bread;
import edu.uco.rnolastname.termproject.jpa.Butter;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Milk;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=Item.class)
public class ItemConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.equals("")){
            return null;
        }
        
        ItemController controller = (ItemController) context.getELContext().getELResolver()
                .getValue(context.getELContext(), null, "itemController");
        
        return controller.getItem(getKey(value));                      
    }

    Integer getKey(String value){
        return Integer.valueOf(value);
    }
    
    String getStringKey(int value){
        return String.valueOf(value);
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null){
            return null;
        }
        
        if(value instanceof Item){
            Item item = (Item)value;
            return getStringKey(item.getId());
        }else{
            throw new IllegalArgumentException("Argument type: " + value.getClass().getName() 
                    + ", expected type: " + Item.class.getName());
        }                
    }                                
}
