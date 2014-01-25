package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.resparser.IScriptResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.dbcreate.DbCreateResourceCreator;
import com.yonyou.nc.codevalidator.resparser.dbinit.DbInitResourceCreator;
import com.yonyou.nc.codevalidator.resparser.lang.I18nMultiLangResourceCreator;
import com.yonyou.nc.codevalidator.rule.IRuleDefinition;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 用于临时表资源创建的工具类，在规则执行之前进行初始化和清理的操作
 * @author mazhqa
 * @since V2.3
 */
public final class TempTableResourceCreatorUtils {

	protected static IScriptResourceQueryFactory RES_CREATOR_FACTORY = ResourceManagerFacade.getScriptResourceQueryFactory();

	private static Map<String, ITempTableResourceCreator> ID_TO_RESCREATOR_MAP = new HashMap<String, ITempTableResourceCreator>();
	static {
		addTempTableResCreator(new I18nMultiLangResourceCreator());
		addTempTableResCreator(new DbCreateResourceCreator());
		addTempTableResCreator(new DbInitResourceCreator());
	}
	
	private TempTableResourceCreatorUtils() {
		
	}
	
	private static void addTempTableResCreator(ITempTableResourceCreator tempTableResourceCreator) {
		ID_TO_RESCREATOR_MAP.put(tempTableResourceCreator.getIdentifier(), tempTableResourceCreator);
	}

	public static void initEnvironment(List<IRuleDefinition> ruleDefinitionList) throws RuleBaseException {
		Set<String> dependentCreatorIds = new HashSet<String>();
		for (IRuleDefinition ruleDefinition : ruleDefinitionList) {
			String[] dependentCreators = ruleDefinition.getDependentCreator();
			if(dependentCreators != null && dependentCreators.length > 0) {
				dependentCreatorIds.addAll(Arrays.asList(dependentCreators));
			}
		}
		Set<ITempTableResourceCreator> resCreators = new HashSet<ITempTableResourceCreator>();
		for (String resCreatorId : dependentCreatorIds) {
			ITempTableResourceCreator resCreator = ID_TO_RESCREATOR_MAP.get(resCreatorId);
			if(resCreator != null) {
				resCreators.add(resCreator);
			}
		}
		if(!resCreators.isEmpty()) {
			Connection connection = TempTableExecContextOperator.getCurrentConnection() == null ? RES_CREATOR_FACTORY.createTempConnection() :
				TempTableExecContextOperator.getCurrentConnection();
			TempTableExecContextOperator.setCurrentConnection(connection);
			for (ITempTableResourceCreator tempTableResourceCreator : resCreators) {
				String resId = tempTableResourceCreator.getIdentifier();
				if(!TempTableExecContextOperator.isInitialized(resId)){
					tempTableResourceCreator.createTempResources(connection);
					TempTableExecContextOperator.initialized(resId);
				}
			}
		}
	}
	
	public static void cleanUp() throws RuleBaseException {
		Connection currentConnection = TempTableExecContextOperator.getCurrentConnection();
		RES_CREATOR_FACTORY.closeConnection(currentConnection);
		TempTableExecContextOperator.clearCurrentConnection();
	}

}
