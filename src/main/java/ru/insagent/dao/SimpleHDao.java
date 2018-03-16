/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
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

package ru.insagent.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.insagent.exception.AppException;
import ru.insagent.model.IdBase;
import ru.insagent.util.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple abstract dao for id-based entities
 *
 * @param <E> - id-based entities.
 */
public abstract class SimpleHDao<E extends IdBase> extends BaseHDao {
    protected Map<String, String> sortByMap = new HashMap<>();

    protected String countQueryPrefix = null;
    protected String selectQueryPrefix = null;
    protected String selectOrder = null;

    protected String insertQuery = null;
    protected String updateQuery = null;
    protected String removeQuery = null;

    protected String searchWhere = null;

    protected String removedWhere = null;

    protected Class<E> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    protected Long count;

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return count;
    }

    /**
     * Get entity by id.
     *
     * @param id - entity id.
     * @return Entity with the given id.
     */
    public E get(int id) {
        return entityManager.find(clazz, id);
    }

    /**
     * Get all entities.
     *
     * @return All entities.
     */
    public List<E> list() {
        return listByWhere();
    }

    /**
     * Get entities by ids.
     *
     * @param ids - entity ids.
     * @return Entities with the given ids.
     */
    public List<E> list(List<Integer> ids) {
        return Hibernate.getCurrentSession().byMultipleIds(clazz).multiLoad(ids);
    }

    /**
     * Get entities with quick search, sorting and pagination.
     *
     * @param search      - keyword for quick search, {@code null} to disable quick search,
     * @param sortBy      - field to sort by, {@code null} to disable sorting,
     * @param sortDir     - sort direction,
     * @param limitRows   - max number returned entities, {@code 0} to disable pagination,
     * @param limitOffset - offset of the first entity.
     * @return Filtered, sorted and paginated entities.
     */
    public List<E> list(String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
        if (searchWhere == null) {
            throw new AppException("Условие запроса поиска объекта не задано.");
        }

        String where = null;
        Map<String, Object> parameters = null;
        if (search != null && !search.trim().isEmpty() && searchWhere != null) {
            search = "%" + search + "%";

            where = searchWhere;

            parameters = new HashMap<String, Object>();
            parameters.put("search", search);
        }

        return listByWhere(where, parameters, sortBy, sortDir, limitRows, limitOffset);
    }

    protected List<E> listByWhere() throws AppException {
        return listByWhere(null, null);
    }

    protected List<E> listByWhere(String where, Map<String, Object> parameters) throws AppException {
        return listByWhere(where, parameters, null, null, 0, 0);
    }

    protected List<E> listByWhere(String where, Map<String, Object> parameters, String sortBy, String sortDir, int limitRows, int limitOffset) throws AppException {
        if (sortBy != null && !sortByMap.containsKey(sortBy)) {
            throw new AppException("Passed an unknown sort field");
        }
        if (sortDir != null && !sortDir.equals("asc") && !sortDir.equals("desc")) {
            throw new AppException("Passed an unknown sort direction");
        }

        StringBuilder countQueryConstructed = null;
        if (countQueryPrefix != null) {
            countQueryConstructed = new StringBuilder(countQueryPrefix);
            if (where != null && !where.isEmpty()) {
                countQueryConstructed.append(" AND (");
                countQueryConstructed.append(where);
                countQueryConstructed.append(")");
            }
        }

        StringBuilder selectQueryConstructed = new StringBuilder(selectQueryPrefix);
        if (where != null && !where.isEmpty()) {
            selectQueryConstructed.append(" AND (");
            selectQueryConstructed.append(where);
            selectQueryConstructed.append(")");
        }
        if (sortBy != null && sortDir != null) {
            selectQueryConstructed.append(" ORDER BY ");
            selectQueryConstructed.append(sortByMap.get(sortBy));
            selectQueryConstructed.append(" ");
            selectQueryConstructed.append(sortDir);
        } else {
            if (selectOrder != null) {
                selectQueryConstructed.append(" ORDER BY ");
                selectQueryConstructed.append(selectOrder);
            }
        }

        try {
            if (countQueryConstructed != null) {
                TypedQuery<Long> countQuery = entityManager.createQuery(countQueryConstructed.toString(), Long.class);
                if (parameters != null) {
                    for (Entry<String, Object> entry : parameters.entrySet()) {
                        countQuery.setParameter(entry.getKey(), entry.getValue());
                    }
                }

                count = countQuery.getSingleResult();
            }

            TypedQuery<E> selectQuery = entityManager.createQuery(selectQueryConstructed.toString(), clazz);
            if (parameters != null) {
                for (Entry<String, Object> entry : parameters.entrySet()) {
                    selectQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (limitRows > 0) {
                selectQuery.setFirstResult(limitOffset);
                selectQuery.setMaxResults(limitRows);
            }

            return selectQuery.getResultList();
        } catch (Exception e) {
            throw new AppException("Cannot get object from DB", e);
        }
    }

    /**
     * Add entity
     *
     * @param o - entity to add.
     */
    public void add(E o) {
        entityManager.persist(o);
    }

    /**
     * Update entity
     *
     * @param o - entity to update.
     */
    @Deprecated
    public void update(E o) {
    }

    /**
     * Remove entity by id.
     *
     * @param id - entity id.
     */
    @Deprecated
    public void remove(int id) {
        throw new AppException("Not yet implemented");
    }

    /**
     * Remove entity
     *
     * @param o - entity to remove.
     */
    public void remove(E o) {
        removeImpl(o);
    }

    private void removeImpl(E o) {
        entityManager.remove(o);
    }
}
