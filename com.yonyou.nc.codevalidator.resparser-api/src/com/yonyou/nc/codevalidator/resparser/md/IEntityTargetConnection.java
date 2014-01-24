package com.yonyou.nc.codevalidator.resparser.md;

/**
 * 实体之间的连接对象，仅提供目标为实体的连接对象
 * @author mazhqa
 * @since V2.6
 */
public interface IEntityTargetConnection {
	
	/**
	 * 得到其对应的目标实体
	 * @return
	 */
	IEntity getTargetEntity();

}
