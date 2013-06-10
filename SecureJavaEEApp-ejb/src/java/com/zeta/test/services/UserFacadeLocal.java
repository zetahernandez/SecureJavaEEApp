/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.test.services;

import com.zeta.test.model.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zeta
 */
@Local
public interface UserFacadeLocal {

    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(Object id);

    List<User> findAll();

    List<User> findRange(int[] range);

    int count();
    
    User find(String username, String password);
}
