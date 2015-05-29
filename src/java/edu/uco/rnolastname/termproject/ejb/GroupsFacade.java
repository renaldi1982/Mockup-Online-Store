/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uco.rnolastname.termproject.ejb;

import edu.uco.rnolastname.termproject.jpa.Groups;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rey
 */
@Stateless
public class GroupsFacade extends AbstractFacade<Groups> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupsFacade() {
        super(Groups.class);
    }
    
    public EntityManager getEntManager(){
        return em;
    }
    
    public Groups getGroupById(int id){
        return em.find(Groups.class, id);
    }
    
    public String findGroupTypeById(int id){
        return find(id).getGroupType();        
    }
    
    public Groups findGroupById(int id){
        return find(id);
    }
}
