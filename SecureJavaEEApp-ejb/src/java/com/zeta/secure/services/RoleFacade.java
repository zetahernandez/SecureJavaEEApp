/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.services;

import com.zeta.secure.model.Role;
import com.zeta.secure.repository.RoleJpaController;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

/**
 *
 * @author zeta
 */
@Stateless
@PermitAll
public class RoleFacade extends AbstractFacade<Role,RoleJpaController> implements RoleFacadeLocal {
  
    public RoleFacade() {
        super(Role.class, RoleJpaController.class);
    }

    
    
}
