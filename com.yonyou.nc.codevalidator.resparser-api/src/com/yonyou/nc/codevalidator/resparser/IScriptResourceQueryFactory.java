package com.yonyou.nc.codevalidator.resparser;

import java.sql.Connection;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.TableColumn;

/**
 * ���ݿ�ű���Դ��ѯ�����࣬������Դ�Ĳ�ѯ�Լ���ʱ�������һЩ�ؼ�����
 * <P>
 * ע���û���ʹ��ʱ����Ҫע�ⲻ�����׵������е���ʱ�����������Լ��򿪹ر����ӵȲ���
 * 
 * @author mazhqa
 * @since V1.0
 */
public interface IScriptResourceQueryFactory extends IResourceQueryFactory<ScriptResource, ScriptResourceQuery> {

	/**
	 * �Խű����ݼ��ķ�ʽ�������
	 * 
	 * @param resourceQuery
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSet(ScriptResourceQuery resourceQuery) throws ResourceParserException;

	/**
	 * ���ڿ�session����ʱ���ѯ���ݼ�
	 * 
	 * @param resourceQuery
	 * @param connection
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSetWithConnection(ScriptResourceQuery resourceQuery, Connection connection)
			throws ResourceParserException;

	/**
	 * �������ݿ���ʱ��
	 * 
	 * @param tableName
	 *            - ��ʱ������
	 * @param columns
	 *            - ������
	 * @param idx
	 *            - ��������
	 * @return ��ʱ������
	 */
	String createTempTable(Connection connection, String tableName, String columns, String... idx) throws ResourceParserException;

	/**
	 * ���ݵ�ǰ�߳�ִ�е�����Դ������connection��������ʱ������Լ�ɾ���Ȳ���
	 * <p>
	 * ע������֮���뼰ʱ����connection�Ĺرղ���
	 * 
	 * @return
	 */
	Connection createTempConnection() throws ResourceParserException;

	/**
	 * ���ڹر���ʱ��ʹ�õ�connection����
	 * 
	 * @param connection
	 * @throws ResourceParserException
	 */
	void closeConnection(Connection connection) throws ResourceParserException;

	/**
	 * ��Ԥ�õ�����װ������ʱ����
	 * <p>
	 * ע�⣺�����������connection���йر�
	 * 
	 * @param tempTableName
	 *            - ��ʱ������
	 * @param columnNames
	 *            - ��ʱ����������
	 * @param dataArrays
	 *            - ���ݣ��������б�ķ�ʽ��ÿһ��Ϊһ��������������Ŀ����ӦԤ����ʱ�������ƶ�Ӧ������
	 * @param connection
	 *            - ���ݿ����ӣ�����Ǵ�TempTableExecContext�л��
	 * @throws ResourceParserException
	 */
	void insertDataToTempTable(String tempTableName, List<String> columnNames, List<Object[]> dataArrays,
			Connection connection) throws ResourceParserException;

//	/**
//	 * ��mde���ӵ����ݿ��в�ѯNCModuleVO(�������Ľ���)
//	 * 
//	 * @return
//	 * @throws ResourceParserException
//	 */
//	Map<String, NCModuleVO> queryModuleVO() throws ResourceParserException;
//
//	/**
//	 * ��mde���ӵ����ݿ��в�ѯNCFunnodeVO(�������Ľ���)
//	 * 
//	 * @return
//	 * @throws ResourceParserException
//	 */
//	Map<String, NCFunnodeVO> queryFunnodeVO() throws ResourceParserException;
	
	/**
	 * �Խű����ݼ��ķ�ʽ�������ĳ���ض�����Դ��ѯ���
	 * <p>
	 * ע���˷�������֤��ǰ���õ�����Դ�����������
	 * @param resourceQuery
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	ScriptDataSetResource getResourceAsDataSet(String queryString, String dataSourceName) throws ResourceParserException;
	
	/**
	 * �������ݱ����ƻ�ȡ�ֶ��б����������ݿ�Ĳ�����
	 * @param tableName
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	List<TableColumn> getTableColumns(String tableName, String dataSourceName) throws ResourceParserException;

}
