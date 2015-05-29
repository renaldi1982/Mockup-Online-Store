/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.ejb;

import edu.uco.rnolastname.termproject.jpa.Image;
import edu.uco.rnolastname.termproject.jpa.Item;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rey
 */
@Stateless
public class ImageFacade extends AbstractFacade<Image> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImageFacade() {
        super(Image.class);
    }
    
    public EntityManager getEntManager(){
        return em;
    }
    
    public List<Image> getImagesByItem(Item item){
        return em.createNamedQuery("Image.findImageByOwnerID")
                .setParameter("ownerid", item).getResultList();
    }
        
    
}
