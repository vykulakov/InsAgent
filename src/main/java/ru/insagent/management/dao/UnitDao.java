package ru.insagent.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.insagent.dao.SimpleDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.City;
import ru.insagent.management.model.Unit;
import ru.insagent.management.model.UnitFilter;
import ru.insagent.management.model.UnitType;
import ru.insagent.model.Filter;
import ru.insagent.model.User;
import ru.insagent.util.JdbcUtils;

public class UnitDao extends SimpleDao<Unit> {
	{
		sortByMap.put("id", "unitId");
		sortByMap.put("type", "typeName");
		sortByMap.put("city", "cityName");
		sortByMap.put("name", "unitName");

		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     m_units o,"
				+ "     m_unit_types t,"
				+ "     m_cities c"
				+ " WHERE"
				+ "     t.id = o.typeId AND"
				+ "     c.id = o.cityId";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     o.id AS unitId,"
				+ "     o.name AS unitName,"
				+ "     o.comment AS unitComment,"
				+ "     o.removed AS unitRemoved,"
				+ "     t.id AS typeId,"
				+ "     t.name AS typeName,"
				+ "     c.id AS cityId,"
				+ "     c.name AS cityName"
				+ " FROM"
				+ "     m_units o,"
				+ "     m_unit_types t,"
				+ "     m_cities c"
				+ " WHERE"
				+ "     t.id = o.typeId AND"
				+ "     c.id = o.cityId";

		insertQuery = ""
				+ " INSERT INTO"
				+ "     m_units"
				+ " SET"
				+ "     typeId = ?,"
				+ "     cityId = ?,"
				+ "     created = NOW(),"
				+ "     name = ?,"
				+ "     comment = ?;";

		updateQuery = ""
				+ " UPDATE"
				+ "     m_units"
				+ " SET"
				+ "     typeId = ?,"
				+ "     cityId = ?,"
				+ "     name = ?,"
				+ "     comment = ?"
				+ " WHERE"
				+ "     id = ?;";

		removeQuery = ""
				+ " UPDATE"
				+ "     m_units"
				+ " SET"
				+ "     removed = 1"
				+ " WHERE"
				+ "     id = ?;";

		idField = "o.id";

		searchCount = 3;
		searchWhere = ""
				+ " o.name LIKE ? OR"
				+ " t.name LIKE ? OR"
				+ " c.name LIKE ?";

		removedWhere = ""
				+ " o.removed = ?";
	}

	/**
	 * Идентификатор типа подразделения "Центральный офис".
	 */
	public static final int CENTRAL_TYPE_ID = 1;

	/**
	 * Идентификатор типа подразделения "Центральный офис филиала".
	 */
	public static final int FILIAL_TYPE_ID = 2;
	
	/**
	 * Идентификатор типа подразделения "Точка продаж".
	 */
	public static final int POINT_TYPE_ID = 3;
	
	public UnitDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<Unit> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	@Override
	public List<Unit> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	@Override
	public List<Unit> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		sb.append("o.id IN (");
		String query = ""
				+ " SELECT"
				+ "     n.id AS unitId"
				+ " FROM"
				+ "     m_units o"
				+ "     LEFT JOIN m_unit_types t1 ON t1.id = o.typeId"
				+ "     LEFT JOIN m_unit_types t2 ON t1.id = t2.parentId"
				+ "     LEFT JOIN m_unit_types t3 ON t2.id = t3.parentId"
				+ "     LEFT JOIN m_units n ON (t1.id = n.typeId OR t2.id = n.typeId OR t3.id = n.typeId) AND IF(t1.id = 1, 1, IF(t1.id = 2, o.cityId = n.cityId, o.id = n.id AND o.cityId = n.cityId)) AND o.removed = 0 AND n.removed = 0"
				+ " WHERE"
				+ "     o.id = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUnit().getId());

