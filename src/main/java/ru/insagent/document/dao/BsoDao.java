package ru.insagent.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ru.insagent.dao.SimpleDao;
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.management.dao.UnitDao;
import ru.insagent.management.model.City;
import ru.insagent.management.model.Unit;
import ru.insagent.management.model.UnitType;
import ru.insagent.model.Filter;
import ru.insagent.model.User;
import ru.insagent.util.TimeUtils;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

public class BsoDao extends SimpleDao<Bso> {
	{
		sortByMap.put("created", "bsoCreated");
		sortByMap.put("series", "bsoSeries");
		sortByMap.put("number", "bsoNumber");
		sortByMap.put("issuedDate", "bsoIssuedDate");
		sortByMap.put("issuedBy", "CONCAT(bsoIssuedUserFirstName,bsoIssuedUserLastName)");
		sortByMap.put("issuedUnit", "bsoIssuedUnitName");
		sortByMap.put("corruptedDate", "bsoCorruptedDate");
		sortByMap.put("corruptedBy", "CONCAT(bsoCorruptedUserFirstName,bsoCorruptedUserLastName)");
		sortByMap.put("corruptedUnit", "bsoCorruptedUnitName");
		sortByMap.put("corruptedDate", "bsoCorruptedDate");
		sortByMap.put("registeredBy", "CONCAT(bsoRegisteredUserFirstName,bsoRegisteredUserLastName)");
		sortByMap.put("registeredUnit", "bsoRegisteredUnitName");
		sortByMap.put("insured", "bsoInsured");
		sortByMap.put("premium", "bsoPremium");
		sortByMap.put("node", "nodeName");
		sortByMap.put("unit", "unitName");

		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     d_bsos b"
				+ "     LEFT JOIN m_users ui ON ui.id = b.issuedBy"
				+ "     LEFT JOIN m_users uc ON uc.id = b.corruptedBy"
				+ "     LEFT JOIN m_users ur ON ur.id = b.registeredBy"
				+ "     LEFT JOIN m_units oi ON oi.id = b.issuedUnitId"
				+ "     LEFT JOIN m_units oc ON oc.id = b.corruptedUnitId"
				+ "     LEFT JOIN m_units ox ON ox.id = b.registeredUnitId"
				+ "     LEFT JOIN m_units o ON o.id = b.unitId"
				+ "     LEFT JOIN m_unit_types t ON t.id = o.typeId"
				+ "     LEFT JOIN m_cities c ON c.id = o.cityId,"
				+ "     w_nodes n"
				+ " WHERE"
				+ "     n.id = b.nodeId";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     b.id AS bsoId,"
				+ "     UNIX_TIMESTAMP(b.created) AS bsoCreated,"
				+ "     b.series AS bsoSeries,"
				+ "     b.number AS bsoNumber,"
				+ "     b.issued AS bsoIssued,"
				+ "     UNIX_TIMESTAMP(b.issuedDate) AS bsoIssuedDate,"
				+ "     ui.id AS bsoIssuedUserId,"
				+ "     ui.firstName AS bsoIssuedUserFirstName,"
				+ "     ui.lastName AS bsoIssuedUserLastName,"
				+ "     oi.id AS bsoIssuedUnitId,"
				+ "     oi.name AS bsoIssuedUnitName,"
				+ "     b.corrupted AS bsoCorrupted,"
				+ "     UNIX_TIMESTAMP(b.corruptedDate) AS bsoCorruptedDate,"
				+ "     uc.id AS bsoCorruptedUserId,"
				+ "     uc.firstName AS bsoCorruptedUserFirstName,"
				+ "     uc.lastName AS bsoCorruptedUserLastName,"
				+ "     oc.id AS bsoCorruptedUnitId,"
				+ "     oc.name AS bsoCorruptedUnitName,"
				+ "     b.registered AS bsoRegistered,"
				+ "     UNIX_TIMESTAMP(b.registeredDate) AS bsoRegisteredDate,"
				+ "     ur.id AS bsoRegisteredUserId,"
				+ "     ur.firstName AS bsoRegisteredUserFirstName,"
				+ "     ur.lastName AS bsoRegisteredUserLastName,"
				+ "     ox.id AS bsoRegisteredUnitId,"
				+ "     ox.name AS bsoRegisteredUnitName,"
				+ "     b.insured AS bsoInsured,"
				+ "     b.premium AS bsoPremium,"
				+ "     o.id AS unitId,"
				+ "     o.name AS unitName,"
				+ "     t.id AS unitTypeId,"
				+ "     t.name AS unitTypeName,"
				+ "     c.id AS unitCityId,"
				+ "     c.name AS unitCityName,"
				+ "     n.id AS nodeId,"
				+ "     n.name AS nodeName"
				+ " FROM"
				+ "     d_bsos b"
				+ "     LEFT JOIN m_users ui ON ui.id = b.issuedBy"
				+ "     LEFT JOIN m_users uc ON uc.id = b.corruptedBy"
				+ "     LEFT JOIN m_users ur ON ur.id = b.registeredBy"
				+ "     LEFT JOIN m_units oi ON oi.id = b.issuedUnitId"
				+ "     LEFT JOIN m_units oc ON oc.id = b.corruptedUnitId"
				+ "     LEFT JOIN m_units ox ON ox.id = b.registeredUnitId"
				+ "     LEFT JOIN m_units o ON o.id = b.unitId"
				+ "     LEFT JOIN m_unit_types t ON t.id = o.typeId"
				+ "     LEFT JOIN m_cities c ON c.id = o.cityId,"
				+ "     w_nodes n"
				+ " WHERE"
				+ "     n.id = b.nodeId";

