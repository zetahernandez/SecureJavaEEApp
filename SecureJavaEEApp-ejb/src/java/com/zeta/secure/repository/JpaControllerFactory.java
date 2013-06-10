/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author zeta
 */
public class JpaControllerFactory {
    
    private static Map<Class<? extends JpaController>,Class<?  extends JpaController>> map = new HashMap<Class<? extends JpaController>, Class<? extends JpaController>>();
    
    static {
        map.put(UserJpaController.class, UserJpaControllerImpl.class);
        map.put(RoleJpaController.class, RoleJpaControllerImpl.class);
    }
    
    public static <T extends JpaController> T getJpaControllerInstance(Class<T> controller,UserTransaction userTransaction, EntityManagerFactory entityManagerFactory ){
        try {
            Class<? extends JpaController> controllerImplClass = map.get(controller);
            JpaController newInstance = controllerImplClass.getDeclaredConstructor(UserTransaction.class,EntityManagerFactory.class).newInstance(userTransaction,entityManagerFactory);
            return (T) newInstance;
        } catch (InstantiationException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(JpaControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
