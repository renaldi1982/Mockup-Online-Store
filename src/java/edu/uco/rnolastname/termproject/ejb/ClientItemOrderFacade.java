
package edu.uco.rnolastname.termproject.ejb;

import edu.uco.rnolastname.jpautil.JsfUtil;
import edu.uco.rnolastname.termproject.jpa.Client;
import edu.uco.rnolastname.termproject.jpa.ClientItemOrder;
import edu.uco.rnolastname.termproject.jpa.Orders;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class ClientItemOrderFacade extends AbstractFacade<ClientItemOrder> {
    @PersistenceContext(unitName = "termprojectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientItemOrderFacade() {
        super(ClientItemOrder.class);
    }
    
    
    
    public List<ClientItemOrder> findOrderByClient(Client c){
        List<ClientItemOrder> cio = em.createNamedQuery("ClientItemOrder.findOrderByClient",ClientItemOrder.class)
                .setParameter("client", c).getResultList();        
        
        return cio;
    }
    
    public List<ClientItemOrder> findItemByOrder(Orders o){
        List<ClientItemOrder> items = em.createNamedQuery("ClientItemOrder.findItemByOrder",ClientItemOrder.class)
                .setParameter("order", o).getResultList();
        JsfUtil.printError("return findItemByOrder: " + items.size());
        return items;
    }
        
    
}
