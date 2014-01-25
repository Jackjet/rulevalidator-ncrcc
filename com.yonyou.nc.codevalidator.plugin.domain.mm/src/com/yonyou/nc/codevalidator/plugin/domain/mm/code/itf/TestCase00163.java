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
 * EJB部署操作接口的名称不能重复
 * 
 * @author gaojf
 * @reporter
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT,catalog = CatalogEnum.JAVACODE, description = "EJB部署操作接口的名称不能重复", memo = "", solution = "EJB部署操作接口的名称不能重复", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "163", coder = "gaojf")
public class TestCase00163 extends AbstractRuleDefinition  {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
	
		//获得所有的upm文件
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        UpmResourceQuery upmResourceQuery = new UpmResourceQuery();
        upmResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
        
        MapList<String, UpmComponentVO> itfToUpmVOMap = new MapList<String, UpmComponentVO>();
        //根据upm构建接口映射关系
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
					sb.append("接口：");
					for(UpmComponentVO umpvo:comvos){						
						sb.append("[");
						sb.append(umpvo.getInterfaceName());						
						sb.append("],");							
					}
					sb.append("存在接口命名重复，请进行检查修改！\n");
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
                	//将接口名，与upmvo对应
                	itfToUpmVOMap.put(temp.substring(temp.lastIndexOf(".")+1), upmComponentVO);
                }
            }
        }
        return itfToUpmVOMap;
    }
}
