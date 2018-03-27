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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.insagent.dao.MenuItemDao;
import ru.insagent.dao.RoleDao;
import ru.insagent.dao.UnitDao;
import ru.insagent.dao.UserDao;
import ru.insagent.exception.AppException;
import ru.insagent.exception.NotFoundException;
import ru.insagent.management.user.model.UserDTO;
import ru.insagent.management.user.model.UserFilter;
import ru.insagent.model.*;
import ru.insagent.util.Hibernate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private UserDao userDao;
    private UnitDao unitDao;
    private RoleDao roleDao;
    private MenuItemDao menuItemDao;
    private PasswordEncoder passwordEncoder;

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

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<Role> roles() {
        return roleDao.list();
    }

    public List<Unit> units() {
        return unitDao.list();
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return userDao.getCount();
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

    public User getEditable(int id) {
        User user = userDao.get(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user.makeEditableCopy();
    }

    public List<UserDTO> listByUser(User user, UserFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return userDao
                .listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset)
                .stream().map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public void update(User newUser) {
        Unit unit = unitDao.get(newUser.getUnit().getId());
        if (unit == null) {
            throw new NotFoundException("Unit not found");
        }

        newUser.setUnit(unit);
        if (StringUtils.isNotBlank(newUser.getPassword())) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        if (newUser.getId() == 0) {
            userDao.add(newUser);
        } else {
            User oldUser = userDao.get(newUser.getId());
            if (oldUser == null) {
                throw new NotFoundException("User not found");
            }

            oldUser.setUnit(newUser.getUnit());
            oldUser.setRoles(newUser.getRoles());
            oldUser.setUsername(newUser.getUsername());
            if (StringUtils.isNotBlank(newUser.getPassword())) {
                oldUser.setPassword(newUser.getPassword());
            }
            oldUser.setPassword(newUser.getPassword());
            oldUser.setFirstName(newUser.getFirstName());
            oldUser.setLastName(newUser.getLastName());
            oldUser.setComment(newUser.getComment());
        }
    }

    public void remove(int id) {
        User user = userDao.get(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        user.setRemoved(true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<String> roles = user.getRoles().stream().map(Role::getIdx).collect(Collectors.toSet());

        List<MenuItem> items = menuItemDao.listByRoleIdxes(roles);
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new SpringUser(user, items, authorities);
    }
}
