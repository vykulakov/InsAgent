package ru.insagent.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ru.insagent.dao.SimpleDao;
import ru.insagent.dao.UnitDao;
import ru.insagent.document.model.Act;
import ru.insagent.document.model.ActPack;
import ru.insagent.document.model.ActType;
import ru.insagent.exception.AppException;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.util.TimeUtils;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

public class ActDao extends SimpleDao<Act> {
	{
		sortByMap.put("created", "actCreated");
		sortByMap.put("type", "actTypeFullName");
		sortByMap.put("nodeFrom", "nodeFromName");
		sortByMap.put("nodeTo", "nodeToName");
		sortByMap.put("unitFrom", "unitFromName");
		sortByMap.put("unitTo", "unitToName");
		sortByMap.put("amount", "actAmount");

		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     d_acts a"
				+ "     LEFT JOIN m_units uf ON uf.id = a.unitFromId"
				+ "     LEFT JOIN m_units ut ON ut.id = a.unitToId,"
				+ "     d_act_types t"
				+ " WHERE"
				+ "     t.id = a.typeId";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     a.id AS actId,"
				+ "     UNIX_TIMESTAMP(a.created) AS actCreated,"
				+ "     a.amount AS actAmount,"
				+ "     a.comment AS actComment,"
				+ "     nf.id AS nodeFromId,"
				+ "     nf.name AS nodeFromName,"
				+ "     nt.id AS nodeToId,"
				+ "     nt.name AS nodeToName,"
				+ "     uf.id AS unitFromId,"
				+ "     uf.name AS unitFromName,"
				+ "     ut.id AS unitToId,"
				+ "     ut.name AS unitToName,"
				+ "     t.id AS actTypeId,"
				+ "     t.idx AS actTypeIdx,"
				+ "     t.shortName AS actTypeShortName,"
				+ "     t.fullName AS actTypeFullName"
				+ " FROM"
				+ "     d_acts a"
				+ "     LEFT JOIN w_nodes nf ON nf.id = a.nodeFromId"
				+ "     LEFT JOIN w_nodes nt ON nt.id = a.nodeToId"
				+ "     LEFT JOIN m_units uf ON uf.id = a.unitFromId"
				+ "     LEFT JOIN m_units ut ON ut.id = a.unitToId,"
				+ "     d_act_types t"
				+ " WHERE"
				+ "     t.id = a.typeId";

		insertQuery = ""
				+ " INSERT INTO"
				+ "     d_acts"
				+ " SET"
				+ "     typeId = ?,"
				+ "     created = NOW(),"
				+ "     nodeFromId = ?,"
				+ "     nodeToId = ?,"
				+ "     unitFromId = ?,"
				+ "     unitToId = ?,"
				+ "     amount = ?,"
				+ "     comment = ?;";

		updateQuery = ""
				+ " UPDATE"
				+ "     d_acts"
				+ " SET"
				+ "     comment = ?"
				+ " WHERE"
				+ "     id = ?;";

		removeQuery = null;

		idField = "a.id";

		searchCount = 0;
		searchWhere = null;

		removedWhere = null;
	}

	public ActDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<Act> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		List<Link> links = new LinkDao(conn).listByUser(user);
		List<Unit> units = new UnitDao(conn).listByUser(user);

		StringBuilder sb = new StringBuilder("");
		List<Object> objects = new ArrayList<Object>();

		sb.append("(");

		sb.append("a.nodeFromId IN (");
		for(Link link : links) {
			sb.append("?,");
			objects.add(link.getNodeFrom().getId());
		}
		sb.append("-1)");

		sb.append(" AND ");

		sb.append("(a.unitFromId IS NULL OR a.unitFromId IN (");
		for(Unit unit : units) {
			sb.append("?,");
			objects.add(unit.getId());
		}
		sb.append("-1))");

		sb.append(") OR (");

		sb.append("a.nodeToId IN (");
		for(Link link : links) {
			sb.append("?,");
			objects.add(link.getNodeTo().getId());
		}
		sb.append("-1)");

		sb.append(" AND ");

		sb.append("(a.unitToId IS NULL OR a.unitToId IN (");
		for(Unit unit : units) {
			sb.append("?,");
			objects.add(unit.getId());
		}
		sb.append("-1))");

		sb.append(")");

