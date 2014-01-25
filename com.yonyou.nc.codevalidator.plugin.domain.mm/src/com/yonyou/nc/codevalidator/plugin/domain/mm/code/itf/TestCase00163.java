package com.yonyou.nc.codevalidator.plugin.domain.mm.code.itf;

import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
import com.yonyou.nc.codevalidator.sdk.upm.UpmComponentVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;

/**
 * EJB��������ӿڵ����Ʋ����ظ�
 * 
 * @author gaojf
 * @reporter
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT,catalog = CatalogEnum.JAVACODE, description = "EJB��������ӿڵ����Ʋ����ظ�", memo = "", solution = "EJB��������ӿڵ����Ʋ����ظ�", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "163", coder = "gaojf")
public class TestCase00163 extends AbstractRuleDefinition  {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
	
		//������е�upm�ļ�
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        
        MapList<String, UpmComponentVO> itfToUpmVOMap = new MapList<String, UpmComponentVO>();
        //����upm�����ӿ�ӳ���ϵ
        for (UpmResource upmResource : upmResources) {
        	UpmModuleVO upmModuleVo = upmResource.getUpmModuleVo();
        	itfToUpmVOMap = this.getPathtoUpmVOMap(upmModuleVo,itfToUpmVOMap);        	
        }
        
		if(null!=itfToUpmVOMap)
		{
			for(String itfname:itfToUpmVOMap.keySet()){
				List<UpmComponentVO> comvos = itfToUpmVOMap.get(itfname);
				if(null != comvos&&comvos.size()>1){
					StringBuffer sb = new StringBuffer();
					sb.append("�ӿڣ�");
					for(UpmComponentVO umpvo:comvos){						
						sb.append("[");
						sb.append(umpvo.getInterfaceName());						
						sb.append("],");							
					}
					sb.append("���ڽӿ������ظ�������м���޸ģ�\n");
					result.addResultElement(itfname, sb.toString());
				}
				
			}
		}
		
		return result;
	}

    /**
     * @param upmModuleVO
     * @return Map<String, String>
     */
    private MapList<String, UpmComponentVO> getPathtoUpmVOMap(UpmModuleVO upmModuleVO,MapList<String,UpmComponentVO> itfToUpmVOMap) {
     
        List<UpmComponentVO> ComponentVoList = upmModuleVO.getPubComponentVoList();       
        List<UpmComponentVO> priComponentVoList = upmModuleVO.getPriComponentVoList();
        if(priComponentVoList!=null)
        ComponentVoList.addAll(priComponentVoList);
        if (ComponentVoList != null && ComponentVoList.size() > 0) {
            for (UpmComponentVO upmComponentVO : ComponentVoList) {
                if (upmComponentVO.getInterfaceName() != null) {
                	String temp = upmComponentVO.getInterfaceName();
                	//���ӿ�������upmvo��Ӧ
                	itfToUpmVOMap.put(temp.substring(temp.lastIndexOf(".")+1), upmComponentVO);
                }
            }
        }
        return itfToUpmVOMap;
    }
}
