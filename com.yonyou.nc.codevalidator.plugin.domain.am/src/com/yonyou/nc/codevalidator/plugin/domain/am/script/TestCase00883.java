package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractSingleScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;


@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "��鵥��ת�������м�������û���ظ���VO���գ�A-B�������򲻶�"
	, relatedIssueId = "883", subCatalog = SubCatalogEnum.PS_CONTENTCHECK,coder="zhangnane",
		executeLayer = ExecuteLayer.MODULE)
public class TestCase00883 extends AbstractSingleScriptQueryRuleDefinition{

	@Override
	protected final IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException{
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		DataSet dataSet = scriptDataSetResource.getDataSet();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(getScriptExportStrategy());
		return result;
	}

	private IScriptExportStrategy getScriptExportStrategy() {

		return new DefaultFormatStringScriptExportStrategy("��������������%s �������ƣ�%s ��Դ�������ͣ�%s ���ƣ�%s ��Դ�������ͣ�%s ���ƣ�%s Ŀ�ĵ������ͣ�%s ���ƣ�%s Ŀ�Ľ������ͣ�%s ���ƣ�%s\n"
				,"groupname","srctypename","srctanstypename","desttransname","desttransname");
	}

	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		StringBuilder checkSql = new StringBuilder();
		//���ռ��ź͵������ͷ����ѯʱ�жϷ������и����Ƿ����1
		//��ѯ���ֶ��м��ţ��������ƣ���Դ�������ͣ���Դ�����������ƣ���Դ�������ͣ���Դ�����������ƣ�Ŀ�ĵ������ͣ�Ŀ�ĵ����������ƣ�Ŀ�Ľ������ͣ�Ŀ�Ľ����������ƣ��ظ�����
		checkSql.append("SELECT org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num FROM ");
		//�����ֶΣ������ѯ���ظ�����
		checkSql.append("(SELECT pk_group,src_billtype,dest_billtype,dest_transtype,src_transtype,count(*) num FROM pub_vochange GROUP BY pk_group,src_billtype,dest_billtype ,dest_transtype,src_transtype " );
		checkSql.append("HAVING src_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"') or dest_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"')) counttable ");	
		//�����Ӳ�ѯ��������
		checkSql.append("LEFT JOIN  org_group ON org_group.pk_group = counttable.pk_group ");
		//�����Ӳ�ѯ��Դ������������
		checkSql.append("LEFT JOIN bd_billtype srcbilltable ON counttable.src_billtype = srcbilltable.pk_billtypecode ");
		//�����Ӳ�ѯ��Դ������������
		checkSql.append("LEFT JOIN bd_billtype srctanstable ON counttable.src_transtype = srctanstable.pk_billtypecode ");
		//�����Ӳ�ѯĿ�ĵ�����������
		checkSql.append("LEFT JOIN bd_billtype destbilltable ON counttable.dest_billtype = destbilltable.pk_billtypecode ");
		//�����Ӳ�ѯĿ�Ľ�����������
		checkSql.append("LEFT JOIN bd_billtype desttranstable ON counttable.dest_transtype = desttranstable.pk_billtypecode ");
		
		checkSql.append("WHERE num> 1");
	
		return new ScriptResourceQuery (checkSql.toString());
	}

	//��Щ�ֶα�����pub_vochange���е��ֶΣ��ж��Ƿ��ظ����ֶ�:���ţ���Դ�������ͣ�Ŀ�ĵ������ͣ���Դ�������ͣ�Ŀ�Ľ�������
//	private final String [] checkRepetList = {"pk_group","src_billtype","dest_billtype","dest_transtype","src_transtype"};
	
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
//			throws RuleBaseException {
//		//TODO: �ع� ģ�鼶������״̬
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//		DataSet dataset = SQLQueryExecuteUtils.executeQuery(getCheckSql(moduleCode.toUpperCase()),ruleExecContext.getRuntimeContext());
//		
//		StringBuilder noteBuilder = new StringBuilder();
//		if(!dataset.isEmpty()){
//			noteBuilder.append("����vo���������ظ�:\n");
//			for(DataRow dr : dataset.getRows()){
//				noteBuilder.append(String.format("��������������%s �������ƣ�%s ��Դ�������ͣ�%s ���ƣ�%s ��Դ�������ͣ�%s ���ƣ�%s Ŀ�ĵ������ͣ�%s ���ƣ�%s Ŀ�Ľ������ͣ�%s ���ƣ�%s\n",
//				dr.getValue("pk_group"),dr.getValue("groupname"),
//				dr.getValue("src_billtype"),dr.getValue("srctypename"),
//				dr.getValue("src_transtype"),dr.getValue("srctanstypename"),
//				dr.getValue("dest_billtype"),dr.getValue("desttransname"),
//				dr.getValue("dest_transtype"),dr.getValue("desttransname")));
//			}
//		}
//		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//		: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
//	}
//	
//	private String getCheckSql(String moduleCode){
//		
//		StringBuilder checkSql = new StringBuilder();
//		//���ռ��ź͵������ͷ����ѯʱ�жϷ������и����Ƿ����1
//		//��ѯ���ֶ��м��ţ��������ƣ���Դ�������ͣ���Դ�����������ƣ���Դ�������ͣ���Դ�����������ƣ�Ŀ�ĵ������ͣ�Ŀ�ĵ����������ƣ�Ŀ�Ľ������ͣ�Ŀ�Ľ����������ƣ��ظ�����
//		checkSql.append("SELECT org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num FROM ");
//		//�����ֶΣ������ѯ���ظ�����
//		checkSql.append("(SELECT pk_group,src_billtype,dest_billtype,dest_transtype,src_transtype,count(*) num FROM pub_vochange GROUP BY pk_group,src_billtype,dest_billtype ,dest_transtype,src_transtype " );
//		checkSql.append("HAVING src_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"') or dest_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"')) counttable ");	
//		//�����Ӳ�ѯ��������
//		checkSql.append("LEFT JOIN  org_group ON org_group.pk_group = counttable.pk_group ");
//		//�����Ӳ�ѯ��Դ������������
//		checkSql.append("LEFT JOIN bd_billtype srcbilltable ON counttable.src_billtype = srcbilltable.pk_billtypecode ");
//		//�����Ӳ�ѯ��Դ������������
//		checkSql.append("LEFT JOIN bd_billtype srctanstable ON counttable.src_transtype = srctanstable.pk_billtypecode ");
//		//�����Ӳ�ѯĿ�ĵ�����������
//		checkSql.append("LEFT JOIN bd_billtype destbilltable ON counttable.dest_billtype = destbilltable.pk_billtypecode ");
//		//�����Ӳ�ѯĿ�Ľ�����������
//		checkSql.append("LEFT JOIN bd_billtype desttranstable ON counttable.dest_transtype = desttranstable.pk_billtypecode ");
//		
//		checkSql.append("WHERE num> 1");
//	
//		return checkSql.toString();
//	}
//
//	
	

}
