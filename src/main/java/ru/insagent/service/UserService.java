/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017-2018 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.insagent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.insagent.dao.MenuItemDao;
import ru.insagent.dao.RoleDao;
import ru.insagent.dao.UnitDao;
import ru.insagent.dao.UserDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.UserFilter;
import ru.insagent.management.user.model.UserDTO;
import ru.insagent.model.*;
import ru.insagent.util.Hibernate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserDao userDao;
    private UnitDao unitDao;
    private RoleDao roleDao;
    private MenuItemDao menuItemDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setMenuItemDao(MenuItemDao menuItemDao) {
        this.menuItemDao = menuItemDao;
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return userDao.getCount();
    }

    public ShiroUser getByUsername(String username) {
        ShiroUser authUser = null;

        Hibernate.beginTransaction();
        try {
            User user = userDao.getByUsername(username);
            if (user != null) {
                authUser = ShiroUser.of(user);
            }

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get user", e);
        }

        return authUser;
    }

    public User get(int id) {
        User user;

        Hibernate.beginTransaction();
        try {
            user = userDao.get(id);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get an user", e);
        }

        return user;
    }

    public List<UserDTO> listByUser(User user, UserFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<UserDTO> users;

        Hibernate.beginTransaction();
        try {
            users = userDao.listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset).stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get users", e);
        }

        return users;
    }

    public void update(User user) {
        Hibernate.beginTransaction();
        try {
            userDao.update(user);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot update user", e);
        }
    }

    public void remove(int userId) {
        Hibernate.beginTransaction();
        try {
            userDao.remove(userId);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot remove user", e);
        }
    }

    public List<Role> roles() {
        List<Role> roles = null;

        Hibernate.beginTransaction();
        try {
            roles = roleDao.list();

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get roles", e);
        }

        return roles;
    }

    public List<Unit> units() {
        List<Unit> units = null;

        Hibernate.beginTransaction();
        try {
            units = unitDao.list();

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get units", e);
        }

        return units;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        Hibernate.beginTransaction();
        try {
            User user = userDao.getByUsername(username);
            if (user != null) {
                Set<String> roles = user.getRoles().stream().map(Role::getIdx).collect(Collectors.toSet());

                List<MenuItem> items = menuItemDao.listByRoleIdxes(roles);
                List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                userDetails = (UserDetails) new SpringUser(user, items, authorities);
            }

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get user", e);
        }

        if (userDetails != null) {
            return userDetails;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
