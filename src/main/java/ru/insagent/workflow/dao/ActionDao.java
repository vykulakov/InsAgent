package ru.insagent.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.insagent.dao.SimpleDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.workflow.model.Action;

public class ActionDao extends SimpleDao<Action> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     w_actions a"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     a.id AS actionId,"
				+ "     a.idx AS actionIdx,"
				+ "     a.shortName AS actionShortName,"
				+ "     a.fullName AS actionFullName"
				+ " FROM"
				+ "     w_actions a"
				+ " WHERE"
				+ "     1 = 1";

		idField = "a.id";
	}

	public ActionDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<Action> listByUser(User user) {
		StringBuilder where = new StringBuilder();
		List<Object> objects = new ArrayList<Object>();

		where.append("a.id IN (");
		for(int actionId : getActionIdByRoles(user.getRoles())) {
			where.append("?,");
			objects.add(actionId);
		}
		where.setLength(where.length() - 1);
		where.append(")");

		return listByWhere(false, where.toString(), objects);
	}

	public List<Integer> listNodeIdsByActionId(int actionId) {
		List<Integer> nodeIds = new ArrayList<Integer>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(""
					+ " SELECT"
					+ "     a.nodeId AS nodeId"
					+ " FROM"
					+ "     w_node_actions a"
					+ " WHERE"
					+ "     a.actionId = ?;");
			ps.setInt(1, actionId);

			rs = ps.executeQuery();
			while(rs.next()) {
				nodeIds.add(rs.getInt("nodeId"));
			}
		} catch(SQLException e) {
			logger.error("Cannot get node ids from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения идентификаторов узлов из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}

		return nodeIds;
	}

	public List<Integer> getActionIdByRoles(Set<String> roles) throws AppException {
		List<Integer> actionIds = new ArrayList<Integer>();

		if(roles == null || roles.isEmpty()) {
			throw new AppException("Передан пустой список ролей.");
		}

		StringBuilder query = new StringBuilder(""
				+ " SELECT"
				+ "     a.actionId AS actionId"
				+ " FROM"
				+ "     m_roles r,"
				+ "     w_action_roles a"
				+ " WHERE"
				+ "     r.id = a.roleId AND"
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
				actionIds.add(rs.getInt("actionId"));
			}
		} catch(SQLException e) {
			logger.error("Cannot get action ids from DB: {}", e.getMessage());

			throw new AppException("Ошибка получения идентификаторов действий из базы данных.", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
		}

		return actionIds;
	}

	@Override
	protected Action getFromRs(ResultSet rs) throws SQLException {
		Action action = new Action();
		action.setId(rs.getInt("actionId"));
		action.setIdx(rs.getString("actionIdx"));
		action.setShortName(rs.getString("actionShortName"));
		action.setFullName(rs.getString("actionFullName"));

		return action;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Action action) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Action action) throws SQLException {
	}
}