		insertQuery = "";

		updateQuery = ""
				+ " UPDATE"
				+ "     d_bsos"
				+ " SET"
				+ "     issued = ?,"
				+ "     issuedDate = FROM_UNIXTIME(?),"
				+ "     issuedBy = ?,"
				+ "     issuedUnitId = ?,"
				+ "     corrupted = ?,"
				+ "     corruptedDate = FROM_UNIXTIME(?),"
				+ "     corruptedBy = ?,"
				+ "     corruptedUnitId = ?,"
				+ "     registered = ?,"
				+ "     registeredDate = FROM_UNIXTIME(?),"
				+ "     registeredBy = ?,"
				+ "     registeredUnitId = ?,"
				+ "     insured = ?,"
				+ "     premium = ?"
				+ " WHERE"
				+ "     id = ?";

		idField = "b.id";

		searchCount = 5;
		searchWhere = ""
				+ " b.series LIKE ? OR"
				+ " b.number LIKE ? OR"
				+ " n.name LIKE ? OR"
				+ " o.name LIKE ? OR"
				+ " b.insured LIKE ?";
	}

	public BsoDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<Bso> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	@Override
	public List<Bso> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	@Override
	public List<Bso> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		List<Unit> units = new UnitDao(conn).listByUser(user);
		List<Link> links = new LinkDao(conn).listByUser(user);

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		if(!units.isEmpty()) {
			sb.append("(n.nodeTypeId = 4 AND o.id IS NOT NULL AND o.id IN (");
			for(Unit unit : units) {
				sb.append("?,");
				objects.add(unit.getId());
			}
			sb.setLength(sb.length() - 1);
			sb.append("))");
		}

		if(!units.isEmpty() && !links.isEmpty()) {
			sb.append(" OR ");
		}

