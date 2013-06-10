/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.test.repository;

import com.zeta.test.repository.exceptions.NonexistentEntityException;
import com.zeta.test.repository.exceptions.PreexistingEntityException;
import com.zeta.test.repository.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author zeta
 */
public interface JpaController<T,ID> extends Serializable {

    void create(T entity) throws PreexistingEntityException, RollbackFailureException, Exception;

    void destroy(ID id) throws NonexistentEntityException, RollbackFailureException, Exception;

    void edit(T entity) throws NonexistentEntityException, RollbackFailureException, Exception;

    T find(ID id);

    List<T> findEntities();

    List<T> findEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getCount();
    
}
