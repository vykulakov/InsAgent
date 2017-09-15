package ru.insagent.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtils {

	private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * Приватный конструктор, чтобы класс нельзя было инициализировать.
	 */
	private JdbcUtils() {
	}

	public static void closeConnection(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				logger.error("Could not close JDBC Connection", e);
			} catch(Throwable e) {
				logger.error("Unexpected exception on closing JDBC Connection", e);
			}
		}
	}

	public static void closeStatement(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				logger.error("Could not close JDBC Statement", e);
			} catch(Throwable e) {
				logger.error("Unexpected exception on closing JDBC Statement", e);
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				logger.error("Could not close JDBC ResultSet", e);
			} catch(Throwable e) {
				logger.error("Unexpected exception on closing JDBC ResultSet", e);
			}
		}
	}
}
