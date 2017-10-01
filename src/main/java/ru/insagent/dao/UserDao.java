package ru.insagent.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.credential.DefaultPasswordService;

import ru.insagent.exception.AppException;
import ru.insagent.management.model.UserFilter;
import ru.insagent.model.Filter;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.util.JdbcUtils;

public class UserDao extends SimpleHDao<User> {
	{
		clazz =  User.class;

		sortByMap.put("id", "userId");
		sortByMap.put("username", "userUsername");
		sortByMap.put("firstName", "userFirstName");
		sortByMap.put("lastName", "userLastName");
		sortByMap.put("lastAuth", "userLastAuth");
		sortByMap.put("unit", "unitName");

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     m_users u,"
			+ "     m_units o,"
			+ "     m_unit_types t,"
			+ "     m_cities c"
			+ " WHERE"
			+ "     o.id = u.unitId AND"
			+ "     t.id = o.typeId AND"
			+ "     c.id = o.cityId";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     u.id AS userId,"
			+ "     u.username AS userUsername,"
			+ "     u.password AS userEncrypt,"
			+ "     u.firstName AS userFirstName,"
			+ "     u.lastName AS userLastName,"
			+ "     u.comment AS userComment,"
			+ "     u.lastIp AS userLastIp,"
			+ "     UNIX_TIMESTAMP(u.lastAuth) AS userLastAuth,"
			+ "     u.removed AS userRemoved,"
			+ "     o.id AS unitId,"
			+ "     o.name AS unitName,"
			+ "     t.id AS typeId,"
			+ "     t.name AS typeName,"
			+ "     c.id AS cityId,"
			+ "     c.name AS cityName"
			+ " FROM"
			+ "     m_users u,"
			+ "     m_units o,"
			+ "     m_unit_types t,"
			+ "     m_cities c"
			+ " WHERE"
			+ "     o.id = u.unitId AND"
			+ "     t.id = o.typeId AND"
			+ "     c.id = o.cityId";

		insertQuery = ""
			+ " INSERT INTO"
			+ "     m_users"
			+ " SET"
			+ "     unitId = ?,"
			+ "     created = NOW(),"
			+ "     username = ?,"
			+ "     password = ?,"
			+ "     firstName = ?,"
			+ "     lastName = ?,"
			+ "     comment = ?;";

		searchWhere = ""
			+ " u.username LIKE ? OR"
			+ " u.firstName LIKE ? OR"
			+ " u.lastName LIKE ? OR"
			+ " o.name LIKE ?";
	}

	public User getByUsername(String username) {
		List<Object> objects = new ArrayList<Object>();
		objects.add(username);

		List<User> users = listByWhere("u.username = ?", objects);
		if(users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
	}

	public List<User> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	public List<User> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<User> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		List<Unit> units = new UnitDao(conn).listByUser(user);

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		sb.append("u.unitId IN (");
		for(Unit unit : units) {
			sb.append("?,");
			objects.add(unit.getId());
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			sb.append(" AND (");
			sb.append(searchWhere);
			for(int i = 0; i < searchCount; i++) {
				objects.add(search);
			}
			sb.append(")");
		}

		String where = null;
		if(objects.size() > 0) {
			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(false, where, objects, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<User> listByUser(User user, Filter f, String sortBy, String sortDir, int limitRows, int limitOffset) {
		UserFilter filter = (UserFilter) f;

		List<Unit> units = new UnitDao(conn).listByUser(user);

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		sb.append("u.unitId IN (");
		for(Unit unit : units) {
			sb.append("?,");
			objects.add(unit.getId());
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");

		boolean includeRemoved = false;
		if(filter != null) {
			sb.append(" AND (");

			sb.append("1 = 1");
			if(filter.getLogin() != null) {
				sb.append(" AND u.username LIKE ?");
				objects.add("%" + filter.getLogin().replace("*", "%") + "%");
			}
			if(filter.getName() != null) {
				sb.append(" AND CONCAT(u.firstName, ' ', u.lastName) LIKE ? OR CONCAT(u.lastName, ' ', u.firstName) LIKE ?");
				objects.add("%" + filter.getName().replace("*", "%") + "%");
				objects.add("%" + filter.getName().replace("*", "%") + "%");
			}
			if(filter.getUnits() != null && !filter.getUnits().isEmpty()) {
				sb.append(" AND u.unitId IN (");
				for(Unit unit : filter.getUnits()) {
					sb.append("?,");
					objects.add(unit.getId());
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			}
			if(filter.getRemoved() != null) {
				includeRemoved = filter.getRemoved();
			}

			sb.append(")");
		}

		String where = null;
		if(objects.size() > 0) {
			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(includeRemoved, where, objects, sortBy, sortDir, limitRows, limitOffset);
	}

	@Override
	public void update(User user) throws AppException {
		if(user == null) {
			throw new AppException("Передан null-пользователь.");
		}

		DefaultPasswordService passwordService = new DefaultPasswordService();
		if(user.getPassword() != null && !user.getPassword().isEmpty()) {
			user.setPassword(passwordService.encryptPassword(user.getPassword()));
		}

		if(user.getId() > 0) {
			StringBuilder query = new StringBuilder(updateQuery);
			query.append(""
				+ " UPDATE"
				+ "     m_users"
				+ " SET"
				+ "     unitId = ?,"
				+ "     username = ?,");
			if(user.getPassword() != null && !user.getPassword().isEmpty()) {
				query.append("     password = ?,");
			}
			query.append(""
				+ "     firstName = ?,"
				+ "     lastName = ?,"
				+ "     comment = ?"
				+ " WHERE"
				+ "     id = ?;");

			updateQuery = query.toString();
		}

		super.update(user);

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(""
				+ " DELETE FROM"
				+ "     m_user_roles"
				+ " WHERE"
				+ "     userId = ?;");
			ps.setInt(1, user.getId());
			ps.executeUpdate();
		} catch(SQLException e) {
			logger.error("Cannot delete user roles from DB: {}", e.getMessage());

			throw new AppException("Ошибка удаления ролей пользователя из базы данных.", e);
		} finally {
			JdbcUtils.closeStatement(ps);
		}

		try {
			ps = conn.prepareStatement(""
				+ " INSERT INTO"
				+ "     m_user_roles"
				+ " SELECT"
				+ "     ?,"
				+ "     id"
				+ " FROM"
				+ "     m_roles"
				+ " WHERE"
				+ "     idx = ?;");
			for(String role : user.getRoles()) {
				ps.setInt(1, user.getId());
				ps.setString(2, role);
				ps.executeUpdate();
			}
		} catch(SQLException e) {
			logger.error("Cannot insert user roles in DB: {}", e.getMessage());

			throw new AppException("Ошибка добавления ролей пользователя в базу данных.", e);
		} finally {
			JdbcUtils.closeStatement(ps);
		}
	}

	public Set<String> getRolesByUserId(int userId) throws AppException {
		Set<String> roles = new HashSet<String>();

		String query = ""
			+ " SELECT"
			+ "     r.id AS roleId,"
			+ "     r.idx AS roleIdx,"
			+ "     r.name AS roleName"
			+ " FROM"
			+ "     m_roles r,"
			+ "     m_user_roles u"
			+ " WHERE"
			+ "     r.id = u.roleId AND"
			+ "     u.userId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, userId);

			rs = ps.executeQuery();
			while(rs.next()) {
				roles.add(rs.getString("roleIdx"));
			}
		} catch(SQLException e) {
			logger.error("Cannot get user roles from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения ролей пользователя из базы данных.", e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}

		return roles;
	}
}
