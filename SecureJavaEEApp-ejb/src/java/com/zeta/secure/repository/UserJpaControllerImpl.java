/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeta.secure.repository;

import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.zeta.secure.model.Role;
import com.zeta.secure.model.User;
import com.zeta.secure.repository.exceptions.NonexistentEntityException;
import com.zeta.secure.repository.exceptions.PreexistingEntityException;
import com.zeta.secure.repository.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author zeta
 */
public class UserJpaControllerImpl implements UserJpaController {

    UserJpaControllerImpl(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(User user) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (user.getRoleList() == null) {
            user.setRoleList(new ArrayList<Role>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Role> attachedRoleList = new ArrayList<Role>();
            for (Role roleListRoleToAttach : user.getRoleList()) {
                roleListRoleToAttach = em.getReference(roleListRoleToAttach.getClass(), roleListRoleToAttach.getId());
                attachedRoleList.add(roleListRoleToAttach);
            }
            user.setRoleList(attachedRoleList);
            em.persist(user);
            for (Role roleListRole : user.getRoleList()) {
                User oldUserIdOfRoleListRole = roleListRole.getUserId();
                roleListRole.setUserId(user);
                roleListRole = em.merge(roleListRole);
                if (oldUserIdOfRoleListRole != null) {
                    oldUserIdOfRoleListRole.getRoleList().remove(roleListRole);
                    oldUserIdOfRoleListRole = em.merge(oldUserIdOfRoleListRole);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (find(user.getId()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(User user) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User persistentUser = em.find(User.class, user.getId());
            List<Role> roleListOld = persistentUser.getRoleList();
            List<Role> roleListNew = user.getRoleList();
            List<Role> attachedRoleListNew = new ArrayList<Role>();
            for (Role roleListNewRoleToAttach : roleListNew) {
                roleListNewRoleToAttach = em.getReference(roleListNewRoleToAttach.getClass(), roleListNewRoleToAttach.getId());
                attachedRoleListNew.add(roleListNewRoleToAttach);
            }
            roleListNew = attachedRoleListNew;
            user.setRoleList(roleListNew);
            user = em.merge(user);
            for (Role roleListOldRole : roleListOld) {
                if (!roleListNew.contains(roleListOldRole)) {
                    roleListOldRole.setUserId(null);
                    roleListOldRole = em.merge(roleListOldRole);
                }
            }
            for (Role roleListNewRole : roleListNew) {
                if (!roleListOld.contains(roleListNewRole)) {
                    User oldUserIdOfRoleListNewRole = roleListNewRole.getUserId();
                    roleListNewRole.setUserId(user);
                    roleListNewRole = em.merge(roleListNewRole);
                    if (oldUserIdOfRoleListNewRole != null && !oldUserIdOfRoleListNewRole.equals(user)) {
                        oldUserIdOfRoleListNewRole.getRoleList().remove(roleListNewRole);
                        oldUserIdOfRoleListNewRole = em.merge(oldUserIdOfRoleListNewRole);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<Role> roleList = user.getRoleList();
            for (Role roleListRole : roleList) {
                roleListRole.setUserId(null);
                roleListRole = em.merge(roleListRole);
            }
            em.remove(user);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<User> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<User> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<User> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public User find(String username, String password) {
        EntityManager em = getEntityManager();
        try {
            User user = (User) em.createQuery("Select u from User u Where u.username = :username").setParameter("username", username).getSingleResult();
            return user;
        } finally {
            em.close();
        }
    }
    
}
