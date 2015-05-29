package edu.uco.rnolastname.termproject.controller;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.termproject.ejb.ImageFacade;
import edu.uco.rnolastname.termproject.ejb.ItemFacade;
import edu.uco.rnolastname.termproject.jpa.Bread;
import edu.uco.rnolastname.termproject.jpa.Butter;
import edu.uco.rnolastname.termproject.jpa.Image;
import edu.uco.rnolastname.termproject.jpa.Item;
import edu.uco.rnolastname.termproject.jpa.Milk;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value="imageManager")
@ApplicationScoped
public class ImageController implements Serializable {
    private static final String destination = "C:\\Users\\Rey\\Documents\\NetBeansProjects\\termproject\\web\\resources\\images\\item_images";        
    
    /* Image Attributes */
    private final static int largeImgHeight = 336;
    private final static int largeImgWidth = 516;
    
    private final static int medImgHeight = 168;
    private final static int medImgWidth = 258;   
    
    private final static int smallImgHeight = 84;
    private final static int smallImgWidth = 129;
    
    @EJB
    private ImageFacade imageFacade;         

    @EJB 
    private ItemFacade itemFacade;
    
    private Image selectedImage;
    
    public List<Item> getItems(){
        List<Item> items = itemFacade.findAll();        
        return items;
    }
    
    public List<Bread> getBread(){
        
        return null;
    }
    
    public List<Butter> getButter(){
        
        return null;
    }
    
    public List<Milk> getMilk(){
        
        return null;
    }
    
    public List<Image> getItemImages(Item item){
        List<Image> images = imageFacade.getImagesByItem(item);                    
        
        return images;
    }    
    
    public StreamedContent getImage(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
            /* HTML Rendering the HTML, return a stub streamed content so that it will generate the right URL */            
            
            return new DefaultStreamedContent();
        }else{
            /* HTML Rendering the Actual Image */
            String imageid = context.getExternalContext().getRequestParameterMap().get("imageid");            
            Image img = imageFacade.find(Integer.valueOf(imageid));                        
            
            BufferedImage resized = resizeImage(byteArrayToBufferedImage(img.getContent()),"medium");
            byte[] newImage = bufferedImageToByteArray(resized);
            return new DefaultStreamedContent(new ByteArrayInputStream(newImage),img.getContent_type());
        }
    }              
    
    public StreamedContent getSmallImage(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
            /* HTML Rendering the HTML, return a stub streamed content so that it will generate the right URL */            
            return new DefaultStreamedContent();
        }else{
            /* HTML Rendering the Actual Image */
            String imageid = context.getExternalContext().getRequestParameterMap().get("imageid");
            Image img = imageFacade.find(Integer.valueOf(imageid));
            
            BufferedImage resized = resizeImage(byteArrayToBufferedImage(img.getContent()),"small");
            byte[] newImage = bufferedImageToByteArray(resized);
            return new DefaultStreamedContent(new ByteArrayInputStream(newImage),img.getContent_type());
        }
    }

    public StreamedContent getLargeImage(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
            /* HTML Rendering the HTML, return a stub streamed content so that it will generate the right URL */            
            return new DefaultStreamedContent();
        }else{
            /* HTML Rendering the Actual Image */
            String imageid = context.getExternalContext().getRequestParameterMap().get("imageid");
            Image img = imageFacade.find(Integer.valueOf(imageid));
            
            BufferedImage resized = resizeImage(byteArrayToBufferedImage(img.getContent()),"large");
            byte[] newImage = bufferedImageToByteArray(resized);
            return new DefaultStreamedContent(new ByteArrayInputStream(newImage),img.getContent_type());
        }
    }
    
    public Image getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
    }          
    
    
    public static byte[] iStreamToByteArray(InputStream iStream){
        byte[] imageBytes = null;
        try{
            BufferedImage original = ImageIO.read(iStream);
                        
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(original,"jpg",bos);
            bos.flush();

            imageBytes = bos.toByteArray();
        }catch(Exception e){
            JsfUtil.printError(ImageController.class.getName(), "imageResizeAndConvert", e.getMessage());
            JsfUtil.addErrorMessage("Resize and Convert Failed, Reason: " + e.getLocalizedMessage());
        }                
        
        return imageBytes;
    }                
    
    public static byte[] bufferedImageToByteArray(BufferedImage original){
        byte[] imageBytes = null;
        try{                                    
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(original,"jpg",bos);
            bos.flush();

            imageBytes = bos.toByteArray();
        }catch(Exception e){
            JsfUtil.printError(ImageController.class.getName(), "imageResizeAndConvert", e.getMessage());
            JsfUtil.addErrorMessage("Resize and Convert Failed, Reason: " + e.getLocalizedMessage());
        }                
        
        return imageBytes;
    }
    
    /* Convert byte array to BufferedImage */
    private static BufferedImage byteArrayToBufferedImage(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }    
    
    private static BufferedImage resizeImage(BufferedImage original, String size){
        BufferedImage resized;
        int type = (original.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : original.getType();              
        Graphics2D g;
        
        switch (size) {
            case "medium":
                resized = new BufferedImage(medImgWidth,medImgHeight,type);
                g = resized.createGraphics();
        
                g.drawImage(original,0,0,medImgWidth,medImgHeight,null);
                break;
            case "small":
                resized = new BufferedImage(smallImgWidth,smallImgHeight,type);
                g = resized.createGraphics();
        
                g.drawImage(original,0,0,smallImgWidth,smallImgHeight,null);
                break;
            case "large":
                resized = new BufferedImage(largeImgWidth,largeImgHeight,type);
                g = resized.createGraphics();
        
                g.drawImage(original,0,0,largeImgWidth,largeImgHeight,null);
                break;
            default:
                resized = new BufferedImage(original.getWidth(),original.getHeight(),type);
                g = resized.createGraphics();
        
                g.drawImage(original,0,0,original.getWidth(),original.getHeight(),null);
                break;
        }
                
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                       
        return resized;
    }                
}