			rs = ps.executeQuery();
			while(rs.next()) {
				int unitId = rs.getInt("unitId");
				if(unitId == 0) {
					continue;
				}

				sb.append("?,");
				objects.add(unitId);
			}
		} catch(SQLException e) {
			logger.error("Cannot get unit ids from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения идентификаторов подразделений из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
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

	@Override
	public List<Unit> listByUser(User user, Filter f, String sortBy, String sortDir, int limitRows, int limitOffset) {
		UnitFilter filter = (UnitFilter) f;

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		boolean includeRemoved = false;
		if(filter != null && filter.getRemoved() != null) {
			includeRemoved = filter.getRemoved();
		}

		sb.append("o.id IN (");
		String queryWhere = "";
		if(!includeRemoved) {
			queryWhere = " AND o.removed = 0 AND n.removed = 0";
		}
		String query = ""
				+ " SELECT"
				+ "     n.id AS unitId"
				+ " FROM"
				+ "     m_units o"
				+ "     LEFT JOIN m_unit_types t1 ON t1.id = o.typeId"
				+ "     LEFT JOIN m_unit_types t2 ON t1.id = t2.parentId"
				+ "     LEFT JOIN m_unit_types t3 ON t2.id = t3.parentId"
				+ "     LEFT JOIN m_units n ON (t1.id = n.typeId OR t2.id = n.typeId OR t3.id = n.typeId) AND IF(t1.id = 1, 1, IF(t1.id = 2, o.cityId = n.cityId, o.id = n.id AND o.cityId = n.cityId))" + queryWhere
				+ " WHERE"
				+ "     o.id = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUnit().getId());

			rs = ps.executeQuery();
			while(rs.next()) {
				int unitId = rs.getInt("unitId");
				if(unitId == 0) {
					continue;
				}

				sb.append("?,");
				objects.add(unitId);
			}
		} catch(SQLException e) {
			logger.error("Cannot get unit ids from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения идентификаторов подразделений из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");

		if(filter != null) {
			sb.append(" AND (");

			sb.append("1 = 1");
			if(filter.getName() != null) {
				sb.append(" AND o.name LIKE ?");
				objects.add("%" + filter.getName().replace("*", "%") + "%");
			}
			if(filter.getTypes() != null && !filter.getTypes().isEmpty()) {
				sb.append(" AND t.id IN (");
				for(UnitType type : filter.getTypes()) {
					sb.append("?,");
					objects.add(type.getId());
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			}
			if(filter.getCities() != null && !filter.getCities().isEmpty()) {
				sb.append(" AND c.id IN (");
				for(City city : filter.getCities()) {
					sb.append("?,");
					objects.add(city.getId());
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
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

	public List<Unit> listByTypeIds(Collection<Integer> typeIds) {
		StringBuilder sb = new StringBuilder("o.typeId IN (");
		List<Object> objects = new ArrayList<Object>();

		for(int typeId : typeIds) {
			sb.append("?,");
			objects.add(typeId);
		}

		String where = null;
		if(objects.size() > 0) {
			sb.setLength(sb.length() - 1);
			sb.append(")");

			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(false, where, objects, null, null, 0, 0);
	}

	@Override
	public void update(Unit unit) {
		if(unit.getType().getId() == CENTRAL_TYPE_ID || unit.getType().getId() == FILIAL_TYPE_ID) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				StringBuilder sb = new StringBuilder();
				sb.append(""
						+ " SELECT"
						+ "     COUNT(*) AS count"
						+ " FROM"
						+ "     m_units o"
						+ " WHERE"
						+ "     o.id != ? AND"
						+ "     o.typeId = ? AND");
				if(unit.getType().getId() == FILIAL_TYPE_ID) {
					sb.append("     o.cityId = ? AND");
				}
				sb.append("     o.removed = 0");
				int index = 1;
				ps = conn.prepareStatement(sb.toString());
				ps.setInt(index++, unit.getId());
				ps.setInt(index++, unit.getType().getId());
				if(unit.getType().getId() == FILIAL_TYPE_ID) {
					ps.setInt(index++, unit.getCity().getId());
				}

				rs = ps.executeQuery();
				if(rs.first()) {
					int count = rs.getInt("count");
					if(unit.getType().getId() == CENTRAL_TYPE_ID && count > 0) {
						throw new AppException("Центральный офис уже заведён в системе.");
					}
					if(unit.getType().getId() == FILIAL_TYPE_ID && count > 0) {
						throw new AppException("Центральный офис филиала уже заведён в этом городе.");
					}
				} else {
					throw new AppException("Ошибка при получении количества подразделений из базы данных.");
				}
			} catch(SQLException e) {
				logger.error("Cannot get units count from DB: {}", e.getMessage());
	
				throw new AppException("Ошибка получения количества подразделений из базы данных.", e);
			} finally {
	            JdbcUtils.closeResultSet(rs);
	            JdbcUtils.closeStatement(ps);
			}
		}
		
		super.update(unit);
	}

	@Override
	protected Unit getFromRs(ResultSet rs) throws SQLException {
		UnitType type = new UnitType();
		type.setId(rs.getInt("typeId"));
		type.setName(rs.getString("typeName"));

		City city = new City();
		city.setId(rs.getInt("cityId"));
		city.setName(rs.getString("cityName"));

		Unit unit = new Unit();
		unit.setId(rs.getInt("unitId"));
		unit.setType(type);
		unit.setCity(city);
		unit.setName(rs.getString("unitName"));
		unit.setComment(rs.getString("unitComment"));
		unit.setRemoved(rs.getBoolean("unitRemoved"));

		return unit;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Unit unit) throws SQLException {
		int index = 1;
		ps.setInt(index++, unit.getType().getId());
		ps.setInt(index++, unit.getCity().getId());
		ps.setString(index++, unit.getName());
		ps.setString(index++, unit.getComment());
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Unit unit) throws SQLException {
		int index = 1;
		ps.setInt(index++, unit.getType().getId());
		ps.setInt(index++, unit.getCity().getId());
		ps.setString(index++, unit.getName());
		ps.setString(index++, unit.getComment());
		ps.setInt(index++, unit.getId());
	}
}
