package com.yonyou.nc.codevalidator.plugin.domain.mm.md;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;

/**
 * 流程制造对元数据检查使用的工具类
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmMetadataUtils {

	private MmMetadataUtils() {

	}

	/**
	 * 返回元数据是实现的接口的Name
	 * 
	 * @param itfList
	 * @return
	 */
	public static List<String> getBusiInterfaceNames(
			List<IBusiInterface> itfList) {

		if (itfList == null || itfList.size() < 1) {
			return new ArrayList<String>();
		}

		List<String> itfNames = new ArrayList<String>();
		for (IBusiInterface itf : itfList) {
			itfNames.add(itf.getFullClassName());
		}

		return itfNames;

	}

	/**
	 * 判断实体实现的接口是否映射了元数据中对应的属性
	 * 
	 * @param itfAttrMap
	 *            接口对应的属性
	 * @param mapProperties
	 *            接口需要映射的属性
	 * @return
	 */
	public static boolean isMappingProperties(
			Map<String, IAttribute> itfAttrMap, String[] mapProperties) {

		if (itfAttrMap == null || itfAttrMap.size() < 1
				|| mapProperties == null || mapProperties.length < 1) {
			return false;
		}
		for (String mapProperty : mapProperties) {
			if (null == itfAttrMap.get(mapProperty)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 元数据中是否有FieldName字段
	 * 
	 * @param entity
	 * @return
	 */
	public boolean hasMetaDataField(IEntity entity, String fieldName) {

		List<IAttribute> attrList = entity.getAttributes();

		if (null == attrList || attrList.size() < 1) {
			return false;
		}
		boolean hasField = false;
		for (IAttribute iAttribute : attrList) {
			if (fieldName.equals(iAttribute.getFieldName())
					|| iAttribute.getFieldName().contains(fieldName)) {
				hasField = true;
				break;
			}
		}
		return hasField;
	}
}
