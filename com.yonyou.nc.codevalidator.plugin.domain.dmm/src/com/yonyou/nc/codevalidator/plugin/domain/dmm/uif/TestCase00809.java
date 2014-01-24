package com.yonyou.nc.codevalidator.plugin.domain.dmm.uif;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "muxh", description = "不需要合计行需要在配置文件中进行设置", 
relatedIssueId = "809", subCatalog = SubCatalogEnum.UF_CONFIGFILE)
/**
 * 勾选此规则，意在检测卡片是否设置了不显示和记行，如没显示设置，则抛错误提示。
 */
public class TestCase00809 extends AbstractXmlRuleDefinition{

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes,
				ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();


		if (resources == null || resources.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "请检查输入的功能编码是否正确！\n");
			return result;
		}

		for (XmlResource xmlResource : resources) {
//			List unlawfulNodes = new ArrayList();
			List<Element> billForm = xmlResource
					.getBeanElementByClass("nc.ui.uif2.editor.BillForm");						
			if(billForm==null||billForm.isEmpty()){
//				unlawfulNodes.add(xmlResource.getFuncNodeCode());
				continue;
			}
			for(Element ele:billForm){
				Element isShowTotal=xmlResource.getChildPropertyElement(ele, "showTotalLine");
				//如果没设置，则为true.抛出检查异常
				if(isShowTotal==null || isShowTotal.getAttribute("value").equals("true")){
//					unlawfulNodes.add(xmlResource.getFuncNodeCode());
//				}
//				else if(){
//					unlawfulNodes.add(xmlResource.getFuncNodeCode());
					result.addResultElement(xmlResource.getResourcePath(), "不需要合计行需要在配置文件中进行设置");
				}
			}
		}
//		if (unlawfulNodes.size() > 0) {
//			result.addResultElement(ruleExecContext.getBusinessComponent()
//					.getDisplayBusiCompName(), String.format(
//					"功能编码为：%s 的节点配置文件中,如要不显示合计行，请设置isShowTotalLine为false\n", new Object[] {
//							unlawfulNodes }));
//		}
		return result;

	}

}
