package edu.uco.rnolastname.jpautil;

import edu.uco.rnolastname.termproject.controller.ClientController;
import edu.uco.rnolastname.termproject.jpa.Groups;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=Groups.class)
public class GroupConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        if(value == null || value.length() == 0){
            return null;
        }                
        
        ClientController controller = (ClientController) context.getApplication()
                .getELResolver().getValue(context.getELContext(), null, "clientController");
        
        return controller.getGroup(getKey(value));
    }

    int getKey(String value){
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
        
        if(value instanceof Groups){
            Groups o = (Groups) value;
            return getStringKey(o.getId());
        }else{
            throw new IllegalArgumentException(value + " is of type: " 
                + value.getClass().getName() + "; expected type: " + Groups.class.getName());
        }
        
    }
    
    
    
}
