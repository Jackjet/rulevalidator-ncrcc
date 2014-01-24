package com.yonyou.nc.codevalidator.plugin.domain.mm.md;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IBusiInterface;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;

/**
 * ���������Ԫ���ݼ��ʹ�õĹ�����
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmMetadataUtils {

	private MmMetadataUtils() {

	}

	/**
	 * ����Ԫ������ʵ�ֵĽӿڵ�Name
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
	 * �ж�ʵ��ʵ�ֵĽӿ��Ƿ�ӳ����Ԫ�����ж�Ӧ������
	 * 
	 * @param itfAttrMap
	 *            �ӿڶ�Ӧ������
	 * @param mapProperties
	 *            �ӿ���Ҫӳ�������
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
	 * Ԫ�������Ƿ���FieldName�ֶ�
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
