/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.services;

import com.zeta.secure.model.AbstractEntity;
import com.zeta.secure.repository.JpaController;
import com.zeta.secure.repository.JpaControllerFactory;
import com.zeta.secure.repository.exceptions.NonexistentEntityException;
import com.zeta.secure.repository.exceptions.PreexistingEntityException;
import com.zeta.secure.repository.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author zeta
 */
public abstract class AbstractFacade<T extends AbstractEntity,JPA extends JpaController> {
    private Class<T> entityClass;
    private Class<JPA> jpaControllerClass;
    protected JPA jpaController;
    
    @Resource
    private UserTransaction userTransaction;
    
    @PersistenceUnit(unitName = "SecureJavaEEApp-ejbPU")
    private EntityManagerFactory emf;

    public AbstractFacade(Class<T> entityClass, Class<JPA> jpaControllerClass) {
        this.entityClass = entityClass;
        this.jpaControllerClass = jpaControllerClass;
    }
      
    
    
    @PostConstruct
    private void createJpaFactory(){
      jpaController = JpaControllerFactory.getJpaControllerInstance(jpaControllerClass, userTransaction, emf);
    }

//    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        try {
            jpaController.create(entity);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void edit(T entity) {
        try {
            //        getEntityManager().merge(entity);
           jpaController.edit(entity);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void remove(T entity) {
        try {
            //        getEntityManager().remove(getEntityManager().merge(entity));
                    jpaController.destroy(entity);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public T find(Object id) {
       return (T) jpaController.find(id);
    }

    public List<T> findAll() {
        return jpaController.findEntities();
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        cq.select(cq.from(entityClass));
//        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
//return (T) jPAController.findEntities(maxResults, firstResult)
        return null;
    }

    public int count() {
        return jpaController.getCount();
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
//        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
//        javax.persistence.Query q = getEntityManager().createQuery(cq);
//        return ((Long) q.getSingleResult()).intValue();
    }
    
}
