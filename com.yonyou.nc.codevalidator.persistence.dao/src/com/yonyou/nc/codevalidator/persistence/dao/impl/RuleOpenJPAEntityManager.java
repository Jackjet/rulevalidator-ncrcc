package com.yonyou.nc.codevalidator.persistence.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;

/**
 * 在规则JPA执行环境下的实体管理器
 * 
 * @author mazhqa
 * @since V2.7
 */
public class RuleOpenJPAEntityManager {

	public static final String DEFAULT_PERSISTENCE_NAME = "rule-openjpa";

	private static EntityManagerFactory entityManagerFactory;

	private static RuleOpenJPAEntityManager instance;

	public static RuleOpenJPAEntityManager getInstance() {
		if (instance == null) {
			synchronized (RuleOpenJPAEntityManager.class) {
				if (instance == null) {
					instance = new RuleOpenJPAEntityManager();
				}
			}
		}
		return instance;
	}

	private RuleOpenJPAEntityManager() {
		Map<String, String> optionMap = new HashMap<String, String>();
		optionMap.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)"); //$NON-NLS-1$ //$NON-NLS-2$
		optionMap.put("openjpa.Log", "DefaultLevel=WARN, Runtime=WARN, Tool=WARN");
		optionMap.put("javax.persistence.provider", "org.apache.openjpa.persistence.PersistenceProviderImpl");
		optionMap.put("openjpa.ConnectionFactoryProperties", "PrintParameters=True");
		optionMap.put("openjpa.DetachState", "loaded");
//		optionMap.put("openjpa.ConnectionURL", "jdbc:postgresql://20.1.83.57:5432/ncrcc");
		entityManagerFactory = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_NAME, optionMap);
	}

	/**
	 * 创建一个新的实体管理器
	 * @return - 当前并没有EJB容器自动对EntityManager进行全生命周期的管理，需要手动释放
	 * @throws RuleDaoException - 当工厂类并没有初始化成功，以致于并不能获取对应的实体管理器
	 */
	public EntityManager createEntityManager() throws RuleDaoException {
		if (entityManagerFactory == null) {
			throw new RuleDaoException("JPA Entity Manage的工厂类未创建成功!");
		}
		return entityManagerFactory.createEntityManager();
	}

}
