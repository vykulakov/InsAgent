package ru.insagent.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.insagent.dao.SimpleDao;
import ru.insagent.document.model.ActPack;

public class ActPackDao extends SimpleDao<ActPack> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     d_act_packs p"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     p.id AS packId,"
				+ "     p.series AS packSeries,"
				+ "     p.numberFrom AS packNumberFrom,"
				+ "     p.numberTo AS packNumberTo,"
				+ "     p.amount AS packAmount"
				+ " FROM"
				+ "     d_act_packs p"
				+ " WHERE"
				+ "     1 = 1";

		idField = "p.id";
	}

	public ActPackDao(Connection conn) {
		super(conn);
	}


	public List<ActPack> listByActId(int id) {
		String where = "p.actId = ?";
		List<Object> objects = new ArrayList<Object>();
		objects.add(id);

		return listByWhere(false, where, objects, null, null, 0, 0);
	}

	@Override
	protected ActPack getFromRs(ResultSet rs) throws SQLException {
		ActPack actPack = new ActPack();
		actPack.setId(rs.getInt("packId"));
		actPack.setSeries(rs.getString("packSeries"));
		actPack.setNumberFrom(rs.getLong("packNumberFrom"));
		actPack.setNumberTo(rs.getLong("packNumberTo"));
		actPack.setAmount(rs.getInt("packAmount"));

		return actPack;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, ActPack actPack) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, ActPack actPack) throws SQLException {
	}
}
