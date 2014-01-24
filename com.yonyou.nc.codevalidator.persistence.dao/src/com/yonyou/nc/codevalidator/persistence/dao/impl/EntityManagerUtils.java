package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.entity.IEntityIdentifier;

/**
 * 实体管理器的工具类
 * 
 * @author mazhqa
 * @since V2.8
 */
public class EntityManagerUtils {

	/**
	 * 使用em对实体进行保存操作
	 * @param em
	 * @param entity
	 */
	public static <T extends IEntityIdentifier> void saveEntity(EntityManager em, T entity) {
		String entityId = entity.getId();
		if (entityId == null) {
			em.persist(entity);
		} else {
			em.merge(entity);
		}
	}

}
