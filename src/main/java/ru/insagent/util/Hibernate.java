/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.insagent.model.UnitType;
import ru.insagent.model.*;

/**
 * Class to work with Hibernate sessions
 */
public class Hibernate {
	private SessionFactory sessionFactory;

	private static final Hibernate instance = new Hibernate();

	private Hibernate() {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
			.configure()
			.build();

		Metadata metadata = new MetadataSources(standardRegistry)
			.addAnnotatedClass(City.class)
			.addAnnotatedClass(Unit.class)
			.addAnnotatedClass(UnitType.class)
			.addAnnotatedClass(User.class)
			.addAnnotatedClass(Role.class)
			.addAnnotatedClass(MenuItem.class)
			.getMetadataBuilder()
			.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
			.build();

		sessionFactory = metadata.getSessionFactoryBuilder()
			.build();
	}

	/**
	 * Commits a Hibernate transaction for the current session.
	 */
	public static void commit() {
		getCurrentSession().getTransaction().commit();
	}

	/**
	 * Commits a Hibernate transaction.
	 */
	public static void commit(Session session) {
		session.getTransaction().commit();
	}

	/**
	 * Rollbacks a Hibernate transaction for the current session.
	 */
	public static void rollback() {
		getCurrentSession().getTransaction().rollback();
	}

	/**
	 * Rollbacks a Hibernate transaction.
	 */
	public static void rollback(Session session) {
		session.getTransaction().rollback();
	}

	/**
	 * Obtain a Hibernate session.
	 * @return A Hibernate session.
	 */
	public static Session openSession() {
		return instance.sessionFactory.openSession();
	}

	/**
	 * Obtain the current Hibernate session.
	 * @return A Hibernate session.
	 */
	public static Session getCurrentSession() {
		return instance.sessionFactory.getCurrentSession();
	}

	/**
	 * Begins a new transaction for the current session.
	 */
	public static void beginTransaction() {
		getCurrentSession().beginTransaction();
	}

	/**
	 * Close resources and return a class to an uninitialized state.
	 */
	public static void recycle() {
		instance.sessionFactory.close();
		instance.sessionFactory = null;
	}
}
