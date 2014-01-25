package com.yonyou.nc.codevalidator.resparser.function.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IScriptResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.datasource.IDataSourceService;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;

/**
 * 用于通用的NC功能查询
 * 
 * @author mazhqa
 * @since V2.3 - 从IScriptResourceQueryFactory中挪出的相关方法
 */
public final class NCCommonQueryUtils {

	public static final String MODULE_KEY = "module_key"; // 用此表示module的主键（module没有cfunid字段）

	private static final String MODULEVO_SQL = "select '"
			+ MODULE_KEY
			+ "' as modulekey,moduleid as modulecode,(case when (parentcode is null or parentcode='~') then '_root_' else parentcode end) as parentmodulecode,"
			+ " systypename as modulename from dap_dapsystem union select cfunid as modulekey,funcode as modulecode,(case when (parent_id is null or parent_id='~') "
			+ " then own_module else parent_id end) as parentmodulecode,fun_name as modulename from sm_funcregister where isfunctype = 'Y'";

	private static final String FUNNODEVO_SQL = "select cfunid as modulekey,funcode,"
			+ "(case when (parent_id is null or parent_id = '~') then own_module else parent_id end) as parentcode,"
			+ "fun_name as funname from sm_funcregister where isfunctype = 'N'";

	private static final IScriptResourceQueryFactory RESOURCE_QUERY_FACTORY = ResourceManagerFacade
			.getScriptResourceQueryFactory();

	/**
	 * 从当前的默认数据源 design 中查询出所有的NC模块，取自数据库表sm_funcregister
	 * 
	 * @return 模块编码对应模块对象的映射
	 * @throws ResourceParserException
	 */
	public static List<NCModuleVO> queryAllModuleVOs() throws ResourceParserException {
		ScriptDataSetResource scriptDataSetResource = RESOURCE_QUERY_FACTORY.getResourceAsDataSet(MODULEVO_SQL,
				IDataSourceService.DEFAULT_DATASOURCE_NAME);
		DataSet dataSet = scriptDataSetResource.getDataSet();
		List<NCModuleVO> result = new ArrayList<NCModuleVO>();
		if (!dataSet.isEmpty()) {
			List<DataRow> dataRowList = dataSet.getRows();
			for (DataRow dataRow : dataRowList) {
				String moduleKey = (String) dataRow.getValue("modulekey");
				String moduleCode = (String) dataRow.getValue("modulecode");
				String moduleName = (String) dataRow.getValue("modulename");
				String parentModuleCode = (String) dataRow.getValue("parentmodulecode");
				result.add(new NCModuleVO(moduleKey, moduleName, moduleCode, parentModuleCode));
			}
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * 从当前的默认数据源 design 中查询出所有的NC功能节点，取自数据库表sm_funcregister
	 * 
	 * @return 节点对应功能节点VO的映射
	 * @throws ResourceParserException
	 */
	public static List<NCFunnodeVO> queryAllFunnodeVOs() throws ResourceParserException {
		ScriptDataSetResource scriptDataSetResource = RESOURCE_QUERY_FACTORY.getResourceAsDataSet(FUNNODEVO_SQL,
				IDataSourceService.DEFAULT_DATASOURCE_NAME);
		DataSet dataSet = scriptDataSetResource.getDataSet();
		// Map<String, NCFunnodeVO> result = new HashMap<String, NCFunnodeVO>();
		List<NCFunnodeVO> result = new ArrayList<NCFunnodeVO>();
		if (!dataSet.isEmpty()) {
			List<DataRow> dataRowList = dataSet.getRows();
			for (DataRow dataRow : dataRowList) {
				String key = (String) dataRow.getValue("modulekey");
				String funcode = (String) dataRow.getValue("funcode");
				String funname = (String) dataRow.getValue("funname");
				String parentcode = (String) dataRow.getValue("parentcode");
				result.add(new NCFunnodeVO(key, funname, funcode, parentcode));
			}
		}
		return Collections.unmodifiableList(result);
	}
	
	/**
	 * 得到当前数据源中的所有功能节点编号列表
	 * @return
	 * @throws ResourceParserException
	 */
	public static List<String> getAllNcFuncodes() throws ResourceParserException{
		List<NCFunnodeVO> queryAllFunnodeVOs = queryAllFunnodeVOs();
		List<String> result = new ArrayList<String>();
		for (NCFunnodeVO funnodeVo : queryAllFunnodeVOs) {
			result.add(funnodeVo.getFuncode());
		}
		return result;
	}

	/**
	 * 根据模块vo查询出其所有的子模块vo，包括间接子
	 * @param allNcModuleVoList
	 * @param moduleVo
	 * @return
	 */
	public static List<NCModuleVO> getAllChildrenOfModuleVO(List<NCModuleVO> allNcModuleVoList, NCModuleVO moduleVo) {
		String key = moduleVo.getKey();
		String moduleCode = moduleVo.getModulecode();
		List<NCModuleVO> result = new ArrayList<NCModuleVO>();
		for (NCModuleVO ncModuleVO : allNcModuleVoList) {
			if (ncModuleVO.getParentmodulecode().equals(moduleCode) || ncModuleVO.getParentmodulecode().equals(key)) {
				result.add(ncModuleVO);
			}
		}
		return result;
	}

	/**
	 * 根据模块编码或的模块的所有子vo，包括间接子
	 * @param allNcModuleVoList
	 * @param moduleCode
	 * @return
	 */
	public static List<NCModuleVO> getAllChildrenOfModuleVO(List<NCModuleVO> allNcModuleVoList, String moduleCode) {
		List<NCModuleVO> result = new ArrayList<NCModuleVO>();
		for (NCModuleVO ncModuleVO : allNcModuleVoList) {
			if (ncModuleVO.getParentmodulecode().equals(moduleCode)) {
				result.add(ncModuleVO);
			}
		}
		return result;
	}
}
