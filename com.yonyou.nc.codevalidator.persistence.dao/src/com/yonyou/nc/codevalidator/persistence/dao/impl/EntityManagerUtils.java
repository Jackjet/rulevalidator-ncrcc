package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.entity.IEntityIdentifier;

/**
 * ʵ��������Ĺ�����
 * 
 * @author mazhqa
 * @since V2.8
 */
public class EntityManagerUtils {

	/**
	 * ʹ��em��ʵ����б������
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
