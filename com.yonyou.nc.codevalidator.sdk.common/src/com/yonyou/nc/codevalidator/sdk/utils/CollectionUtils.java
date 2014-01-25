package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用的集合工具类
 * 
 * @author mazhqa
 * @since V2.7
 */
public class CollectionUtils {

	/**
	 * 判断两个list是否相等，ListEqualVO
	 * 
	 * @param
	 * @param list1
	 * @return
	 */
	public static <T> ListEqualVO<T> isListEqual(List<T> srcList, List<T> destList) {
		ListEqualVO<T> equalVO = new ListEqualVO<T>();
		// 源对象与目标对象均为空，则认为两个list相等
		if (srcList == null && null == destList) {
			equalVO.setIsEqual(true);
		} else {
			// 源对象为空，
			if (srcList == null) {
				equalVO.setIsEqual(false);
				equalVO.setNotExistSrc(destList);
			} else if (destList == null) {
				// destList标对象为空，
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
