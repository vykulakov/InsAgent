package ru.insagent.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.insagent.exception.AppException;
import ru.insagent.model.IdBase;
import ru.insagent.model.User;
import ru.insagent.model.Filter;
import ru.insagent.util.JdbcUtils;

public abstract class SimpleDao<E extends IdBase> extends BaseDao {
	protected Map<String, String> sortByMap = new HashMap<String, String>();
	protected String countQueryPrefix = null;
	protected String selectQueryPrefix = null;
	protected String selectOrder = null;

	protected String insertQuery = null;
	protected String updateQuery = null;
	protected String removeQuery = null;

	protected String idField = null;

	protected int searchCount = 0;
	protected String searchWhere = null;

	protected String removedWhere = null;

	/**
	 * Количество строк, полученных последним get методом.
	 */
	protected int count;
	
	public SimpleDao(Connection conn) {
		super(conn);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public E getById(int id) {
		if(idField == null) {
			throw new AppException("Поле с идентификатором объекта не задано.");
		}

		if(id == 0) {
			return null;
		}

		List<Object> objects = new ArrayList<Object>();
		objects.add(new Integer(id));

		List<E> result = listByWhere(false, idField + " = ?", objects);
		if(result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}

	public List<E> list() {
		List<E> cities = listByWhere(false, null, null);
		return cities;
	}

	public List<E> list(String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		if(searchWhere == null) {
			throw new AppException("Условие запроса поиска объекта не задано.");
		}

		String where = null;
		List<Object> objects = null;
		if(search != null && !search.trim().isEmpty() && searchWhere != null) {
			search = "%" + search + "%";

			where = searchWhere;
			
			objects = new ArrayList<Object>();
			for(int i = 0; i < searchCount; i++) {
				objects.add(search);
			}
		}

		List<E> result = listByWhere(false, where, objects, sortBy, sortDir, limitRows, limitOffset);
		return result;
	}

	public List<E> listByIds(Collection<Integer> ids) {
		if(idField == null) {
			throw new AppException("Поле с идентификатором объекта не задано.");
		}

		if(ids == null) {
			throw new AppException("Передан null-список.");
		}

		if(ids.isEmpty()) {
			return new ArrayList<E>();
		}

		List<Object> objects = new ArrayList<Object>();
		objects.addAll(ids);

		StringBuilder where = new StringBuilder(idField);
		where.append(" IN (");
		for(int i = 0; i < ids.size(); i++) {
			where.append("?,");
		}
		where.setLength(where.length() - 1);
		where.append(")");

		return listByWhere(false, where.toString(), objects);
	}

	public List<E> listByUser(User user) {
		throw new AppException("Метод не определён");
	}

	public List<E> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		throw new AppException("Метод не определён");
	}

	public List<E> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		throw new AppException("Метод не определён");
	}

	public List<E> listByUser(User user, Filter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		throw new AppException("Метод не определён");
	}

	protected List<E> listByWhere(Boolean includeRemoved, String where, List<Object> objects) throws AppException {
		return listByWhere(includeRemoved, where, objects, null, null, 0, 0);
	}

