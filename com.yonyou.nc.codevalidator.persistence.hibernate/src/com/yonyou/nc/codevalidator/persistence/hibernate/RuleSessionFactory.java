package com.yonyou.nc.codevalidator.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class RuleSessionFactory {

	private static SessionFactory sessionFactory = null;

	private static SessionFactory builderSessionFactory() {
		// Ĭ�ϻ����classpathͬĿ¼�µ�hibernate.cfg.xml�ļ�
		Configuration cfg = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(cfg.getProperties());
		ServiceRegistry registry = builder.build();
		return cfg.buildSessionFactory(registry);
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = builderSessionFactory();
		}
		return sessionFactory;
	}

}
