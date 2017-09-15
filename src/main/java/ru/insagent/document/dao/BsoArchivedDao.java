package ru.insagent.document.dao;

import java.sql.Connection;

public class BsoArchivedDao extends BsoDao {
	{
		countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     d_bsos_archived b"
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
				+ "     d_bsos_archived b"
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

		updateQuery = "";
	}

	public BsoArchivedDao(Connection conn) {
		super(conn);
	}
}
