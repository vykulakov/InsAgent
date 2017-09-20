package ru.insagent.dao;

import ru.insagent.model.Role;

public class RoleDao extends SimpleHDao<Role> {
	{
		clazz = Role.class;

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     Role r"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     r"
			+ " FROM"
			+ "     Role r"
			+ " WHERE"
			+ "     1 = 1";
	}
}
