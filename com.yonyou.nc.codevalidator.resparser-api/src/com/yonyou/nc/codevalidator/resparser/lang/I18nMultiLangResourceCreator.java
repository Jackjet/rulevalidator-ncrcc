package com.yonyou.nc.codevalidator.resparser.lang;

import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.temp.AbstractTempTableResourceCreator;
import com.yonyou.nc.codevalidator.resparser.temp.TempTableExecContextOperator;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ���ﴦ������ʹ�����
 * 
 * @author mazhqa
 * @since V2.3
 */
public class I18nMultiLangResourceCreator extends AbstractTempTableResourceCreator{

	public final static String DEFAULT_TABLE_NAME_PREFIX = "mlt";
	public final static String PK_FIELD_NAME = "mltpk";
	public final static String ID_FIELD_NAME = "id";
	public final static String ENGLIST_FIELD_NAME = "english";
	public final static String SIMP_FIELD_NAME = "simp";
	public final static String TRAD_FIELD_NAME = "trad";
	public final static String PATH_FIELD_NAME = "path";
	public final static String FILE_NAME_FIELD_NAME = "filename";
	public final static String TS_FIELD_NAME = "ts";

//	private static final IScriptResourceQueryFactory RESOURCE_QUERY_FACTORY = (IScriptResourceQueryFactory) new ResourceManagerProxy()
//			.getResourceQueryFactory(ResourceType.SCRIPT);

	private final static String LANGLIB_FOLDER_NAME = "langlib";

//	private static ThreadLocal<MultiLangContext> CLIENT_CONTEXT_TL = new ThreadLocal<MultiLangContext>() {
//		protected MultiLangContext initialValue() {
//			MultiLangContext multiLangContext = new MultiLangContext();
//			Random random = new Random(Long.MAX_VALUE);
//			String randomIdentifier = String.valueOf(random.nextLong());
//			multiLangContext.setIdentifier(randomIdentifier);
//			multiLangContext.setInit(false);
//			return multiLangContext;
//		};
//	};

	public void createTempResources(Connection connection) throws RuleBaseException {
//		if(CLIENT_CONTEXT_TL.get().isInit()){
//			return;
//		}
		Logger.info("��ʼִ�ж����ʼ��...");
		RuntimeContext currentRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
				.getCurrentRuntimeContext();
		String ncHome = currentRuntimeContext.getNcHome();
		File langlibFolder = new File(ncHome, LANGLIB_FOLDER_NAME);
		if (!langlibFolder.exists() || !langlibFolder.isDirectory()) {
			Logger.error("����Ŀ¼�����ڣ���" + langlibFolder.getAbsolutePath());
			throw new RuleBaseException("����Ŀ¼�����ڣ���" + langlibFolder.getAbsolutePath());
		}

		String columns = String
				.format("%s varchar(512), %s varchar(1024), %s varchar(2048), %s varchar(2048), %s varchar(2048), %s varchar(1024), %s varchar(1024)",
						PK_FIELD_NAME, ID_FIELD_NAME, ENGLIST_FIELD_NAME, SIMP_FIELD_NAME, TRAD_FIELD_NAME, PATH_FIELD_NAME,
						FILE_NAME_FIELD_NAME);
		String tempTableName = resourceQueryFactory.createTempTable(connection, getMultiLangTempTableName(), columns,
				PK_FIELD_NAME);
		TempTableExecContextOperator.getTempTableExecContext().setMultiLangTableName(tempTableName);
		List<Object[]> dataArrayList = new ArrayList<Object[]>();
		
		File[] simLangFiles = langlibFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.getName().matches(".*sim_langres.jar");
			}
		});
		Map<String, I18nPropertyElement> simLangElements = I18nFileAnalyser.analyseI18nElements(simLangFiles);
		for (Map.Entry<String, I18nPropertyElement> entry : simLangElements.entrySet()) {
			String resId = entry.getKey();
			I18nPropertyElement propElement = entry.getValue();
			dataArrayList.add(new String[] { UUID.randomUUID().toString(), resId, propElement.getEnglishValue(), propElement.getSimpValue(),
					propElement.getTradValue(), propElement.getEntryName(), propElement.getFileName() });
		}

		File ncscriptFolder = new File(ncHome, "ncscript");
		if(ncscriptFolder.exists()){
			Collection<File> csvFiles = (Collection<File>) FileUtils.listFiles(ncscriptFolder,
					new String[] { "csv" }, true);
			Map<String, I18nPropertyElement> csvElements = I18nFileAnalyser.analyseCsvElements(csvFiles);
			
			for (Map.Entry<String, I18nPropertyElement> entry : csvElements.entrySet()) {
				String resId = entry.getKey();
				I18nPropertyElement propElement = entry.getValue();
				dataArrayList.add(new String[] { UUID.randomUUID().toString(), resId, propElement.getEnglishValue(), propElement.getSimpValue(),
						propElement.getTradValue(), propElement.getEntryName(), propElement.getFileName() });
			}
		}

		resourceQueryFactory.insertDataToTempTable(tempTableName, Arrays.asList(PK_FIELD_NAME, ID_FIELD_NAME,
				ENGLIST_FIELD_NAME, SIMP_FIELD_NAME, TRAD_FIELD_NAME, PATH_FIELD_NAME, FILE_NAME_FIELD_NAME),
				dataArrayList, connection);
		Logger.info("�����ʼ��ִ�н���...");
//		CLIENT_CONTEXT_TL.get().setInit(true);
//		CLIENT_CONTEXT_TL.get().setConnection(connection);
	}
	
//	/**
//	 * �ڶ��������ɺ���е�һЩ�������
//	 */
//	public static void cleanUp(){
//		MultiLangContext multiLangContext = CLIENT_CONTEXT_TL.get();
//		if(multiLangContext != null){
//			Connection connection = multiLangContext.getConnection();
//			if(connection != null){
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					Logger.error(e.getMessage(), e);
//				}
//			}
//		}
//		CLIENT_CONTEXT_TL.set(null);
//		
//	}

	/**
	 * ������ʱ������ƣ������߳̽�������
	 * 
	 * @return
	 */
	public static String getMultiLangTempTableName() {
//		return String.format("%s_%s", DEFAULT_TABLE_NAME_PREFIX, CLIENT_CONTEXT_TL.get().getIdentifier());
		//mazhqa ���ǵ�����û���ͬʱ����ʱ������ͬ����������⣬ʹ��Ĭ�ϵı����Ƽ��ɡ�
		return DEFAULT_TABLE_NAME_PREFIX;
	}

	@Override
	public String getIdentifier() {
		return CreatorConstants.I18N;
	}
	
//	public static Connection getMultiLangConnection(){
//		return CLIENT_CONTEXT_TL.get().getConnection();
//	}

}
