package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ���õļ��Ϲ�����
 * 
 * @author mazhqa
 * @since V2.7
 */
public class CollectionUtils {

	/**
	 * �ж�����list�Ƿ���ȣ�ListEqualVO
	 * 
	 * @param
	 * @param list1
	 * @return
	 */
	public static <T> ListEqualVO<T> isListEqual(List<T> srcList, List<T> destList) {
		ListEqualVO<T> equalVO = new ListEqualVO<T>();
		// Դ������Ŀ������Ϊ�գ�����Ϊ����list���
		if (srcList == null && null == destList) {
			equalVO.setIsEqual(true);
		} else {
			// Դ����Ϊ�գ�
			if (srcList == null) {
				equalVO.setIsEqual(false);
				equalVO.setNotExistSrc(destList);
			} else if (destList == null) {
				// destList�����Ϊ�գ�
				equalVO.setIsEqual(false);
				equalVO.setNotExistSrc(srcList);
			} else {
				List<T> commonList = new ArrayList<T>();
				for (T srcCls : srcList) {
					if (destList.contains(srcCls)) {
						commonList.add(srcCls);
					}
				}
				List<T> srcCopyList = new ArrayList<T>(srcList);
				srcCopyList.removeAll(commonList);
				List<T> destCopyList = new ArrayList<T>(destList);
				destCopyList.removeAll(commonList);

				if (srcCopyList.size() > 0 || destCopyList.size() > 0) {
					equalVO.setIsEqual(false);
					equalVO.setNotExistDest(srcCopyList);
					equalVO.setNotExistSrc(destCopyList);
				} else {
					equalVO.setIsEqual(true);
				}
				equalVO.setSameObj(commonList);
			}
		}
		return equalVO;
	}

}
