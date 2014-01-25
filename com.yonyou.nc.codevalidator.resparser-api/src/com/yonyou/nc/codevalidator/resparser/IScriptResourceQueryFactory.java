package com.yonyou.nc.codevalidator.resparser;

import java.sql.Connection;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.TableColumn;

/**
 * 数据库脚本资源查询工厂类，包括资源的查询以及临时表操作的一些关键方法
 * <P>
 * 注：用户在使用时，需要注意不能轻易调用其中的临时表创建，插入以及打开关闭连接等操作
 * 
 * @author mazhqa
 * @since V1.0
 */
public interface IScriptResourceQueryFactory extends IResourceQueryFactory<ScriptResource, ScriptResourceQuery> {

	/**
	 * 以脚本数据集的方式结果返回
	 * 
	 * @param resourceQuery
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSet(ScriptResourceQuery resourceQuery) throws ResourceParserException;

	/**
	 * 用于跨session的临时表查询数据集
	 * 
	 * @param resourceQuery
	 * @param connection
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSetWithConnection(ScriptResourceQuery resourceQuery, Connection connection)
			throws ResourceParserException;

	/**
	 * 创建数据库临时表
	 * 
	 * @param tableName
	 *            - 临时表名称
	 * @param columns
	 *            - 列名称
	 * @param idx
	 *            - 索引序列
	 * @return 临时表名称
	 */
	String createTempTable(Connection connection, String tableName, String columns, String... idx) throws ResourceParserException;

	/**
	 * 根据当前线程执行的数据源，生成connection，用于临时表插入以及删除等操作
	 * <p>
	 * 注：用完之后请及时调用connection的关闭操作
	 * 
	 * @return
	 */
	Connection createTempConnection() throws ResourceParserException;

	/**
	 * 用于关闭临时表使用的connection连接
	 * 
	 * @param connection
	 * @throws ResourceParserException
	 */
	void closeConnection(Connection connection) throws ResourceParserException;

	/**
	 * 将预置的数据装入至临时表中
	 * <p>
	 * 注意：本方法不会对connection进行关闭
	 * 
	 * @param tempTableName
	 *            - 临时表名称
	 * @param columnNames
	 *            - 临时表中列名称
	 * @param dataArrays
	 *            - 数据，以数组列表的方式，每一行为一条完整的数据条目，对应预置临时表列名称对应的数据
	 * @param connection
	 *            - 数据库连接，最好是从TempTableExecContext中获得
	 * @throws ResourceParserException
	 */
	void insertDataToTempTable(String tempTableName, List<String> columnNames, List<Object[]> dataArrays,
			Connection connection) throws ResourceParserException;

//	/**
//	 * 从mde连接的数据库中查询NCModuleVO(详见该类的解释)
//	 * 
//	 * @return
//	 * @throws ResourceParserException
//	 */
//	Map<String, NCModuleVO> queryModuleVO() throws ResourceParserException;
//
//	/**
//	 * 从mde连接的数据库中查询NCFunnodeVO(详见该类的解释)
//	 * 
//	 * @return
//	 * @throws ResourceParserException
//	 */
//	Map<String, NCFunnodeVO> queryFunnodeVO() throws ResourceParserException;
	
	/**
	 * 以脚本数据集的方式结果返回某个特定数据源查询结果
	 * <p>
	 * 注：此方法不验证当前设置的数据源，需谨慎调用
	 * @param resourceQuery
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSet(String queryString, String dataSourceName) throws ResourceParserException;
	
	/**
	 * 根据数据表名称获取字段列表，并屏蔽数据库的差异性
	 * @param tableName
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	List<TableColumn> getTableColumns(String tableName, String dataSourceName) throws ResourceParserException;

}
