package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;

import com.yonyou.nc.codevalidator.resparser.IScriptResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * �������ʱ����Դ������
 * 
 * @author mazhqa
 * @since V2.3
 */
public abstract class AbstractTempTableResourceCreator implements ITempTableResourceCreator {

	protected IScriptResourceQueryFactory resourceQueryFactory = ResourceManagerFacade.getScriptResourceQueryFactory();

	@Override
	public final void cleanUp() throws RuleBaseException {
		Connection connection = TempTableExecContextOperator.getCurrentConnection();
		resourceQueryFactory.closeConnection(connection);
	}

}
