package edu.uco.rnolastname.termproject.controller;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.jpautil.PaginationHelper;
import edu.uco.rnolastname.termproject.ejb.BreadFacade;
import edu.uco.rnolastname.termproject.ejb.ButterFacade;
import edu.uco.rnolastname.termproject.ejb.ClientItemOrderFacade;
import edu.uco.rnolastname.termproject.ejb.ImageFacade;
import edu.uco.rnolastname.termproject.ejb.ItemFacade;
import edu.uco.rnolastname.termproject.ejb.MilkFacade;
import edu.uco.rnolastname.termproject.ejb.OrdersFacade;
import edu.uco.rnolastname.termproject.jpa.Bread;
import edu.uco.rnolastname.termproject.jpa.Butter;
import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import edu.uco.rnolastname.termproject.jpa.Image;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Milk;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.selectitems.SelectItemsBuilder;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@Named("itemController")
@SessionScoped
public class ItemController implements Serializable {
    
    /* OmniFaces 2.0 Faces Converter for Abstract Item representation */
    
    private static final List<SelectItem> selectItems;
    static{
        selectItems = new SelectItemsBuilder()
                .add(new Bread(), "Bread")
                .add(new Butter(), "Butter")
                .add(new Milk(),"Milk")
                .buildList();        
        
    }      
    
    /* Item related variables 
    
        param: 
            current = Current Item selected from List of Item
            items = DataModel representation of List Item
            selectedItemIndex = Integer index of selected Item from DataModel
            pagination = Helper for creating pagination in List of Item
    */
    @Inject 
    private ShoppingCartController shoppingCartController;
    
    private Item current;
    private DataModel items;
    private int selectedItemIndex;
    private PaginationHelper pagination;        
        
    /* Enterprise Java Bean */
    @EJB
    private ItemFacade ejbFacade;        
    @EJB
    private OrdersFacade orderFacade;
    @EJB
    private ClientItemOrderFacade clientItemOrderFacade;
    @EJB
    private ImageFacade imageFacade;
    @EJB
    private BreadFacade breadFacade;
    @EJB
    private ButterFacade butterFacade;
    @EJB
    private MilkFacade milkFacade;
            
    private Item selectedCategory;
    
