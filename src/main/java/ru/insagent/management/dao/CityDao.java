package ru.insagent.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.insagent.dao.SimpleDao;
import ru.insagent.management.model.City;
import ru.insagent.management.model.CityFilter;
import ru.insagent.model.Filter;
import ru.insagent.model.User;

public class CityDao extends SimpleDao<City> {
	{
		sortByMap.put("id", "cityId");
		sortByMap.put("name", "cityName");

		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     m_cities c"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     c.id AS cityId,"
				+ "     c.name AS cityName,"
				+ "     c.comment AS cityComment,"
				+ "     c.removed AS cityRemoved"
				+ " FROM"
				+ "     m_cities c"
				+ " WHERE"
				+ "     1 = 1";

		insertQuery = ""
				+ " INSERT INTO"
				+ "     m_cities"
				+ " SET"
				+ "     created = NOW(),"
				+ "     name = ?,"
				+ "     comment = ?;";

		updateQuery = ""
				+ " UPDATE"
				+ "     m_cities"
				+ " SET"
				+ "     name = ?,"
				+ "     comment = ?"
				+ " WHERE"
				+ "     id = ?;";

		removeQuery = ""
				+ " UPDATE"
				+ "     m_cities"
				+ " SET"
				+ "     removed = 1"
				+ " WHERE"
				+ "     id = ?;";

		idField = "c.id";

		searchCount = 1;
		searchWhere = ""
				+ " c.name LIKE ?";

		removedWhere = ""
				+ " c.removed = ?";
	}

	public CityDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<City> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	@Override
	public List<City> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	@Override
	public List<City> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			sb.append(searchWhere);
			for(int i = 0; i < searchCount; i++) {
				objects.add(search);
			}
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
	public List<City> listByUser(User user, Filter f, String sortBy, String sortDir, int limitRows, int limitOffset) {
		CityFilter filter = (CityFilter) f;

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		boolean includeRemoved = false;
		if(filter != null) {
			sb.append("1 = 1");
			if(filter.getName() != null) {
				sb.append(" AND c.name LIKE ?");
				objects.add("%" + filter.getName().replace("*", "%") + "%");
			}
			if(filter.getRemoved() != null) {
				includeRemoved = filter.getRemoved();
			}
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
	protected City getFromRs(ResultSet rs) throws SQLException {
		City city = new City();
		city.setId(rs.getInt("cityId"));
		city.setName(rs.getString("cityName"));
		city.setComment(rs.getString("cityComment"));
		city.setRemoved(rs.getBoolean("cityRemoved"));

		return city;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, City city) throws SQLException {
		int index = 1;
		ps.setString(index++, city.getName());
		ps.setString(index++, city.getComment());
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, City city) throws SQLException {
		int index = 1;
		ps.setString(index++, city.getName());
		ps.setString(index++, city.getComment());
		ps.setInt(index++, city.getId());
	}
}
