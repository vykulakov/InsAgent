package ru.insagent.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.insagent.dao.SimpleDao;
import ru.insagent.document.model.ActType;

public class ActTypeDao extends SimpleDao<ActType> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     d_act_types t"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     t.id AS actTypeId,"
				+ "     t.idx AS actTypeIdx,"
				+ "     t.shortName AS actTypeShortName,"
				+ "     t.fullName AS actTypeFullName"
				+ " FROM"
				+ "     d_act_types t"
				+ " WHERE"
				+ "     1 = 1";

		idField = "t.id";
	}

	public ActTypeDao(Connection conn) {
		super(conn);
	}

	@Override
	protected ActType getFromRs(ResultSet rs) throws SQLException {
		ActType actType = new ActType();
		actType.setId(rs.getInt("actTypeId"));
		actType.setIdx(rs.getString("actTypeIdx"));
		actType.setShortName(rs.getString("actTypeShortName"));
		actType.setFullName(rs.getString("actTypeFullName"));

		return actType;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, ActType actType) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, ActType actType) throws SQLException {
	}
}
