package ru.insagent.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;

public class Setup {
	private static final Setup instance = new Setup();

	private DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(Setup.class);

	private Setup() {
		try {
			Context initContext = new InitialContext();
			dataSource = (DataSource) initContext.lookup("java:/comp/env/jdbc/InsAgentDB");
		} catch(NamingException e) {
			logger.error("Cannot init DataSource: " + e.getMessage());
			throw new AppException("Cannot init DataSource", e);
		}
		if(dataSource == null) {
			logger.error("DataSource is null");
			throw new AppException("DataSource is null");
		}
	}

	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch(SQLException e) {
			logger.error("Cannot get connection: " + e.getMessage());
			throw new AppException("Cannot get connection", e);
		}
	}

	public static Setup getInstance() {
		return instance;
	}
}
