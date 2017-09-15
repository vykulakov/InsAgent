package ru.insagent.dao;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDao {
	protected Connection conn;
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public BaseDao(Connection conn) {
		this.conn = conn;
	}
}
