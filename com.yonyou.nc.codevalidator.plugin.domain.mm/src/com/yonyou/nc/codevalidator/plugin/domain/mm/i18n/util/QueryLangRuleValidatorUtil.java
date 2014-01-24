package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 查询多语文件表的工具类
 * 
 * @since 6.0
 * @version 2013-8-31 下午9:13:41
 * @author zhongcha
 */
public class QueryLangRuleValidatorUtil {
    /**
     * 查询该resid是否存在于对应的多语文件中
     * 
     * @param resid
     * @return
     * @throws RuleBaseException
     */
    public static boolean notInLRV(String resid, RuntimeContext runtimeContext) throws RuleBaseException {
        String sql =
                String.format("select * from %s where id='%s'", SQLQueryExecuteUtils.getCurrentMultiLangTableName(),
                        resid);
        DataSet ds = SQLQueryExecuteUtils.executeMultiLangQuery(sql, runtimeContext);
        // String sqlStr = "select * from LANG_RULEVALIDATOR where id='" + resid + "'";
        // ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sqlStr);
        // DataSet ds = ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery).getDataSet();
        boolean result = ds.isEmpty();
        return result;
    }

    /**
     * 查询该resid是否存在于对应的多语文件中
     * 
     * @param resid
     * @return
     * @throws RuleBaseException
     */
    public static List<String> notInLRV(String[] resids, RuntimeContext runtimeContext) throws RuleBaseException {
        List<String> notInLRVResids = new ArrayList<String>();
        notInLRVResids.addAll(Arrays.asList(resids));
        StringBuilder sql = new StringBuilder();
        sql.append("select id from " + SQLQueryExecuteUtils.getCurrentMultiLangTableName() + " where ");
        sql.append(SQLQueryExecuteUtils.buildSqlForIn("id", resids));
        DataSet ds = SQLQueryExecuteUtils.executeMultiLangQuery(sql.toString(), runtimeContext);
        for (DataRow dataRow : ds.getRows()) {
            notInLRVResids.remove(dataRow.getValue("id"));
        }
        return notInLRVResids;
    }
}