		return listByWhere(false, sb.toString(), objects, sortBy, sortDir, limitRows, limitOffset);
	}

	@Override
	public void update(Act act) throws AppException {
		if(act == null) {
			throw new AppException("Передан null-акт.");
		}

		boolean insert = (act.getId() == 0);

		super.update(act);

		if(insert) {
			PreparedStatement ps1 = null;
			PreparedStatement ps2 = null;
			ResultSet rs1 = null;
			try {
				ps1 = conn.prepareStatement(""
						+ " INSERT INTO"
						+ "     d_act_packs"
						+ " SET"
						+ "     actId = ?,"
						+ "     series = ?,"
						+ "     numberFrom = ?,"
						+ "     numberTo = ?,"
						+ "     amount = ?;");

				ps2 = conn.prepareStatement(""
						+ " INSERT INTO"
						+ "     d_bsos"
						+ " SET"
						+ "     nodeId = ?,"
						+ "     unitId = ?,"
						+ "     created = NOW(),"
						+ "     series = ?,"
						+ "     number = ?"
						+ " ON DUPLICATE KEY UPDATE"
						+ "     nodeId = ?,"
						+ "     unitId = ?;");

				int index = 1;
				for(ActPack pack : act.getPacks()) {
					index = 1;
					ps1.setInt(index++, act.getId());
					ps1.setString(index++, pack.getSeries());
					ps1.setLong(index++, pack.getNumberFrom());
					ps1.setLong(index++, pack.getNumberTo());
					ps1.setInt(index++, pack.getAmount());
					ps1.executeUpdate();

					for(long number = pack.getNumberFrom(); number <= pack.getNumberTo(); number++) {
						index = 1;
						ps2.setInt(index++, act.getNodeTo().getId());
						if(act.getUnitTo() == null) {
							ps2.setNull(index++, Types.INTEGER);
						} else {
							ps2.setInt(index++, act.getUnitTo().getId());
						}
						ps2.setString(index++, pack.getSeries());
						ps2.setLong(index++, number);
						ps2.setInt(index++, act.getNodeTo().getId());
						if(act.getUnitTo() == null) {
							ps2.setNull(index++, Types.INTEGER);
						} else {
							ps2.setInt(index++, act.getUnitTo().getId());
						}
						ps2.executeUpdate();
					}
				}
			} catch(SQLException e) {
				logger.error("Cannot update packs or bsos in DB: {}", e.getMessage());
	
				throw new AppException("Ошибка изменения пачек БСО или самих БСО в базе данных.", e);
			} finally {
	            JdbcUtils.closeResultSet(rs1);
	            JdbcUtils.closeStatement(ps1);
	            JdbcUtils.closeStatement(ps2);
			}
		}

		if(insert && act.getType().getId() == 3) {
			PreparedStatement ps1 = null;
			PreparedStatement ps2 = null;
			try {
				ps1 = conn.prepareStatement(""
						+ " INSERT INTO"
						+ "     d_bsos_archived"
						+ " SELECT"
						+ "     *"
						+ " FROM"
						+ "     d_bsos"
						+ " WHERE"
						+ "     series = ? AND"
						+ "     ? <= number AND number <= ?;");

				ps2 = conn.prepareStatement(""
						+ " DELETE FROM"
						+ "     d_bsos"
						+ " WHERE"
						+ "     series = ? AND"
						+ "     ? <= number AND number <= ?");

				int index = 1;
				for(ActPack pack : act.getPacks()) {
					index = 1;
					ps1.setString(index++, pack.getSeries());
					ps1.setLong(index++, pack.getNumberFrom());
					ps1.setLong(index++, pack.getNumberTo());
					ps1.executeUpdate();

					index = 1;
					ps2.setString(index++, pack.getSeries());
					ps2.setLong(index++, pack.getNumberFrom());
					ps2.setLong(index++, pack.getNumberTo());
					ps2.executeUpdate();
				}
			} catch(SQLException e) {
				logger.error("Cannot archive bsos in DB: {}", e.getMessage());
	
				throw new AppException("Ошибка архивирования БСО в базе данных.", e);
			} finally {
	            JdbcUtils.closeStatement(ps1);
	            JdbcUtils.closeStatement(ps2);
			}
		}
	}

	@Override
	protected Act getFromRs(ResultSet rs) throws SQLException {
		ActType actType = new ActType();
		actType.setId(rs.getInt("actTypeId"));
		actType.setIdx(rs.getString("actTypeIdx"));
		actType.setShortName(rs.getString("actTypeShortName"));
		actType.setFullName(rs.getString("actTypeFullName"));

		Node nodeFrom = new Node();
		nodeFrom.setId(rs.getInt("nodeFromId"));
		nodeFrom.setName(rs.getString("nodeFromName"));

		Node nodeTo = new Node();
		nodeTo.setId(rs.getInt("nodeToId"));
		nodeTo.setName(rs.getString("nodeToName"));

		Unit unitFrom = new Unit();
		unitFrom.setId(rs.getInt("unitFromId"));
		unitFrom.setName(rs.getString("unitFromName"));

		Unit unitTo = new Unit();
		unitTo.setId(rs.getInt("unitToId"));
		unitTo.setName(rs.getString("unitToName"));

		Act act = new Act();
		act.setId(rs.getInt("actId"));
		act.setType(actType);
		act.setCreated(TimeUtils.convertTimestampToDate(rs.getLong("actCreated")));
		act.setNodeFrom(nodeFrom);
		act.setNodeTo(nodeTo);
		act.setUnitFrom(unitFrom);
		act.setUnitTo(unitTo);
		act.setAmount(rs.getInt("actAmount"));
		act.setComment(rs.getString("actComment"));

		return act;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Act act) throws SQLException {
		int index = 1;
		ps.setInt(index++, act.getType().getId());
		if(act.getNodeFrom() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, act.getNodeFrom().getId());
		}
		if(act.getNodeTo() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, act.getNodeTo().getId());
		}
		if(act.getUnitFrom() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, act.getUnitFrom().getId());
		}
		if(act.getUnitTo() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, act.getUnitTo().getId());
		}
		ps.setInt(index++, act.getAmount());
		ps.setString(index++, act.getComment());
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Act act) throws SQLException {
		int index = 1;
		ps.setString(index++, act.getComment());
		ps.setInt(index++, act.getId());
	}
}
