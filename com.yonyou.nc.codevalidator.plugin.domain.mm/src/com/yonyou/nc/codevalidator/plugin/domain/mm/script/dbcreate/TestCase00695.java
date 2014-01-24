package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.dbcreate.DbCreateTable;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractDbcreateRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 检测所有建库脚本是否存在相同表名的脚本存在
 * 
 * @since 6.0
 * @version 2013-9-23 下午1:29:24
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "检测所有建库脚本是否存在相同表名的脚本存在 ", solution = "请修改表名使其名称唯一  ", coder = "zhongcha", relatedIssueId = "695")
public class TestCase00695 extends AbstractDbcreateRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        DataSet ds = this.executeDbcreateTableQuery(this.getSql(), ruleExecContext);
        List<String> errorList = new ArrayList<String>();
        MapList<String, String> tbname2tbnames = new MapList<String, String>();
        for (int i = 0; i < ds.getRowCount(); i++) {
            String tablename = (String) ds.getValue(i, DbCreateTable.TABLE_NAME_FIELD);
            tbname2tbnames.put(tablename, tablename);
        }
        for (Entry<String, List<String>> tbname2tbname : tbname2tbnames.entrySet()) {
            if (tbname2tbname.getValue().size() > 1) {
                errorList.add(tbname2tbname.getKey());
            }
        }
        if (MMValueCheck.isNotEmpty(errorList)) {
            result.addResultElement("全局", "所有建库脚本中表名为" + errorList + "的表存在相同表名的建库脚本\n");
        }

        return result;
    }

    private String getSql() {
        return String
                .format("select %s from %s", DbCreateTable.TABLE_NAME_FIELD, DbCreateTable.DBCREATE_TEMPTABLE_NAME);
    }

}
