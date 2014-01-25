package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

/**
 * �ű���ʽ�Ľ��(DataSet)��������
 * 
 * @author mazhqa
 * @since V2.5
 */
public interface IScriptExportStrategy {
	
	/**
	 * Ĭ�ϵĵ������ԣ�ֻ��ӡ��dataSet��toString��ʾ
	 */
	static final IScriptExportStrategy DEFAULT_EXPORT_STRATEGY = new IScriptExportStrategy() {
		
		@Override
		public String exportDataSet(DataSet dataSet) {
			return dataSet.toString();
		}
	};

	/**
	 * ������������ɶ�Ӧ���ַ�����ʾ
	 * @param dataSet
	 * @return
	 */
	String exportDataSet(DataSet dataSet);

}
