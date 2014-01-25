package com.yonyou.nc.codevalidator.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class RuleSessionFactory {

	private static SessionFactory sessionFactory = null;

	private static SessionFactory builderSessionFactory() {
		// 默认会加载classpath同目录下的hibernate.cfg.xml文件
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
