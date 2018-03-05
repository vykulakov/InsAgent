/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
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
import ru.insagent.dao.RoleDao;
import ru.insagent.dao.UnitDao;
import ru.insagent.dao.UserDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.UserFilter;
import ru.insagent.model.Role;
import ru.insagent.model.ShiroUser;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.util.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    final private UnitDao unitDao = new UnitDao();

    @Autowired
    private UserDao userDao;

    final private RoleDao roleDao = new RoleDao();

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

    public List<User> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<User> users;

        Hibernate.beginTransaction();
        try {
            users = userDao.listByUser(user, search, sortBy, sortDir, limitRows, limitOffset);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get users", e);
        }

        return users;
    }

    public List<User> listByUser(User user, UserFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<User> users;

        Hibernate.beginTransaction();
        try {
            users = userDao.listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset);

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
        return roleDao.list();
    }

    public List<Unit> units() {
        return unitDao.list();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        Hibernate.beginTransaction();
        try {
            User user = userDao.getByUsername(username);
            if (user != null) {
                user.getRoles().stream().map(Role::getIdx).forEach(System.out::println);
                List<GrantedAuthority> authorities = user.getRoles().stream().map(Role::getIdx).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
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
