package ru.insagent.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.insagent.dao.SimpleDao;
import ru.insagent.model.UnitType;
import ru.insagent.workflow.model.Node;
import ru.insagent.workflow.model.NodeType;

public class NodeDao extends SimpleDao<Node> {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     w_nodes n,"
				+ "     w_node_types t"
				+ " WHERE"
				+ "     t.id = n.nodeTypeId";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     n.id AS nodeId,"
				+ "     n.name AS nodeName,"
				+ "     n.issued AS nodeIssued,"
				+ "     t.id AS nodeTypeId,"
				+ "     t.name AS nodeTypeName,"
				+ "     n.unitTypeId AS nodeUnitTypeId"
				+ " FROM"
				+ "     w_nodes n,"
				+ "     w_node_types t"
				+ " WHERE"
				+ "     t.id = n.nodeTypeId";

		selectOrder = "n.order ASC";

		idField = "n.id";
	}

	public NodeDao(Connection conn) {
		super(conn);
	}

	@Override
	protected Node getFromRs(ResultSet rs) throws SQLException {
		NodeType nodeType = new NodeType();
		nodeType.setId(rs.getInt("nodeTypeId"));
		nodeType.setName(rs.getString("nodeTypeName"));

		UnitType unitType = new UnitType();
		unitType.setId(rs.getInt("nodeUnitTypeId"));

		Node node = new Node();
		node.setId(rs.getInt("nodeId"));
		node.setName(rs.getString("nodeName"));
		node.setNodeType(nodeType);
		node.setUnitType(unitType);

		return node;
	}

	@Override
	protected void setInsertPs(PreparedStatement ps, Node node) throws SQLException {
	}

	@Override
	protected void setUpdatePs(PreparedStatement ps, Node node) throws SQLException {
	}
}
