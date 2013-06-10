/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.test.services;

import com.zeta.test.model.User;
import com.zeta.test.repository.UserJpaController;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 *
 * @author zeta
 */
@Stateless
@RolesAllowed("admin,user")
public class UserFacade extends AbstractFacade<User, UserJpaController> implements UserFacadeLocal {

    public UserFacade() {
        super(User.class, UserJpaController.class);
    }

    @Override
    @PermitAll
    public User find(String username, String password) {
            User find = jpaController.find(username, password);
        return find;
    }
}
