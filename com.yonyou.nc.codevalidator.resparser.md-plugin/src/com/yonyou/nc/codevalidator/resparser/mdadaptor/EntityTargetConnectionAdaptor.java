package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.model.Entity;

import com.yonyou.nc.codevalidator.resparser.md.IEntityTargetConnection;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class EntityTargetConnectionAdaptor implements IEntityTargetConnection {
	
	private final Connection connection;
	
	public EntityTargetConnectionAdaptor(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public IEntity getTargetEntity() {
		Cell target = connection.getTarget();
		if (target instanceof Entity) {
			Entity entity = (Entity) target;
			return new EntityAdaptor(entity);
		}
		Logger.error("得到的target并非Entity类型!");
		return null;
	}

}
