/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.services;

import com.zeta.secure.model.User;
import com.zeta.secure.repository.UserJpaController;
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

    @Override
    public void create(User entity) {
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @RolesAllowed("admin")
    public void edit(User entity) {
        super.edit(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @RolesAllowed("admin")
    public void remove(User entity) {
        super.remove(entity); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