	protected List<E> listByWhere(Boolean includeRemoved, String where, List<Object> objects, String sortBy, String sortDir, int limitRows, int limitOffset) throws AppException {
		List<E> result = new ArrayList<E>();

		if(countQueryPrefix == null || selectQueryPrefix == null) {
			throw new AppException("Префиксы запросов на получение объектов из базы данных не заданы.");
		}

		if(sortBy != null && !sortByMap.containsKey(sortBy)) {
			throw new AppException("Передано неизвестное поле для сортировки объектов в базе данных.");
		}
		if(sortDir != null && !sortDir.equals("asc") && !sortDir.equals("desc")) {
			throw new AppException("Передано неизвестное направление для сортировки объектов в базе данных.");
		}

		StringBuilder countQuery = new StringBuilder(countQueryPrefix);
		if(includeRemoved != null && removedWhere != null && !includeRemoved) {
			countQuery.append(" AND ");
			countQuery.append(removedWhere);
		}
		if(where != null && !where.isEmpty()) {
			countQuery.append(" AND (");
			countQuery.append(where);
			countQuery.append(")");
		}
		countQuery.append(";");

		StringBuilder selectQuery = new StringBuilder(selectQueryPrefix);
		if(includeRemoved != null && removedWhere != null && !includeRemoved) {
			selectQuery.append(" AND ");
			selectQuery.append(removedWhere);
		}
		if(where != null && !where.isEmpty()) {
			selectQuery.append(" AND (");
			selectQuery.append(where);
			selectQuery.append(")");
		}
		if(sortBy != null && sortDir != null) {
			selectQuery.append(" ORDER BY ");
			selectQuery.append(sortByMap.get(sortBy));
			selectQuery.append(" ");
			selectQuery.append(sortDir);
		} else {
			if(selectOrder != null) {
				selectQuery.append(" ORDER BY ");
				selectQuery.append(selectOrder);
			}
		}
		if(limitRows > 0) {
			selectQuery.append(" LIMIT ");
			if(limitOffset > 0) {
				selectQuery.append(limitOffset);
				selectQuery.append(", ");
			}
			selectQuery.append(limitRows);
		}
		selectQuery.append(";");

		int index = 1;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			ps1 = conn.prepareStatement(countQuery.toString());
			if(includeRemoved != null && removedWhere != null && !includeRemoved) {
				ps1.setBoolean(index++, includeRemoved);
			}
			if(objects != null) {
				for(Object o : objects) {
					ps1.setObject(index++, o);
				}
			}

			rs1 = ps1.executeQuery();
			if(rs1.first()) {
				count = rs1.getInt("count");
			} else {
				throw new AppException("Невозможно получить количество объектов из базы данных.");
			}

			index = 1;
			ps2 = conn.prepareStatement(selectQuery.toString());
			if(includeRemoved != null && removedWhere != null && !includeRemoved) {
				ps2.setBoolean(index++, includeRemoved);
			}
			if(objects != null) {
				for(Object o : objects) {
					ps2.setObject(index++, o);
				}
			}

			rs2 = ps2.executeQuery();
			while(rs2.next()) {
				E o = getFromRs(rs2);

				result.add(o);
			}
		} catch(SQLException e) {
			logger.error("Cannot get objects from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения объектов из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs1);
            JdbcUtils.closeResultSet(rs2);
            JdbcUtils.closeStatement(ps1);
            JdbcUtils.closeStatement(ps2);
		}

		return result;
	}

	public void update(E o) throws AppException {
		if(o == null) {
			throw new AppException("Передан null-объект.");
		}

		if(insertQuery == null || updateQuery == null) {
			throw new AppException("Запросы на добавление/обновление объектов в базе данных не заданы.");
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if(o.getId() > 0) {
				ps = conn.prepareStatement(updateQuery);
				setUpdatePs(ps, o);
				ps.executeUpdate();
			} else {
				ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				setInsertPs(ps, o);
				ps.executeUpdate();

				rs = ps.getGeneratedKeys();
				if(rs.first()) {
					o.setId(rs.getInt(1));
				} else {
					throw new AppException("Ошибка при добавлении объекта в базу данных.");
				}
			}
		} catch(SQLException e) {
			logger.error("Cannot update city in DB: {}", e.getMessage());

			throw new AppException("Ошибка обновления объекта в базе данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}
	}

	public void remove(int id) throws AppException {
		if(id <= 0) {
			throw new AppException("Передан неверный идентификатор объекта.");
		}

		if(removeQuery == null) {
			throw new AppException("Запрос на удаление объекта в базе данных не задан.");
		}

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(removeQuery);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch(SQLException e) {
			logger.error("Cannot remove object from DB: {}", e.getMessage());

			throw new AppException("Ошибка удаления объекта из базы данных.", e);
		} finally {
            JdbcUtils.closeStatement(ps);
		}
	}

	protected abstract E getFromRs(ResultSet rs) throws SQLException;
	protected abstract void setInsertPs(PreparedStatement ps, E o) throws SQLException;
	protected abstract void setUpdatePs(PreparedStatement ps, E o) throws SQLException;
}
