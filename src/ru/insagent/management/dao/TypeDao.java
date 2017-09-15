package ru.insagent.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.insagent.dao.SimpleDao;
import ru.insagent.management.model.UnitType;

public class TypeDao extends SimpleDao<UnitType> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     m_unit_types r"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     r.id AS typeId,"
				+ "     r.name AS typeName"
				+ " FROM"
				+ "     m_unit_types r"
				+ " WHERE"
				+ "     1 = 1";

		idField = "t.id";
	}

	public TypeDao(Connection conn) {
		super(conn);
	}

	@Override
	protected UnitType getFromRs(ResultSet rs) throws SQLException {
		UnitType type = new UnitType();
		type.setId(rs.getInt("typeId"));
		type.setName(rs.getString("typeName"));

		return type;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, UnitType o) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, UnitType o) throws SQLException {
	}
}