		if(!links.isEmpty()) {
			sb.append("(n.nodeTypeId != 4 AND o.id IS NULL AND n.id IN (");
			for(Link link : links) {
				sb.append("?,");
				objects.add(link.getNodeFrom().getId());
				sb.append("?,");
				objects.add(link.getNodeTo().getId());
			}
			sb.setLength(sb.length() - 1);
			sb.append("))");
		}

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			if(objects.size() > 0) {
				sb.insert(0, "(");
				sb.append(") AND (");
			}
			sb.append(searchWhere);
			if(objects.size() > 0) {
				sb.append(")");
			}
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
	public List<Bso> listByUser(User user, Filter f, String sortBy, String sortDir, int limitRows, int limitOffset) {
		BsoFilter filter = (BsoFilter) f;

		List<Unit> units = new UnitDao(conn).listByUser(user);
		List<Link> links = new LinkDao(conn).listByUser(user);

		StringBuilder sb = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		sb.append("(n.nodeTypeId = 4 AND o.id IS NOT NULL AND o.id IN (");
		if(!units.isEmpty()) {
			for(Unit unit : units) {
				sb.append("?,");
				objects.add(unit.getId());
			}
			sb.setLength(sb.length() - 1);
		} else {
			sb.append("-1");
		}
		sb.append("))");

		sb.append(" OR ");

		sb.append("(n.nodeTypeId != 4 AND o.id IS NULL AND n.id IN (");
		if(!links.isEmpty()) {
			for(Link link : links) {
				sb.append("?,");
				objects.add(link.getNodeFrom().getId());
				sb.append("?,");
				objects.add(link.getNodeTo().getId());
			}
			sb.setLength(sb.length() - 1);
		} else {
			sb.append("-1");
		}
		sb.append("))");

		if(filter != null) {
			sb.insert(0, "(");
			sb.append(") AND (");

			sb.append("1 = 1");
			if(filter.getSeries() != null) {
				sb.append(" AND b.series LIKE ?");
				objects.add(filter.getSeries().replace("*", "%"));
			}
			if(filter.getNumberFrom() != null) {
				sb.append(" AND ? <= b.number");
				objects.add(filter.getNumberFrom());
			}
			if(filter.getNumberTo() != null) {
				sb.append(" AND b.number <= ?");
				objects.add(filter.getNumberTo());
			}
			if(filter.getInsured() != null) {
				sb.append(" AND b.insured LIKE ?");
				objects.add(filter.getInsured().replace("*", "%"));
			}
			if(filter.getPremiumFrom() != null) {
				sb.append(" AND ? <= b.premium");
				objects.add(filter.getPremiumFrom());
			}
			if(filter.getPremiumTo() != null) {
				sb.append(" AND b.premium <= ?");
				objects.add(filter.getPremiumTo());
			}
			if(filter.getNodes() != null && !filter.getNodes().isEmpty()) {
				sb.append(" AND b.nodeId IN (");
				for(Node node : filter.getNodes()) {
					sb.append("?,");
					objects.add(node.getId());
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			}
			if(filter.getUnits() != null && !filter.getUnits().isEmpty()) {
				sb.append(" AND b.unitId IN (");
				for(Unit unit : filter.getUnits()) {
					sb.append("?,");
					objects.add(unit.getId());
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			}
			if(filter.getCreatedFrom() != null) {
				sb.append(" AND ? <= UNIX_TIMESTAMP(b.created)");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getCreatedFrom()));
			}
			if(filter.getCreatedTo() != null) {
				sb.append(" AND UNIX_TIMESTAMP(b.created) <= ?");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getCreatedTo()));
			}
			if(filter.getIssuedFrom() != null) {
				sb.append(" AND ? <= UNIX_TIMESTAMP(b.issuedDate)");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getIssuedFrom()));
			}
			if(filter.getIssuedTo() != null) {
				sb.append(" AND UNIX_TIMESTAMP(b.issuedDate) <= ?");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getIssuedTo()));
			}
			if(filter.getCorruptedFrom() != null) {
				sb.append(" AND ? <= UNIX_TIMESTAMP(b.corruptedDate)");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getCorruptedFrom()));
			}
			if(filter.getCorruptedTo() != null) {
				sb.append(" AND UNIX_TIMESTAMP(b.corruptedDate) <= ?");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getCorruptedTo()));
			}
			if(filter.getRegisteredFrom() != null) {
				sb.append(" AND ? <= UNIX_TIMESTAMP(b.registeredDate)");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getRegisteredFrom()));
			}
			if(filter.getRegisteredTo() != null) {
				sb.append(" AND UNIX_TIMESTAMP(b.registeredDate) <= ?");
				objects.add(TimeUtils.convertDateToTimestamp(filter.getRegisteredTo()));
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

	public List<Bso> listBySeriesAndNumbers(String series, long numberFrom, long numberTo) {
		String where = "b.series = ? AND ? <= b.number AND b.number <= ?";
		List<Object> objects = new ArrayList<Object>();
		objects.add(series);
		objects.add(numberFrom);
		objects.add(numberTo);

		return listByWhere(false, where, objects);
	}

	@Override
	protected Bso getFromRs(ResultSet rs) throws SQLException {
		Node node = new Node();
		node.setId(rs.getInt("nodeId"));
		node.setName(rs.getString("nodeName"));
		
		Unit unit = null;
		if(rs.getInt("unitId") > 0) {
			UnitType type = new UnitType();
			type.setId(rs.getInt("unitTypeId"));
			type.setName(rs.getString("unitTypeName"));

			City city = new City();
			city.setId(rs.getInt("unitCityId"));
			city.setName(rs.getString("unitCityName"));

			unit = new Unit();
			unit.setId(rs.getInt("unitId"));
			unit.setType(type);
			unit.setCity(city);
			unit.setName(rs.getString("unitName"));
		}

		User issuedUser = null;
		if(rs.getInt("bsoIssuedUserId") > 0) {
			issuedUser = new User();
			issuedUser.setId(rs.getInt("bsoIssuedUserId"));
			issuedUser.setFirstName(rs.getString("bsoIssuedUserFirstName"));
			issuedUser.setLastName(rs.getString("bsoIssuedUserLastName"));
		}

		Unit issuedUnit = null;
		if(rs.getInt("bsoIssuedUnitId") > 0) {
			issuedUnit = new Unit();
			issuedUnit.setId(rs.getInt("bsoIssuedUnitId"));
			issuedUnit.setName(rs.getString("bsoIssuedUnitName"));
		}

		User corruptedUser = null;
		if(rs.getInt("bsoCorruptedUserId") > 0) {
			corruptedUser = new User();
			corruptedUser.setId(rs.getInt("bsoCorruptedUserId"));
			corruptedUser.setFirstName(rs.getString("bsoCorruptedUserFirstName"));
			corruptedUser.setLastName(rs.getString("bsoCorruptedUserLastName"));
		}

		Unit corruptedUnit = null;
		if(rs.getInt("bsoCorruptedUnitId") > 0) {
			corruptedUnit = new Unit();
			corruptedUnit.setId(rs.getInt("bsoCorruptedUnitId"));
			corruptedUnit.setName(rs.getString("bsoCorruptedUnitName"));
		}

		User registeredUser = null;
		if(rs.getInt("bsoRegisteredUserId") > 0) {
			registeredUser = new User();
			registeredUser.setId(rs.getInt("bsoRegisteredUserId"));
			registeredUser.setFirstName(rs.getString("bsoRegisteredUserFirstName"));
			registeredUser.setLastName(rs.getString("bsoRegisteredUserLastName"));
		}
		
		Unit registeredUnit = null;
		if(rs.getInt("bsoRegisteredUnitId") > 0) {
			registeredUnit = new Unit();
			registeredUnit.setId(rs.getInt("bsoRegisteredUnitId"));
			registeredUnit.setName(rs.getString("bsoRegisteredUnitName"));
		}
		
		Bso bso = new Bso();
		bso.setId(rs.getInt("bsoId"));
		bso.setCreated(TimeUtils.convertTimestampToDate(rs.getLong("bsoCreated")));
		bso.setSeries(rs.getString("bsoSeries"));
		bso.setNumber(rs.getLong("bsoNumber"));
		bso.setIssued(rs.getBoolean("bsoIssued"));
		bso.setIssuedDate(TimeUtils.convertTimestampToDate(rs.getLong("bsoIssuedDate")));
		bso.setIssuedBy(issuedUser);
		bso.setIssuedUnit(issuedUnit);
		bso.setCorrupted(rs.getBoolean("bsoCorrupted"));
		bso.setCorruptedDate(TimeUtils.convertTimestampToDate(rs.getLong("bsoCorruptedDate")));
		bso.setCorruptedBy(corruptedUser);
		bso.setCorruptedUnit(corruptedUnit);
		bso.setRegistered(rs.getBoolean("bsoRegistered"));
		bso.setRegisteredDate(TimeUtils.convertTimestampToDate(rs.getLong("bsoRegisteredDate")));
		bso.setRegisteredBy(registeredUser);
		bso.setRegisteredUnit(registeredUnit);
		bso.setInsured(rs.getString("bsoInsured"));
		bso.setPremium(rs.getBigDecimal("bsoPremium"));
		bso.setUnit(unit);
		bso.setNode(node);

		return bso;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Bso bso) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Bso bso) throws SQLException {
		int index = 1;

		ps.setBoolean(index++, bso.isIssued());
		if(bso.getIssuedDate() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setLong(index++, TimeUtils.convertDateToTimestamp(bso.getIssuedDate()));
		}
		if(bso.getIssuedBy() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getIssuedBy().getId());
		}
		if(bso.getIssuedUnit() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getIssuedUnit().getId());
		}

		ps.setBoolean(index++, bso.isCorrupted());
		if(bso.getCorruptedDate() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setLong(index++, TimeUtils.convertDateToTimestamp(bso.getCorruptedDate()));
		}
		if(bso.getCorruptedBy() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getCorruptedBy().getId());
		}
		if(bso.getCorruptedUnit() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getCorruptedUnit().getId());
		}

		ps.setBoolean(index++, bso.isRegistered());
		if(bso.getRegisteredDate() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setLong(index++, TimeUtils.convertDateToTimestamp(bso.getRegisteredDate()));
		}
		if(bso.getRegisteredBy() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getRegisteredBy().getId());
		}
		if(bso.getRegisteredUnit() == null) {
			ps.setNull(index++, Types.INTEGER);
		} else {
			ps.setInt(index++, bso.getRegisteredUnit().getId());
		}
		
		ps.setString(index++, bso.getInsured());
		ps.setBigDecimal(index++, bso.getPremium());

		ps.setInt(index++, bso.getId());
	}
}
