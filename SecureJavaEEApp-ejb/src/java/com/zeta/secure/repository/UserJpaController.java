/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.repository;

import com.zeta.secure.model.User;

/**
 *
 * @author zeta
 */
public interface UserJpaController extends JpaController<User, Integer> {

    public User find(String username, String password);
    
}