    public Item getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Item selectedCategory) {          
        this.selectedCategory = selectedCategory;        
    }        
    
    public void categoryListener(){        
        current = getSelectedCategory();
        /* Display specific item */                    
        recreateModel();
        recreatePagination();        
        getItems();        
        
        if(items.getRowCount() < 1 
                && (!JsfUtil.getRequestPath().equals("/views/protected/admin/assets/items/Create.xhtml")
                || !JsfUtil.getRequestPath().equals("/views/protected/admin/assets/items/Edit.html"))){                
            JsfUtil.addErrorMessage("No Items to be displayed");                        
        }
    }        
    
    public void addImageListener(){
        if(current == null){
            JsfUtil.addErrorMessage("Please Select Category First");            
        }
    }
    
    public ItemController(){}
    
    private ItemFacade getFacade(){
        return ejbFacade;
    }
    
    /* Item CRUD methods */
    public Item getItem(int id){
        return getFacade().find(id);
    }          
                        
    public String prepareCreate(){                  
        setSelectedCategory(null);
        current = getSelectedCategory();
        selectedItemIndex = -1;        
        return "Create";
    }          
    
    public String create(){
        try{        
            prepareImages(current.getImages());
            
            getFacade().create(current);                                                        
            
            JsfUtil.addSuccessMessage("Item has been successfuly created");
            return prepareCreate();
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(),"create()",e.getMessage());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemUploadFailed"));
            return null;
        }
    }
     
    public String prepareView(){
        current = (Item)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();   
        return "View";
    }
    
    /* Prepare List before going to List.xhtml */
    public String prepareList(){        
        recreateModel();
        return "List";
    }
    
    public String prepareEdit(){
        current = (Item)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();        
        return "Edit";
    }
    
    public String update(){
        try{                                                          
            prepareImages(current.getImages());                        
            getFacade().edit(current);                                    
            
            JsfUtil.addSuccessMessage("Client has been updated");
            return getFileName(JsfUtil.getRequestPath());                       
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(),"edit()",e.getMessage());
            JsfUtil.addErrorMessage("Failed Updating client");                         
            return null;
        }        
    }
    
    public String destroy(){
        current = (Item) getItems().getRowData();    
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();  
     
        performDestroy();
        recreatePagination();
        recreateModel();         
        
        return getFileName(FacesContext.getCurrentInstance().getViewRoot().getViewId());        
    }
    
    private void performDestroy(){
        try{
            current.getImages().removeAll(selectItems);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Item has been deleted");                        
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(), "performDestroy()", e.getMessage());
        }
    }
    
    public String destroyAndView(){
        
        return null;
    }
    
    public Item getSelected(){
        return current;
    }
    
    public void setSelected(Item item){             
        current = item;
    }
    
    
    
    
    /* Helper Image methods */        
    public String prepareManageImages(){
        current = (Item)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Image";
    }
        
    public List<Image> prepareImages(List<Image> images){
        for(Image img: images){   
            if(img.getOwner() == null){
                img.setOwner(current);
            }            
        }
        return images;
    }
    
    public String prepareAddImage(FileUploadEvent event){        
        if(null == current){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemImageCategoryRequired"));         
            return null;
        }                        
        
        Image image = new Image();   
        image.setModifiedDate(new Date());             
        image.setName(current.getCategory() + "-" + event.getFile().getFileName().substring(0, event.getFile().getFileName().indexOf('.')));
        image.setContent_type("image/" + event.getFile().getFileName().substring(event.getFile().getFileName().indexOf('.') + 1));
        try{            
            byte[] bytes = ImageController.iStreamToByteArray(event.getFile().getInputstream());            
            
            if(bytes.length > 0){
                image.setContent(bytes);
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemImageUploadFailed"));
            }            
        }catch(IOException e){
            JsfUtil.printError(this.getClass().getName(), "prepareAddImage", e.getMessage());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemImageUploadFailed"));
            return null;
        }                               
        current.addImage(image);
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                .getString("ItemImageUploadSuccess"));                
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('addImageDialog').hide()");
        
        if(current.getId() != null){            
            image.setOwner(current);
            storeImage(image);            
        }
        
        return "Create";
    }
    
    public void removeImage(Image img){
        current.getImages().remove(img);        
    }
    
    public void deleteImage(Image img){
        current.getImages().remove(img);  
        try{
            imageFacade.remove(img);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Image has been deleted");
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(), "deleteImage()", e.getMessage());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemImageUploadFailed"));
        }       
    }
    
    public void storeImage(Image image){
        try{
            imageFacade.create(image);            
        }catch(Exception e){
            JsfUtil.printError(this.getClass().getName(), "storeImage()", e.getMessage());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("edu.uco.rnolastname.termproject.utilities/Bundle")
                    .getString("ItemImageUploadFailed"));
        }
                        
    }            
    
    
    public List<SelectItem> getAvailableItems(){
        return selectItems;
    }        
    

    public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-DD HH:mm:ss",Locale.US);
        String returnDate = sdf.format(date);
        return returnDate;
    }             
        
    
    public Date getDateTime(){
        return new Date();
    }
    
    public DataModel getItems(){
        if(items == null){
            items = getPagination().createPageDataModel();
            updatingEntity();
        }
        return items;
    }                
    
    
    /* Helper for Pagination */
          
    public PaginationHelper getPagination(){
        if(pagination == null){
            pagination = new PaginationHelper(5){
                
                @Override
                public int getItemsCount(){
                    int count = 0;
                    
                    if(getSelectedCategory() != null){
                        String category = selectedCategory.getCategory();
                        
                        switch(category){
                            case "Bread":
                                count = breadFacade.count();
                                break;
                            case "Butter":
                                count = butterFacade.count();
                                break;
                            case "Milk":
                                count = milkFacade.count();
                                break;                            
                        }                        
                    }else{
                        count = getFacade().count();
                    }
                    
                    return count;
                }
                
                @Override
                public DataModel createPageDataModel(){
                    List<Item> listItems;
                    
                    if(getSelectedCategory() != null){
                        String category = selectedCategory.getCategory();
                        listItems = getFacade().findItemRangeByCategory(new int[]{getPageFirstItem(),
                            getPageFirstItem() + getPageSize() - 1}, category);                            
                    }else{
                        listItems = getFacade().findRange(new int[]{getPageFirstItem(),
                            getPageFirstItem() + getPageSize() - 1});                        
                    }                                                                          
                    
                    return new ListDataModel(listItems);
                }                                
            };
        }
        return pagination;
    }
    
    private void recreateModel() {
        items = null;
    }
    
    private void recreatePagination(){
        pagination = null;
    }
    
    public String next(){        
        getPagination().nextPage();
        recreateModel();          
        return getFileName(FacesContext.getCurrentInstance().getViewRoot().getViewId());
    }
            
    public String previous(){        
        getPagination().previousPage();
        recreateModel();        
        return getFileName(FacesContext.getCurrentInstance().getViewRoot().getViewId());
    }
 
    public String getFileName(String path){        
        int period = path.lastIndexOf('.');
        int slash = path.lastIndexOf('/');
        
        String basename = path.substring(slash + 1, period);
        return basename;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }
    
    /* Need to change this with LOCK ~ DO NOT USE THIS AS THIS WILL DECREMENT 
        THE QUANTITY OF AVAILABLE ITEMS EVERY TIME THE DATAMODEL IS RECREATED
        IE : WHEN CLIENT CLICK THE NEXT ON PAGINATION
    */
    public void updatingEntity(){
        List<ClientItemOrder> orders = null;
        if(shoppingCartController == null){
            return;
        }
        
        orders = shoppingCartController.getClientItemOrders();
        
        if(orders == null || orders.isEmpty()){
            return;
        }
        
        for(Iterator it = items.iterator(); it.hasNext();){
            Item item = (Item)it.next();
            
            for(ClientItemOrder cio: orders){
                if(item.equals(cio.getItem())){
                    item.setQuantity(item.getQuantity() - cio.getOrderQuantity());
                    JsfUtil.printError("Setting Quantity to: " + item.getQuantity());
                }
            }
        } 
        
    }
}
