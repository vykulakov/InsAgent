package ru.insagent.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.insagent.dao.SimpleDao;
import ru.insagent.management.model.Role;

public class RoleDao extends SimpleDao<Role> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     m_roles r"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     r.id AS roleId,"
				+ "     r.idx AS roleIdx,"
				+ "     r.name AS roleName"
				+ " FROM"
				+ "     m_roles r"
				+ " WHERE"
				+ "     1 = 1";

		idField = "r.id";
	}

	public RoleDao(Connection conn) {
		super(conn);
	}

	@Override
	protected Role getFromRs(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setId(rs.getInt("roleId"));
		role.setIdx(rs.getString("roleIdx"));
		role.setName(rs.getString("roleName"));

		return role;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Role o) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Role o) throws SQLException {
	}
}
