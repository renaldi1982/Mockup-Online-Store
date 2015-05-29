/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.ejb;

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
public class ItemFacade extends AbstractFacade<Item> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }
    
    public List<Item> findItemRangeByCategory(int[] range, String category){
        List<Item> items = em.createNamedQuery("Item.findItemByCategory",Item.class)
                .setMaxResults(range[1] - range[0] + 1).setFirstResult(range[0])
                .setParameter("category", category).getResultList();
        return items;
    }
    
    public Item findItemById(int id){
        List<Item> items = em.createNamedQuery("Item.findItemById",Item.class)
                .setParameter("id", id).getResultList();
        
        return items.get(0);
    }
    
}
