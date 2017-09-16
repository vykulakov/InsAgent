/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.insagent.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import ru.insagent.exception.AppException;

/**
 * Class to work with database connections
 *
 * @author Kulakov Vyacheslav <kulakov.home@gmail.com>
 */
public class Setup {
	private ComboPooledDataSource dataSource;

	private static final Setup instance = new Setup();

	private Setup() {
		try {
			dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl("jdbc:mysql://192.168.1.60/insagent?useUnicode=true&characterEncoding=utf-8");
			dataSource.setUser("insagent");
			dataSource.setPassword("123456");
			dataSource.setInitialPoolSize(5);
			dataSource.setMinPoolSize(5);
			dataSource.setAcquireIncrement(5);
			dataSource.setMaxPoolSize(20);
			dataSource.setMaxStatements(100);
			dataSource.setPreferredTestQuery("SELECT 1");
			dataSource.setIdleConnectionTestPeriod(30);
		} catch(PropertyVetoException e) {
			throw new AppException("Cannot init DataSource", e);
		}
	}

	/**
	 * Obtain a database connection from a pool.
	 * @return A database connection.
	 */
	public static Connection getConnection() {
		try {
			return instance.dataSource.getConnection();
		} catch(SQLException e) {
			throw new AppException("Cannot get connection", e);
		}
	}

	/**
	 * Close resources and return a class to an uninitialized state.
	 */
	public static void recycle() {
		instance.dataSource.close();
		instance.dataSource = null;
	}
}
