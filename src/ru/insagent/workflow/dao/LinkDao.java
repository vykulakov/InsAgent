package ru.insagent.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.insagent.dao.SimpleDao;
import ru.insagent.document.model.ActType;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

public class LinkDao extends SimpleDao<Link> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     w_links l"
				+ "     LEFT JOIN d_act_types t ON t.id = l.actTypeId"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     l.id AS linkId,"
				+ "     l.nodeFromId AS linkNodeFromId,"
				+ "     l.nodeToId AS linkNodeToId,"
				+ "     l.name AS linkName,"
				+ "     l.comment AS linkComment,"
				+ "     t.id AS actTypeId,"
				+ "     t.idx AS actTypeIdx,"
				+ "     t.shortName AS actTypeShortName,"
				+ "     t.fullName AS actTypeFullName"
				+ " FROM"
				+ "     w_links l"
				+ "     LEFT JOIN d_act_types t ON t.id = l.actTypeId"
				+ " WHERE"
				+ "     1 = 1";

		idField = "l.id";
	}

	public LinkDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<Link> listByUser(User user) {
		StringBuilder where = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		where.append("l.id IN (");
		for(int linkId : getLinkIdByRoles(user.getRoles())) {
			where.append("?,");
			objects.add(linkId);
		}
		where.setLength(where.length() - 1);
		where.append(")");

		List<Link> links = listByWhere(false, where.toString(), objects);

		return links;
	}

	public List<Integer> getLinkIdByRoles(Set<String> roles) throws AppException {
		List<Integer> linkIds = new ArrayList<Integer>();

		if(roles == null || roles.isEmpty()) {
			throw new AppException("Передан пустой список ролей.");
		}

		StringBuilder query = new StringBuilder(""
				+ " SELECT"
				+ "     l.linkId AS linkId"
				+ " FROM"
				+ "     m_roles r,"
				+ "     w_link_roles l"
				+ " WHERE"
				+ "     r.id = l.roleId AND"
				+ "     r.idx IN (");
		for(int i = 0; i < roles.size(); i++) {
			query.append("?");
			query.append(",");
		}
		query.setLength(query.length() - 1);
		query.append(");");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			int index = 1;
			ps = conn.prepareStatement(query.toString());
			for(String roleIdx : roles) {
				ps.setString(index++, roleIdx);
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				linkIds.add(rs.getInt("linkId"));
			}
		} catch(SQLException e) {
			logger.error("Cannot get link ids from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения идентификаторов связей из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}

		return linkIds;
	}

	public Set<String> getRolesByLinkId(int linkId) throws AppException {
		Set<String> roles = new HashSet<String>();

		String query = ""
				+ " SELECT"
				+ "     r.id AS roleId,"
				+ "     r.idx AS roleIdx,"
				+ "     r.name AS roleName"
				+ " FROM"
				+ "     m_roles r,"
				+ "     w_link_roles l"
				+ " WHERE"
				+ "     r.id = l.roleId AND"
				+ "     l.linkId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, linkId);

			rs = ps.executeQuery();
			while(rs.next()) {
				roles.add(rs.getString("roleIdx"));
			}
		} catch(SQLException e) {
			logger.error("Cannot get link roles from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения ролей связи из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}

		return roles;
	}

	public List<ActType> listActTypesByUser(User user) {
		List<ActType> actTypes = new ArrayList<ActType>();

		if(user == null) {
			throw new AppException("Передан пустой пользователь.");
		}

		StringBuilder query = new StringBuilder(""
				+ " SELECT"
				+ "     t.id AS actTypeId,"
				+ "     t.idx AS actTypeIdx,"
				+ "     t.shortName AS actTypeShortName,"
				+ "     t.fullName AS actTypeFullName"
				+ " FROM"
				+ "     d_act_types t,"
				+ "     w_links l,"
				+ "     w_link_roles lr,"
				+ "     m_roles r"
				+ " WHERE"
				+ "     t.id = l.actTypeId AND"
				+ "     l.id = lr.linkId AND"
				+ "     r.id = lr.roleId AND"
				+ "     r.idx IN (");
		for(int i = 0; i < user.getRoles().size(); i++) {
			query.append("?");
			query.append(",");
		}
		query.setLength(query.length() - 1);
		query.append(""
				+ "     )"
				+ " GROUP BY"
				+ "     t.id"
				+ " ORDER BY"
				+ "     t.order;");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			int index = 1;
			ps = conn.prepareStatement(query.toString());
			for(String roleIdx : user.getRoles()) {
				ps.setString(index++, roleIdx);
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				ActType actType = new ActType();
				actType.setId(rs.getInt("actTypeId"));
				actType.setIdx(rs.getString("actTypeIdx"));
				actType.setShortName(rs.getString("actTypeShortName"));
				actType.setFullName(rs.getString("actTypeFullName"));

				actTypes.add(actType);
			}
		} catch(SQLException e) {
			logger.error("Cannot get act types from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения типов актов из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}

		return actTypes;
	}

	@Override
	protected Link getFromRs(ResultSet rs) throws SQLException {
		Node nodeFrom = new Node();
		nodeFrom.setId(rs.getInt("linkNodeFromId"));

		Node nodeTo = new Node();
		nodeTo.setId(rs.getInt("linkNodeToId"));

		ActType type = null;
		if(rs.getInt("actTypeId") != 0) {
			type = new ActType();
			type.setId(rs.getInt("actTypeId"));
			type.setIdx(rs.getString("actTypeIdx"));
			type.setShortName(rs.getString("actTypeShortName"));
			type.setFullName(rs.getString("actTypeFullName"));
		}

		Link link = new Link();
		link.setId(rs.getInt("linkId"));
		link.setName(rs.getString("linkName"));
		link.setNodeFrom(nodeFrom);
		link.setNodeTo(nodeTo);
		link.setActType(type);
		link.setComment(rs.getString("linkComment"));
		link.setRoles(getRolesByLinkId(link.getId()));

		return link;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Link link) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Link link) throws SQLException {
	}
}
